interface Author {
  id: string;
  name: string;
}

interface Book {
  id: string;
  title: string;
  genre: string;
  authorId: string;
}

export const authors: Author[] = [
  { id: "1", name: "George Orwell" },
  { id: "2", name: "Harper Lee" },
  { id: "3", name: "J.K. Rowling" },
  { id: "4", name: "J.R.R. Tolkien" },
  { id: "5", name: "F. Scott Fitzgerald" },
  { id: "6", name: "Jane Austen" },
];

export const books: Book[] = [
  { id: "1", title: "1984", genre: "Dystopian", authorId: "1" },
  { id: "2", title: "Animal Farm", genre: "Political Satire", authorId: "1" },
  { id: "3", title: "To Kill a Mockingbird", genre: "Fiction", authorId: "2" },
  { id: "4", title: "Harry Potter and the Sorcerer's Stone", genre: "Fantasy", authorId: "3" },
  { id: "5", title: "Harry Potter and the Chamber of Secrets", genre: "Fantasy", authorId: "3" },
  { id: "6", title: "The Hobbit", genre: "Fantasy", authorId: "4" },
  { id: "7", title: "The Lord of the Rings: The Fellowship of the Ring", genre: "Fantasy", authorId: "4" },
  { id: "8", title: "The Great Gatsby", genre: "Fiction", authorId: "5" },
  { id: "9", title: "Pride and Prejudice", genre: "Romance", authorId: "6" },
  { id: "10", title: "Sense and Sensibility", genre: "Romance", authorId: "6" },
];
