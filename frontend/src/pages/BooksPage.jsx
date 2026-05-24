import { useEffect, useState } from "react";

import {
  getAllBooks,
  getAvailableBooks,
  addBook,
  searchBooks,
} from "../services/bookService";

import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";

function BooksPage() {
  const { user } = useAuth();

  const [books, setBooks] = useState([]);

  const [searchKeyword, setSearchKeyword] =
    useState("");

  const [showAvailableOnly, setShowAvailableOnly] =
    useState(false);

  const [formData, setFormData] = useState({
    title: "",
    author: "",
  });

  useEffect(() => {
    fetchBooks();
  }, []);

  const fetchBooks = async () => {
    try {
      const booksData =
  await getAllBooks();

setBooks(booksData);
    } catch (error) {
      console.error(error);
      toast.error("Failed to fetch books.");
    }
  };

  // Add Book

  const handleAddBook = async (e) => {
    e.preventDefault();

    try {
      await addBook(formData);

      toast.success("Book Added Successfully");

      setFormData({
        title: "",
        author: "",
      });

      fetchBooks();
    } catch (error) {
      console.error(error);

      toast.error("Failed to Add Book");
    }
  };

  // Search Books

  const handleSearch = async () => {
    try {
      if (searchKeyword.trim() === "") {
        fetchBooks();
        return;
      }

      const booksData =
  await searchBooks(searchKeyword);

setBooks(booksData);
    } catch (error) {
      console.error(error);
      toast.error("Failed to search books.");
    }
  };

  // Filter Available Books

  const handleAvailableFilter = async () => {
    try {
      if (!showAvailableOnly) {
        const booksData =
  await getAvailableBooks();

setBooks(booksData);
      } else {
        fetchBooks();
      }

      setShowAvailableOnly(
        !showAvailableOnly
      );
    } catch (error) {
      console.error(error);
      toast.error("Failed to filter books.");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">
        Books Management
      </h1>

      {/* Librarian Add Book Form */}

      {user?.role === "LIBRARIAN" && (
        <form
          onSubmit={handleAddBook}
          className="bg-white shadow-md rounded-xl p-6 mb-8"
        >
          <h2 className="text-2xl font-semibold mb-4">
            Add New Book
          </h2>

          <div className="grid md:grid-cols-2 gap-4">
            <input
              type="text"
              placeholder="Book Title"
              value={formData.title}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  title: e.target.value,
                })
              }
              className="border p-3 rounded-lg"
              required
            />

            <input
              type="text"
              placeholder="Author Name"
              value={formData.author}
              onChange={(e) =>
                setFormData({
                  ...formData,
                  author: e.target.value,
                })
              }
              className="border p-3 rounded-lg"
              required
            />
          </div>

          <button
            type="submit"
            className="mt-4 bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
          >
            Add Book
          </button>
        </form>
      )}

      {/* Search + Filter */}

      <div className="flex flex-col md:flex-row gap-4 mb-6">
        <input
          type="text"
          placeholder="Search by title or author"
          value={searchKeyword}
          onChange={(e) =>
            setSearchKeyword(e.target.value)
          }
          className="border p-3 rounded-lg flex-1"
        />

        <button
          onClick={handleSearch}
          className="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-lg"
        >
          Search
        </button>

        <button
          onClick={handleAvailableFilter}
          className={`px-6 py-3 rounded-lg text-white ${
            showAvailableOnly
              ? "bg-red-500 hover:bg-red-600"
              : "bg-blue-500 hover:bg-blue-600"
          }`}
        >
          {showAvailableOnly
            ? "Show All"
            : "Available Only"}
        </button>
      </div>

      {/* Books Grid */}

      <div className="grid md:grid-cols-3 gap-4">
        {books.map((book) => (
          <div
            key={book.bookId}
            className="bg-white shadow-md rounded-xl p-5"
          >
            <h2 className="text-xl font-semibold">
              {book.title}
            </h2>

            <p className="text-gray-600 mt-2">
              {book.author}
            </p>

            <div className="mt-4">
              <span
                className={`px-3 py-1 rounded-full text-white text-sm ${
                  book.availability ===
                  "AVAILABLE"
                    ? "bg-green-500"
                    : "bg-red-500"
                }`}
              >
                {book.availability}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default BooksPage;