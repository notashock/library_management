import { useEffect, useState } from "react";

import { getMyBooks } from "../services/issueService";

function MyBooks() {
  const [books, setBooks] = useState([]);

  useEffect(() => {
    fetchMyBooks();
  }, []);

  const fetchMyBooks = async () => {
    try {
      const response =
        await getMyBooks();

      setBooks(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">
        My Issued Books
      </h1>

      {books.length === 0 ? (
        <p>No books issued</p>
      ) : (
        <div className="grid md:grid-cols-3 gap-4">
          {books.map((issue) => (
            <div
              key={issue.issueId}
              className="bg-white shadow-md rounded-xl p-5"
            >
              <h2 className="text-xl font-semibold">
                {issue.book?.title}
              </h2>

              <p className="text-gray-600 mt-2">
                {issue.book?.author}
              </p>

              <p className="mt-2">
                Issue Date:
                {issue.issueDate}
              </p>

              <span className="bg-green-500 text-white px-3 py-1 rounded-full text-sm">
                {issue.status}
              </span>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default MyBooks;