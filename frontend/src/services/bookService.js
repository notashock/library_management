import API from "./api";

export const getAllBooks = async () => {
  return await API.get("/books");
};

export const getAvailableBooks = async () => {
  return await API.get("/books/available");
};

export const addBook = async (bookData) => {
  return await API.post("/books", bookData);
};

export const searchBooks = async (keyword) => {
  return await API.get(
    `/books/search?keyword=${keyword}`
  );
};