# 🔐 Secure Federated GraphQL with JWT, Role-Based Directives, and Apollo Federation

This project demonstrates how to build **secure, federated GraphQL microservices** using:

- 🧠 **Apollo Gateway (Node.js)** for federation & token propagation  
- ☕ **Spring Boot DGS services** for GraphQL subgraphs  
- 🔑 **JWT-based authentication and authorization**  
- 🛡️ **Custom `@Auth` directive** for fine-grained, role-based access control  

---

## 🏗️ Architecture Overview

                   +------------------------------------+
                   |           Apollo Gateway           |
                   |------------------------------------|
                   | • Issues JWT tokens                |
                   |   (/generate-token?role=ADMIN)     |
                   | • Validates and forwards tokens     |
                   |   to subgraphs                      |
                   | • Uses Apollo Federation to merge   |
                   |   schemas                           |
                   +------------------+------------------+
                                      |
        +-----------------------------+-----------------------------+
        |                                                           |
    +-----------------------+                             +-----------------------+
    |   🧩 Author Service   |                             |   📚 Book Service     |
    |   (Spring Boot DGS)   |                             |   (Spring Boot DGS)   |
    |-----------------------|                             |-----------------------|
    | • JWT validated via   |                             | • JWT validated via   |
    |   AuthInterceptor     |                             |   AuthInterceptor     |
    | • Custom @Auth        |                             | • Federation enabled  |
    |   directive for roles |                             |   schema integration  |
    | • DGS schema &        |                             | • DGS schema &        |
    |   resolvers           |                             |   resolvers           |
    +-----------------------+                             +-----------------------+

---

## 🚀 Setup & Run

### Start Subgraphs
🧩 Book Service - Runs on port 4001
```bash
cd book-service
mvn spring-boot:run
```
🧩 Author Service - Runs on port 4002
```bash
cd author-service
mvn spring-boot:run
```

### Start Apollo Gateway
Runs on port 4000
```bash
cd gateway
npm install
node index.js
```
---

## 🔑 Generate JWT Tokens

The **Apollo Gateway** provides a helper endpoint to generate JWT tokens for different roles:

| **Role** | **Example URL** |
|-----------|------------------|
| 🧑‍💻 USER  | [http://localhost:4000/generate-token?role=USER](http://localhost:4000/generate-token?role=USER) |
| 🛡️ ADMIN | [http://localhost:4000/generate-token?role=ADMIN](http://localhost:4000/generate-token?role=ADMIN) |

Each returns a JSON response like:

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6..."
}
```
---

 ## 🧠 Authentication & Authorization Flow
 ### Step 1: Client Requests Token

- A user hits /generate-token with a role query parameter.
- The gateway signs and returns a JWT containing:
  -  username
  - role
  - exp (expiration time)

```bash
Example Payload: 
{
  "username": "admin",
  "role": "USER",
  "exp": 1730406012
}
```

### Step 2: Token Propagation via Gateway

The Apollo Gateway automatically attaches the JWT to each downstream subgraph request:

```bash
willSendRequest({ request, context }) {
  request.http.headers.set(
    "authorization",
    `Bearer ${context.user.token}`
  );
}
```
This ensures each subgraph (e.g., Author, Book) receives the same Authorization header.

### Step 3: Token Validation in Subgraphs
Inside each Spring Boot DGS microservice, the AuthInterceptor intercepts every request and validates the JWT:
```bash
Claims claims = jwtUtil.decode(token);
authContext.setCurrentUser(Map.of(
    "role", claims.get("role"),
    "username", claims.get("username")
));
```
If valid — the user’s role and username are stored in a ThreadLocal context (AuthContext) for use in resolvers.

If invalid — the request proceeds as a guest or is rejected (based on configuration).

### Step 4: Role Enforcement with @Auth Directive
Each resolver can restrict access using the custom @Auth annotation:
```bash
@Auth(requires = "ADMIN")
public Author addAuthor(@InputArgument String name) {
    // Only ADMINs can add new authors
}
```
The AuthAspect automatically checks the current user's role before allowing the resolver to execute:
```bash
if (!userRole.equalsIgnoreCase(auth.requires())) {
    throw new RuntimeException("Forbidden - Requires " + auth.requires());
}
```

If unauthorized, the request fails with:
```bash
{
  "errors": [
    { "message": "Forbidden - Requires ADMIN" }
  ]
}
```
---

## 📘 Example GraphQL Operations

✅ Fetch All Books (requires USER)
```bash
query {
  getAllBooks {
    id
    title
    publishedYear
    author {
      id
      name
    }
  }
}
```

✅ Add Author (requires ADMIN)
```bash
mutation {
  addAuthor(name: "New Writer") {
    id
    name
  }
}
```
---

## ⚙️ Using JWT in GraphiQL / Postman
Add the JWT as a Bearer token in your HTTP headers:
```bash
Authorization: Bearer <your-token>
```
---