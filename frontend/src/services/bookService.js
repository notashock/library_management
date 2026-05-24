import API from "./api";

// GET ALL BOOKS

export const getAllBooks = async () => {
  const response = await API.get("/books");

  return response.data.data;
};

// GET AVAILABLE BOOKS

export const getAvailableBooks = async () => {
  const response = await API.get(
    "/books/available"
  );

  return response.data.data;
};

// ADD BOOK

export const addBook = async (
  bookData
) => {
  const response = await API.post(
    "/books",
    bookData
  );

  return response.data.data;
};

// SEARCH BOOKS

export const searchBooks = async (
  query
) => {
  const response = await API.get(
    `/books/search?query=${query}`
  );

  return response.data.data;
};