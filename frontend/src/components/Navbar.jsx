import { Link } from "react-router-dom";

import { useAuth } from "../context/AuthContext";

function Navbar() {
  const { user, logout } = useAuth();

  return (
    <nav className="bg-blue-600 text-white p-4 shadow-md">
      <div className="flex justify-between items-center">
        
        {/* Left Side Links */}

        <div className="flex gap-6 items-center">
          
          {/* Librarian Dashboard */}

          {user?.role === "LIBRARIAN" && (
            <Link
              to="/"
              className="hover:text-gray-200"
            >
              Dashboard
            </Link>
          )}

          {/* Common Route */}

          <Link
            to="/books"
            className="hover:text-gray-200"
          >
            Books
          </Link>

          {/* Librarian Routes */}

          {user?.role === "LIBRARIAN" && (
            <>
              <Link
                to="/members"
                className="hover:text-gray-200"
              >
                Members
              </Link>

              <Link
                to="/issues"
                className="hover:text-gray-200"
              >
                Issues
              </Link>
            </>
          )}

          {/* Member Routes */}

          
        </div>

        {/* Right Side */}

        <div className="flex items-center gap-4">
          
          <span className="bg-white text-blue-600 px-3 py-1 rounded-full text-sm font-semibold">
            {user?.role}
          </span>

          <button
            onClick={logout}
            className="bg-red-500 hover:bg-red-600 px-4 py-2 rounded-lg"
          >
            Logout
          </button>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;