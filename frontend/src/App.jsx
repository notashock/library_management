import { Routes, Route } from "react-router-dom";

import Navbar from "./components/Navbar";

import Dashboard from "./pages/Dashboard";
import BooksPage from "./pages/BooksPage";
import MembersPage from "./pages/MembersPage";
import IssueReturnPage from "./pages/IssueReturnPage";
import Login from "./pages/Login";
import Unauthorized from "./pages/Unauthorized";

import ProtectedRoute from "./components/ProtectedRoute";
import MemberDetails from "./pages/MemberDetails";
import MyProfile from "./pages/MyProfile";
import MyBooks from "./pages/MyBooks";
import Register from "./pages/Register";

function App() {
  return (
    <div>
      <Navbar />

      <div className="p-6">
        <Routes>
          {/* Public Route */}
           <Route path='/'  element={<Login />} />
          <Route path="/login" element={<Login />} />
          <Route
  path="/register"
  element={<Register />}
/>

          <Route
            path="/unauthorized"
            element={<Unauthorized />}
          />

          {/* Librarian Routes */}

          <Route
            path="/"
            element={
              <ProtectedRoute
                allowedRoles={["LIBRARIAN"]}
              >
                <Dashboard />
              </ProtectedRoute>
            }
          />

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

<Route
  path="/my-profile"
  element={
    <ProtectedRoute
      allowedRoles={["MEMBER"]}
    >
      <MyProfile />
    </ProtectedRoute>
  }
/>

<Route
  path="/my-books"
  element={
    <ProtectedRoute
      allowedRoles={["MEMBER"]}
    >
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