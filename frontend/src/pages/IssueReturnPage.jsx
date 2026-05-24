import { useEffect, useState } from "react";

import { getAllBooks } from "../services/bookService";

import {
  issueBook,
  returnBook,
} from "../services/issueService";
import { toast } from "react-toastify";

function IssueReturnPage() {
  const [books, setBooks] = useState([]);

  const [returnedIssueId, setReturnedIssueId] =
    useState("");

  const [formData, setFormData] = useState({
    bookId: "",
    memberId: "",
  });

  useEffect(() => {
    fetchBooks();
  }, []);

  // FETCH BOOKS

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

  // ISSUE BOOK

  const handleIssueBook = async (e) => {
    e.preventDefault();

    try {
      await issueBook(formData);

      toast.success("Book Issued Successfully");

      setFormData({
        bookId: "",
        memberId: "",
      });

      fetchBooks();

    } catch (error) {
      console.error(error);

      toast.error(
        error.response?.data?.message ||
          "Failed to Issue Book"
      );
    }
  };

  // RETURN BOOK

  const handleReturnBook = async () => {
    try {
      await returnBook(
        returnedIssueId
      );

      toast.success(
        "Book Returned Successfully"
      );

      setReturnedIssueId("");

      fetchBooks();

    } catch (error) {
      console.error(error);

      toast.error(
        error.response?.data?.message ||
          "Failed to Return Book"
      );
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6">

      {/* PAGE TITLE */}

      <h1 className="text-3xl font-bold mb-6">
        Issue & Return Books
      </h1>

      {/* ISSUE BOOK FORM */}

      <form
        onSubmit={handleIssueBook}
        className="bg-white shadow-md rounded-xl p-6 mb-8"
      >
        <h2 className="text-2xl font-semibold mb-4">
          Issue Book
        </h2>

        <div className="grid md:grid-cols-2 gap-4">

          {/* BOOK DROPDOWN */}

          <select
            value={formData.bookId}
            onChange={(e) =>
              setFormData({
                ...formData,
                bookId: e.target.value,
              })
            }
            className="border p-3 rounded-lg"
            required
          >
            <option value="">
              Select Book
            </option>

            {books
              .filter(
                (book) =>
                  book.availability ===
                  "AVAILABLE"
              )
              .map((book) => (
                <option
                  key={book.bookId}
                  value={book.bookId}
                >
                  {book.title}
                </option>
              ))}
          </select>

          {/* MEMBER ID INPUT */}

          <input
            type="number"
            placeholder="Enter Member ID"
            value={formData.memberId}
            onChange={(e) =>
              setFormData({
                ...formData,
                memberId:
                  e.target.value,
              })
            }
            className="border p-3 rounded-lg"
            required
          />
        </div>

        {/* ISSUE BUTTON */}

        <button
          type="submit"
          className="mt-4 bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          Issue Book
        </button>
      </form>

      {/* RETURN BOOK SECTION */}

      <div className="bg-white shadow-md rounded-xl p-6">

        <h2 className="text-2xl font-semibold mb-4">
          Return Book
        </h2>

        <div className="flex flex-col md:flex-row gap-4">

          {/* ISSUE ID INPUT */}

          <input
            type="number"
            placeholder="Enter Issue ID"
            value={returnedIssueId}
            onChange={(e) =>
              setReturnedIssueId(
                e.target.value
              )
            }
            className="border p-3 rounded-lg flex-1"
          />

          {/* RETURN BUTTON */}

          <button
            onClick={handleReturnBook}
            className="bg-red-500 hover:bg-red-600 text-white px-6 py-3 rounded-lg"
          >
            Return Book
          </button>
        </div>
      </div>
    </div>
  );
}

export default IssueReturnPage;