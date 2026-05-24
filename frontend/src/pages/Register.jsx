import { useState } from "react";

import { useNavigate } from "react-router-dom";

import API from "../services/api";

function Register() {
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    role: "MEMBER",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      await API.post(
        "/auth/register",
        formData
      );

      alert("Registration Successful");

      navigate("/login");
    } catch (error) {
      console.log(error);

      alert(
        error.response?.data?.message ||
          "Registration Failed"
      );
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100">
      
      <form
        onSubmit={handleSubmit}
        className="bg-white p-8 rounded-xl shadow-lg w-full max-w-md"
      >
        <h1 className="text-3xl font-bold text-center mb-6">
          Register
        </h1>

        {/* Name */}

        <input
          type="text"
          name="name"
          placeholder="Enter Name"
          value={formData.name}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg mb-4"
          required
        />

        {/* Email */}

        <input
          type="email"
          name="email"
          placeholder="Enter Email"
          value={formData.email}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg mb-4"
          required
        />

        {/* Password */}

        <input
          type="password"
          name="password"
          placeholder="Enter Password"
          value={formData.password}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg mb-4"
          required
        />

        {/* Role */}

        <select
          name="role"
          value={formData.role}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg mb-4"
        >
          <option value="MEMBER">
            MEMBER
          </option>

          <option value="LIBRARIAN">
            LIBRARIAN
          </option>
        </select>

        {/* Submit Button */}

        <button
          type="submit"
          className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg"
        >
          Register
        </button>
      </form>
    </div>
  );
}

export default Register;