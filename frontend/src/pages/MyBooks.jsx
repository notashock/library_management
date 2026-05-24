import { useEffect, useState } from "react";
import { getMemberIssues } from "../services/memberService";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";

function MyBooks() {
  const { user } = useAuth();
  const [issuedBooks, setIssuedBooks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.memberId) {
      fetchIssuedBooks();
    }
  }, [user]);

  const fetchIssuedBooks = async () => {
    try {
      const issuesRes = await getMemberIssues(user.memberId);

      setIssuedBooks(issuesRes.data);
    } catch (error) {
      console.error("Failed to fetch issued books:", error);
      toast.error("Failed to load issued books.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <h1 className="text-center mt-10 text-2xl">Loading...</h1>;
  }

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">
        My Books
      </h1>

      <div className="bg-white shadow-md rounded-xl p-6">
        {issuedBooks.length === 0 ? (
          <p className="text-gray-500 italic">You have no books currently issued.</p>
        ) : (
          <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4">
            {issuedBooks.map((issue) => (
              <div
                key={issue.issueId}
                className="border rounded-xl p-4 flex flex-col justify-between"
              >
                <div>
                  <h3 className="text-xl font-semibold">
                    {issue.book?.title}
                  </h3>
                  <p className="text-gray-600 text-sm">
                    {issue.book?.author}
                  </p>
                  <p className="mt-3 text-sm font-medium">
                    Issued On: {issue.issueDate}
                  </p>
                </div>
                
                <div className="mt-4">
                  <span className={`px-3 py-1 rounded-full text-sm font-bold ${
                    issue.status === 'ACTIVE' 
                      ? 'bg-blue-100 text-blue-700' 
                      : 'bg-green-100 text-green-700'
                  }`}>
                    {issue.status}
                  </span>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}

export default MyBooks;
