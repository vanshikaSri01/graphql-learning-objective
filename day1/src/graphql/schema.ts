import authorType from "./typeDefs/authorTypeDefs.js";
import bookType from "./typeDefs/bookTypeDefs.js";

import authorResolver from "./resolvers/authorResolver.js";
import bookResolver from "./resolvers/bookResolver.js";

const typeDefs = [authorType, bookType];
const resolvers = [authorResolver, bookResolver];

export { typeDefs, resolvers };
