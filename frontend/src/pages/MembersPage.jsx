import { useState } from "react";

import API from "../services/api";

import {
  getMemberById,
  getMemberIssues,
} from "../services/memberService";
import { toast } from "react-toastify";

function MembersPage() {

  // REGISTER FORM

  const [registerData, setRegisterData] =
    useState({
      name: "",
      email: "",
      password: "",
      role: "MEMBER",
    });

  // SEARCH MEMBER

  const [memberId, setMemberId] =
    useState("");

  const [member, setMember] =
    useState(null);

  const [issues, setIssues] =
    useState([]);

  // REGISTER MEMBER

  const handleRegister = async (e) => {

    e.preventDefault();

    try {

      await API.post(
        "/auth/register",
        registerData
      );

      toast.success(
        "Member Registered Successfully"
      );

      setRegisterData({
        name: "",
        email: "",
        password: "",
        role: "MEMBER",
      });

    } catch (error) {

      console.error(error);

      toast.error(
        error.response?.data?.message ||
        "Registration Failed"
      );
    }
  };

  // SEARCH MEMBER

  const handleSearch = async () => {

    try {

      const memberData =
        await getMemberById(memberId);

      const issuesData =
        await getMemberIssues(memberId);

      setMember(memberData);

      setIssues(issuesData);

    } catch (error) {

      console.error(error);

      toast.error("Member Not Found");
    }
  };

  return (

    <div className="min-h-screen bg-gray-100 p-6">

      <h1 className="text-3xl font-bold mb-6">
        Members Management
      </h1>

      {/* REGISTER MEMBER */}

      <form
        onSubmit={handleRegister}
        className="bg-white p-6 rounded-xl shadow-md mb-8"
      >

        <h2 className="text-2xl font-semibold mb-4">
          Register New Member
        </h2>

        <div className="grid md:grid-cols-2 gap-4">

          <input
            type="text"
            placeholder="Enter Name"
            value={registerData.name}
            onChange={(e) =>
              setRegisterData({
                ...registerData,
                name: e.target.value,
              })
            }
            className="border p-3 rounded-lg"
            required
          />

          <input
            type="email"
            placeholder="Enter Email"
            value={registerData.email}
            onChange={(e) =>
              setRegisterData({
                ...registerData,
                email: e.target.value,
              })
            }
            className="border p-3 rounded-lg"
            required
          />

          <input
            type="password"
            placeholder="Enter Password"
            value={registerData.password}
            onChange={(e) =>
              setRegisterData({
                ...registerData,
                password: e.target.value,
              })
            }
            className="border p-3 rounded-lg"
            required
          />

          <select
            value={registerData.role}
            onChange={(e) =>
              setRegisterData({
                ...registerData,
                role: e.target.value,
              })
            }
            className="border p-3 rounded-lg"
          >
            <option value="MEMBER">
              MEMBER
            </option>

            <option value="LIBRARIAN">
              LIBRARIAN
            </option>
          </select>
        </div>

        <button
          type="submit"
          className="mt-4 bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          Register
        </button>
      </form>

      {/* SEARCH MEMBER */}

      <div className="bg-white p-6 rounded-xl shadow-md mb-8">

        <h2 className="text-2xl font-semibold mb-4">
          View Member Details
        </h2>

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
            className="bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-lg"
          >
            Search
          </button>
        </div>
      </div>

      {/* MEMBER DETAILS */}

      {member && (

        <div className="bg-white p-6 rounded-xl shadow-md">

          <h2 className="text-2xl font-semibold">
            {member.name}
          </h2>

          <p className="mt-2">
            {member.email}
          </p>

          <p className="mt-2">
            Role: {member.role}
          </p>

          <p className="mt-2">
            Member ID: {member.memberId}
          </p>

          {/* ISSUED BOOKS */}

          <div className="mt-6">

            <h3 className="text-xl font-semibold mb-4">
              Issued Books
            </h3>

            {issues.length === 0 ? (

              <p>No books issued</p>

            ) : (

              <div className="grid md:grid-cols-2 gap-4">

                {issues.map((issue) => (

                  <div
                    key={issue.issueId}
                    className="border p-4 rounded-lg"
                  >

                    <h4 className="text-lg font-semibold">
                      {issue.book?.title}
                    </h4>

                    <p className="text-gray-600">
                      {issue.book?.author}
                    </p>

                    <p className="mt-2">
                      Status: {issue.status}
                    </p>

                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default MembersPage;