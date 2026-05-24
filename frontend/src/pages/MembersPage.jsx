import { useState } from "react";

import {
  getMemberById,
} from "../services/memberService";

function MembersPage() {

  const [memberId, setMemberId] =
    useState("");

  const [member, setMember] =
    useState(null);

  const handleSearch = async () => {
    try {

      const memberData =
        await getMemberById(memberId);

      setMember(memberData);

    } catch (error) {

      console.log(error);

      alert("Member Not Found");
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 p-6">

      <h1 className="text-3xl font-bold mb-6">
        Member Details
      </h1>

      {/* SEARCH */}

      <div className="bg-white p-6 rounded-xl shadow-md mb-8">

        <div className="flex gap-4">

          <input
            type="number"
            placeholder="Enter Member ID"
            value={memberId}
            onChange={(e) =>
              setMemberId(e.target.value)
            }
            className="border p-3 rounded-lg flex-1"
          />

          <button
            onClick={handleSearch}
            className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg"
          >
            Search
          </button>
        </div>
      </div>

      {/* MEMBER DETAILS */}

      {member && (

        <div className="bg-white shadow-md rounded-xl p-6">

          <h2 className="text-2xl font-semibold">
            {member.name}
          </h2>

          <p className="mt-2 text-gray-600">
            {member.email}
          </p>

          <p className="mt-2">
            Role: {member.role}
          </p>

          <p className="mt-2">
            Member ID: {member.memberId}
          </p>

        </div>
      )}
    </div>
  );
}

export default MembersPage;