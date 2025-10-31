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


const PORT = process.env.PORT || 4000;
const SECRET = "my-super-secret-key-that-should-be-long-123456789";
const app = express();

// ✅ Token Generator Endpoint
app.get("/generate-token", (req, res) => {
  const username = req.query.username || "test-user";
  const role = req.query.role || "USER";
  const token = jwt.sign(
    { username, role },
    SECRET,
    { expiresIn: "1h" }
  );
  res.json({ token });
});

// ✅ Apollo Gateway Setup with header forwarding
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

// ✅ Apollo Server setup
const server = new ApolloServer({
  gateway,
  introspection: true,
});

// Start server
await server.start();

// ✅ Express Middleware for Gateway
app.use(
  "/graphql",
  cors(),
  bodyParser.json(),
  expressMiddleware(server, {
    context: async ({ req }) => {
      const authHeader = req.headers.authorization || "";
      const token = authHeader.replace("Bearer ", "");

      if (token) {
        try {
          const decoded = jwt.verify(token, SECRET);
          console.log("✅ Token decoded:", decoded);
          return { user: { ...decoded, token } };
        } catch (err) {
          console.log("❌ Invalid token:", err.message);
        }
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
});
