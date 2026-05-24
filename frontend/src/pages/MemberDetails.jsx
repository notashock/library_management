import { useEffect, useState } from "react";

import { useParams } from "react-router-dom";

import { getMemberById } from "../services/memberService";

import { getIssuesByMember } from "../services/issueService";

function MemberDetails() {
  const { memberId } = useParams();

  const [member, setMember] = useState(null);

  const [issuedBooks, setIssuedBooks] =
    useState([]);

  useEffect(() => {
    fetchMemberDetails();
  }, []);

  const fetchMemberDetails = async () => {
    try {
      const memberRes =
        await getMemberById(memberId);

      const issuesRes =
        await getIssuesByMember(memberId);

      setMember(memberRes.data);

      setIssuedBooks(issuesRes.data);
    } catch (error) {
      console.log(error);
    }
  };

  if (!member) {
    return <h1>Loading...</h1>;
  }

  return (
    <div>
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

        <p className="mt-2">
          Member ID: {member.memberId}
        </p>
      </div>

      {/* Issued Books */}

      <div className="bg-white shadow-md rounded-xl p-6">
        <h2 className="text-2xl font-semibold mb-4">
          Issued Books
        </h2>

        {issuedBooks.length === 0 ? (
          <p>No books issued</p>
        ) : (
          <div className="grid md:grid-cols-3 gap-4">
            {issuedBooks.map((issue) => (
              <div
                key={issue.issueId}
                className="border rounded-xl p-4"
              >
                <h3 className="text-xl font-semibold">
                  {issue.book?.title}
                </h3>

                <p className="text-gray-600">
                  {issue.book?.author}
                </p>

                <p className="mt-2">
                  Issued On:
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
    </div>
  );
}

export default MemberDetails;