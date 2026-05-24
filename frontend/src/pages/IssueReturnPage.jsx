import { useEffect, useState } from "react";

import { getAllBooks } from "../services/bookService";
import { getAllMembers } from "../services/memberService";

import {
  getAllIssues,
  issueBook,
  returnBook,
} from "../services/issueService";

function IssueReturnPage() {
  const [books, setBooks] = useState([]);
  const [members, setMembers] = useState([]);
  const [issues, setIssues] = useState([]);

  const [formData, setFormData] = useState({
    bookId: "",
    memberId: "",
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const booksRes = await getAllBooks();
      const membersRes = await getAllMembers();
      const issuesRes = await getAllIssues();

      setBooks(booksRes.data);
      setMembers(membersRes.data);
      setIssues(issuesRes.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleIssueBook = async (e) => {
    e.preventDefault();

    try {
      await issueBook(formData);

      alert("Book Issued Successfully");

      setFormData({
        bookId: "",
        memberId: "",
      });

      fetchData();
    } catch (error) {
      console.log(error);

      alert(
        error.response?.data?.message ||
          "Failed to Issue Book"
      );
    }
  };

  const handleReturnBook = async (issueId) => {
    try {
      await returnBook(issueId);

      alert("Book Returned Successfully");

      fetchData();
    } catch (error) {
      console.log(error);

      alert(
        error.response?.data?.message ||
          "Failed to Return Book"
      );
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">
        Issue & Return Books
      </h1>

      {/* Issue Form */}

      <form
        onSubmit={handleIssueBook}
        className="bg-white shadow-md rounded-xl p-6 mb-8"
      >
        <h2 className="text-2xl font-semibold mb-4">
          Issue Book
        </h2>

        <div className="grid md:grid-cols-2 gap-4">
          {/* Select Book */}

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
            <option value="">Select Book</option>

            {books
              .filter(
                (book) =>
                  book.availability === "AVAILABLE"
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

          {/* Select Member */}

          <select
            value={formData.memberId}
            onChange={(e) =>
              setFormData({
                ...formData,
                memberId: e.target.value,
              })
            }
            className="border p-3 rounded-lg"
            required
          >
            <option value="">Select Member</option>

            {members.map((member) => (
              <option
                key={member.memberId}
                value={member.memberId}
              >
                {member.name}
              </option>
            ))}
          </select>
        </div>

        <button
          type="submit"
          className="mt-4 bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          Issue Book
        </button>
      </form>

      {/* Active Issues Table */}

      <div className="bg-white shadow-md rounded-xl p-6">
        <h2 className="text-2xl font-semibold mb-4">
          Active Issues
        </h2>

        <div className="overflow-x-auto">
          <table className="w-full border-collapse">
            <thead>
              <tr className="bg-gray-100">
                <th className="p-3 text-left">Issue ID</th>
                <th className="p-3 text-left">Book</th>
                <th className="p-3 text-left">Member</th>
                <th className="p-3 text-left">Issue Date</th>
                <th className="p-3 text-left">Status</th>
                <th className="p-3 text-left">Action</th>
              </tr>
            </thead>

            <tbody>
              {issues.map((issue) => (
                <tr
                  key={issue.issueId}
                  className="border-b"
                >
                  <td className="p-3">
                    {issue.issueId}
                  </td>

                  <td className="p-3">
                    {issue.book?.title}
                  </td>

                  <td className="p-3">
                    {issue.member?.name}
                  </td>

                  <td className="p-3">
                    {issue.issueDate}
                  </td>

                  <td className="p-3">
                    <span
                      className={`px-3 py-1 rounded-full text-white text-sm ${
                        issue.status === "ACTIVE"
                          ? "bg-green-500"
                          : "bg-gray-500"
                      }`}
                    >
                      {issue.status}
                    </span>
                  </td>

                  <td className="p-3">
                    {issue.status === "ACTIVE" && (
                      <button
                        onClick={() =>
                          handleReturnBook(
                            issue.issueId
                          )
                        }
                        className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg"
                      >
                        Return
                      </button>
                    )}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default IssueReturnPage;