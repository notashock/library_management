import { Routes, Route } from "react-router-dom";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

import Navbar from "./components/Navbar";

import Dashboard from "./pages/Dashboard";
import BooksPage from "./pages/BooksPage";
import MembersPage from "./pages/MembersPage";
import IssueReturnPage from "./pages/IssueReturnPage";
import Login from "./pages/Login";
import Register from "./pages/Register";
import Unauthorized from "./pages/Unauthorized";
import MemberDetails from "./pages/MemberDetails";
import MyProfile from "./pages/MyProfile";
import MyBooks from "./pages/MyBooks";

import ProtectedRoute from "./components/ProtectedRoute";

function App() {

  return (
    <div>
      <ToastContainer position="top-right" autoClose={3000} hideProgressBar={false} closeOnClick pauseOnHover />
      <Navbar />

      <div className="p-6">

        <Routes>

          {/* PUBLIC ROUTES */}

          <Route
            path="/login"
            element={<Login />}
          />

          <Route
            path="/register"
            element={<Register />}
          />

          <Route
            path="/unauthorized"
            element={<Unauthorized />}
          />

          {/* LIBRARIAN DASHBOARD */}

          <Route
            path="/dashboard"
            element={
              <ProtectedRoute
                allowedRoles={["LIBRARIAN"]}
              >
                <Dashboard />
              </ProtectedRoute>
            }
          />

          {/* BOOKS */}

          <Route
            path="/books"
            element={
              <ProtectedRoute
                allowedRoles={[
                  "LIBRARIAN",
                  "MEMBER",
                ]}
              >
                <BooksPage />
              </ProtectedRoute>
            }
          />

          {/* MEMBERS */}

          <Route
            path="/members"
            element={
              <ProtectedRoute
                allowedRoles={["LIBRARIAN"]}
              >
                <MembersPage />
              </ProtectedRoute>
            }
          />

          {/* MEMBER DETAILS */}

          <Route
            path="/members/:memberId"
            element={
              <ProtectedRoute
                allowedRoles={["LIBRARIAN"]}
              >
                <MemberDetails />
              </ProtectedRoute>
            }
          />

          {/* ISSUES */}

          <Route
            path="/issues"
            element={
              <ProtectedRoute
                allowedRoles={["LIBRARIAN"]}
              >
                <IssueReturnPage />
              </ProtectedRoute>
            }
          />

          {/* MY PROFILE (MEMBER ONLY) */}
          <Route
            path="/my-profile"
            element={
              <ProtectedRoute allowedRoles={["MEMBER"]}>
                <MyProfile />
              </ProtectedRoute>
            }
          />

          {/* MY BOOKS (MEMBER ONLY) */}
          <Route
            path="/my-books"
            element={
              <ProtectedRoute allowedRoles={["MEMBER"]}>
                <MyBooks />
              </ProtectedRoute>
            }
          />

        </Routes>

      </div>
    </div>
  );
}

export default App;