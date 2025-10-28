import { ApolloServer } from '@apollo/server';
import { expressMiddleware } from '@apollo/server/express4';
import { ApolloGateway, IntrospectAndCompose } from '@apollo/gateway';
import express from 'express';

const app = express();

const gateway = new ApolloGateway({
  supergraphSdl: new IntrospectAndCompose({
    subgraphs: [
      { name: 'books', url: 'http://localhost:4001/graphql' },
      { name: 'authors', url: 'http://localhost:4002/graphql' },
    ],
  }),
});

const server = new ApolloServer({
  gateway,
  introspection: true,
});

await server.start(); // ✅ must be awaited BEFORE use()

// ✅ register JSON middleware BEFORE expressMiddleware
app.use(express.json());
app.use('/graphql', expressMiddleware(server));

const PORT = 4000;
app.listen(PORT, () => {
  console.log(`🚀 Federation Gateway running at http://localhost:${PORT}/graphql`);
});
