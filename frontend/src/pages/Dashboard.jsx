import { Link } from "react-router-dom";

import { useAuth } from "../context/AuthContext";

function Dashboard() {
  const { user } = useAuth();

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      
      {/* Header */}

      <div className="mb-8">
        <h1 className="text-4xl font-bold text-gray-800">
          Library Management Dashboard
        </h1>

        <p className="text-gray-600 mt-2">
          Welcome, {user?.role}
        </p>
      </div>

      {/* Librarian Dashboard */}

      {user?.role === "LIBRARIAN" && (
        <div className="grid md:grid-cols-3 gap-6">
          
          {/* Books */}

          <Link to="/books">
            <div className="bg-white shadow-md rounded-2xl p-6 hover:shadow-xl transition">
              <h2 className="text-2xl font-semibold mb-2">
                Books
              </h2>

              <p className="text-gray-600">
                Add, search, and manage books.
              </p>
            </div>
          </Link>

          {/* Members */}

          <Link to="/members">
            <div className="bg-white shadow-md rounded-2xl p-6 hover:shadow-xl transition">
              <h2 className="text-2xl font-semibold mb-2">
                Members
              </h2>

              <p className="text-gray-600">
                Register and manage members.
              </p>
            </div>
          </Link>

          {/* Issues */}

          <Link to="/issues">
            <div className="bg-white shadow-md rounded-2xl p-6 hover:shadow-xl transition">
              <h2 className="text-2xl font-semibold mb-2">
                Issue & Return
              </h2>

              <p className="text-gray-600">
                Issue and return books.
              </p>
            </div>
          </Link>
        </div>
      )}

      {/* Member Dashboard */}

      {user?.role === "MEMBER" && (
        <div className="grid md:grid-cols-2 gap-6">
          
          {/* Books */}

          <Link to="/books">
            <div className="bg-white shadow-md rounded-2xl p-6 hover:shadow-xl transition">
              <h2 className="text-2xl font-semibold mb-2">
                Books Catalog
              </h2>

              <p className="text-gray-600">
                View and search books.
              </p>
            </div>
          </Link>

          {/* My Books */}

          <Link to="/my-books">
            <div className="bg-white shadow-md rounded-2xl p-6 hover:shadow-xl transition">
              <h2 className="text-2xl font-semibold mb-2">
                My Books
              </h2>

              <p className="text-gray-600">
                View your issued books.
              </p>
            </div>
          </Link>

          {/* My Profile */}

          <Link to="/my-profile">
            <div className="bg-white shadow-md rounded-2xl p-6 hover:shadow-xl transition">
              <h2 className="text-2xl font-semibold mb-2">
                My Profile
              </h2>

              <p className="text-gray-600">
                View personal details.
              </p>
            </div>
          </Link>
        </div>
      )}
    </div>
  );
}

export default Dashboard;