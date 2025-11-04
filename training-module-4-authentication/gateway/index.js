import express from "express";
import { ApolloServer } from "@apollo/server";
import { expressMiddleware } from "@apollo/server/express4";
import {
  ApolloGateway,
  IntrospectAndCompose,
  RemoteGraphQLDataSource,
} from "@apollo/gateway";
import cors from "cors";
import bodyParser from "body-parser";
import jwt from "jsonwebtoken";
import crypto from "crypto";
import fs from "fs";
import path from "path";

const PORT = process.env.PORT || 4000;
const SECRET = "my-super-secret-key-that-should-be-long-123456789";
const NODE_ENV = process.env.NODE_ENV || "development";
const app = express();

// ✅ Load Whitelist of Allowed Query Hashes
const whitelistPath = path.join(process.cwd(), "whitelist", "queries.json");
const whitelist = fs.existsSync(whitelistPath)
  ? JSON.parse(fs.readFileSync(whitelistPath, "utf-8"))
  : {};

console.log(`📜 Loaded ${Object.keys(whitelist).length} whitelisted queries`);

// ✅ Token Generator Endpoint
app.get("/generate-token", (req, res) => {
  const username = req.query.username || "test-user";
  const role = req.query.role || "USER";
  const token = jwt.sign({ username, role }, SECRET, { expiresIn: "1h" });
  console.log(`🔑 Generated token for ${username} (${role})`);
  res.json({ token });
});

// ✅ Apollo Gateway Setup
const gateway = new ApolloGateway({
  supergraphSdl: new IntrospectAndCompose({
    subgraphs: [
      { name: "author", url: "http://localhost:4002/graphql" },
      { name: "book", url: "http://localhost:4001/graphql" },
    ],
  }),

  // Forward Authorization header to subgraphs
  buildService({ url }) {
    return new RemoteGraphQLDataSource({
      url,
      willSendRequest({ request, context }) {
        if (context?.user?.token) {
          request.http.headers.set(
            "authorization",
            `Bearer ${context.user.token}`
          );
        }
      },
    });
  },
});

// ✅ APQ Cache with Logging
const apqCache = new Map();

const server = new ApolloServer({
  gateway,
  introspection: NODE_ENV !== "production",
  persistedQueries: {
    cache: {
      get: (key) => {
        const found = apqCache.get(key);
        if (found) {
          console.log(`⚡ APQ HIT: ${key}`);
        } else {
          console.log(`🌀 APQ MISS: ${key}`);
        }
        return found;
      },
      set: (key, val) => {
        console.log(`💾 APQ STORED: ${key}`);
        apqCache.set(key, val);
      },
      delete: (key) => {
        console.log(`🗑️ APQ DELETED: ${key}`);
        apqCache.delete(key);
      },
    },
  },
});

await server.start();

// ✅ Body parser must come BEFORE whitelist check
app.use("/graphql", bodyParser.json());

/**
 * ✅ Whitelist Middleware
 * Only allows pre-approved persisted query hashes
 */
app.use("/graphql", (req, res, next) => {
  const { query, extensions } = req.body || {};
  const persistedHash = extensions?.persistedQuery?.sha256Hash;

  let hash;
  if (query) {
    hash = crypto.createHash("sha256").update(query).digest("hex");
  } else if (persistedHash) {
    hash = persistedHash;
  }

  console.log(`🧩 Incoming Query Hash: ${hash || "N/A"}`);

  if (hash && whitelist[hash]) {
    console.log(`✅ Whitelisted query executed: ${whitelist[hash].name}`);
    req.body.query = whitelist[hash].query;
    return next();
  }

  if (NODE_ENV !== "production") {
    console.warn("⚠️ Non-whitelisted query executed (development mode)");
    return next();
  }

  console.error(`🚫 Blocked query with hash: ${hash}`);
  return res.status(403).json({ error: "Query not whitelisted" });
});

// ✅ Express Middleware for Apollo Gateway
app.use(
  "/graphql",
  cors(),
  expressMiddleware(server, {
    context: async ({ req }) => {
      const authHeader = req.headers.authorization || "";
      const token = authHeader.replace("Bearer ", "");

      if (token) {
        try {
          const decoded = jwt.verify(token, SECRET);
          console.log(`👤 Authenticated: ${decoded.username} (${decoded.role})`);
          return { user: { ...decoded, token } };
        } catch (err) {
          console.log("❌ Invalid token:", err.message);
        }
      } else {
        console.log("👥 Guest request received (no token)");
      }

      return { user: { role: "GUEST" } };
    },
  })
);

// ✅ Start Express Server
app.listen(PORT, () => {
  console.log(`🚀 Gateway running at http://localhost:${PORT}/graphql`);
  console.log(
    `🔑 Generate token: http://localhost:${PORT}/generate-token?role=ADMIN`
  );
  console.log(`🌍 Environment: ${NODE_ENV}`);
});
