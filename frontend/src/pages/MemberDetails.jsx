import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

// FIXED: Consolidated imports to use both functions from your memberService
import { getMemberById, getMemberIssues } from "../services/memberService";

function MemberDetails() {
  const { memberId } = useParams();
  const [member, setMember] = useState(null);
  const [issuedBooks, setIssuedBooks] = useState([]);

  useEffect(() => {
    if (memberId) {
      fetchMemberDetails();
    }
  }, [memberId]);

  const fetchMemberDetails = async () => {
    try {
      const memberRes = await getMemberById(memberId);
      
      // FIXED: Using the correct function name from memberService
      const issuesRes = await getMemberIssues(memberId); 

      // FIXED: Both responses are wrapped in our backend's ApiResponse class.
      // We must extract the inner '.data' property from BOTH payloads.
      setMember(memberRes.data);
      setIssuedBooks(issuesRes.data); 
      
    } catch (error) {
      console.error("Failed to fetch member data:", error);
    }
  };

  if (!member) {
    return <h1 className="text-center mt-10 text-2xl">Loading...</h1>;
  }

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">
        Member Details
      </h1>

      {/* Member Info */}
      <div className="bg-white shadow-md rounded-xl p-6 mb-8">
        <h2 className="text-2xl font-semibold">
          {member.name}
        </h2>
        <p className="text-gray-600 mt-2">
          {member.email}
        </p>
        <p className="mt-2 text-sm text-gray-500">
          Member ID: {member.memberId}
        </p>
      </div>

      {/* Issued Books */}
      <div className="bg-white shadow-md rounded-xl p-6">
        <h2 className="text-2xl font-semibold mb-4">
          Issued Books
        </h2>

        {issuedBooks.length === 0 ? (
          <p className="text-gray-500 italic">No books currently issued.</p>
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

export default MemberDetails;