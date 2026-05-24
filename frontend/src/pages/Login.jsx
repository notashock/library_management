import { useState } from "react";

import {
  useNavigate,
  Link,
} from "react-router-dom";

import { jwtDecode } from "jwt-decode";

import API from "../services/api";

import { useAuth } from "../context/AuthContext";

function Login() {

  const navigate = useNavigate();

  const { login } = useAuth();

  const [loading, setLoading] =
    useState(false);

  const [formData, setFormData] =
    useState({
      email: "",
      password: "",
    });

  // HANDLE INPUT CHANGE

  const handleChange = (e) => {

    setFormData({
      ...formData,
      [e.target.name]:
        e.target.value,
    });
  };

  // HANDLE LOGIN

  const handleSubmit = async (e) => {

    e.preventDefault();

    setLoading(true);

    try {

      const response =
        await API.post(
          "/auth/login",
          formData
        );

      const token =
        response.data.token;

      // SAVE TOKEN

      login(token);

      // DECODE TOKEN

      const decoded =
        jwtDecode(token);

      const role =
        decoded.role;
        console.log(decoded);
console.log(role);

      alert("Login Successful");

      // ROLE BASED NAVIGATION

      if (
        role === "LIBRARIAN"
      ) {

        navigate("/dashboard");

      } else if (
        role === "MEMBER"
      ) {

        navigate("/books");
      }

    } catch (error) {

      console.log(error);

      alert(
        error.response?.data?.message ||
        "Invalid Credentials"
      );

    } finally {

      setLoading(false);
    }
  };

  return (

    <div className="min-h-screen flex items-center justify-center bg-gray-100">

      <form
        onSubmit={handleSubmit}
        className="bg-white shadow-lg rounded-2xl p-8 w-full max-w-md"
      >

        {/* TITLE */}

        <h1 className="text-3xl font-bold text-center mb-6">
          Login
        </h1>

        {/* EMAIL */}

        <input
          type="email"
          name="email"
          placeholder="Enter Email"
          value={formData.email}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg mb-4"
          required
        />

        {/* PASSWORD */}

        <input
          type="password"
          name="password"
          placeholder="Enter Password"
          value={formData.password}
          onChange={handleChange}
          className="w-full border p-3 rounded-lg mb-6"
          required
        />

        {/* LOGIN BUTTON */}

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-lg"
        >
          {loading
            ? "Logging in..."
            : "Login"}
        </button>

        {/* REGISTER LINK */}

        <p className="text-center mt-4">

          Don't have an account?

          <Link
            to="/register"
            className="text-blue-600 font-semibold ml-2"
          >
            Register
          </Link>
        </p>

      </form>
    </div>
  );
}

export default Login;