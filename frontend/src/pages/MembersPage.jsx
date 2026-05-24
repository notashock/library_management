import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import {
  getAllMembers,
  addMember,
} from "../services/memberService";

function MembersPage() {
  const [members, setMembers] = useState([]);

  const [formData, setFormData] = useState({
    name: "",
    email: "",
  });

  useEffect(() => {
    fetchMembers();
  }, []);

  const fetchMembers = async () => {
    try {
      const response = await getAllMembers();
      setMembers(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await addMember(formData);

      setFormData({
        name: "",
        email: "",
      });

      fetchMembers();

      alert("Member Added Successfully");
    } catch (error) {
      console.log(error);
      alert("Failed to Add Member");
    }
  };

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">
        Members Management
      </h1>

      {/* Add Member Form */}

      <form
        onSubmit={handleSubmit}
        className="bg-white shadow-md rounded-xl p-6 mb-8"
      >
        <h2 className="text-2xl font-semibold mb-4">
          Add New Member
        </h2>

        <div className="grid md:grid-cols-2 gap-4">
          <input
            type="text"
            name="name"
            placeholder="Enter Name"
            value={formData.name}
            onChange={handleChange}
            className="border p-3 rounded-lg outline-none focus:ring-2 focus:ring-blue-400"
            required
          />

          <input
            type="email"
            name="email"
            placeholder="Enter Email"
            value={formData.email}
            onChange={handleChange}
            className="border p-3 rounded-lg outline-none focus:ring-2 focus:ring-blue-400"
            required
          />
        </div>

        <button
          type="submit"
          className="mt-4 bg-blue-600 hover:bg-blue-700 text-white px-6 py-2 rounded-lg"
        >
          Add Member
        </button>
        <Link
  to={`/members/${member.memberId}`}
  className="mt-4 inline-block bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg"
>
  View Details
</Link>
      </form>
            
      {/* Members List */}

      <div className="grid md:grid-cols-3 gap-4">
        {members.map((member) => (
          <div
            key={member.memberId}
            className="bg-white shadow-md rounded-xl p-5"
          >
            <h2 className="text-xl font-semibold">
              {member.name}
            </h2>

            <p className="text-gray-600 mt-2">
              {member.email}
            </p>

            <div className="mt-4">
              <span className="bg-green-100 text-green-700 px-3 py-1 rounded-full text-sm">
                Member ID: {member.memberId}
              </span>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default MembersPage;