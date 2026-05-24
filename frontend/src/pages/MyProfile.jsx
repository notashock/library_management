import { useEffect, useState } from "react";
import { getMemberById } from "../services/memberService";
import { useAuth } from "../context/AuthContext";
import { toast } from "react-toastify";

function MyProfile() {
  const { user } = useAuth();
  const [member, setMember] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user?.memberId) {
      fetchMemberDetails();
    }
  }, [user]);

  const fetchMemberDetails = async () => {
    try {
      const memberRes = await getMemberById(user.memberId);
      setMember(memberRes.data);
      // console.log("Fetched member details:", memberRes);
    } catch (error) {
      console.error("Failed to fetch member data:", error);
      toast.error("Failed to load profile details.");
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <h1 className="text-center mt-10 text-2xl">Loading...</h1>;
  }

  if (!member) {
    return <h1 className="text-center mt-10 text-2xl text-red-500">Profile not found</h1>;
  }

  return (
    <div className="max-w-4xl mx-auto p-4">
      <h1 className="text-3xl font-bold mb-6">
        My Profile
      </h1>

      <div className="bg-white shadow-md rounded-xl p-6 mb-8">
        <h2 className="text-2xl font-semibold">
          {member.name}
        </h2>
        <p className="text-gray-600 mt-2">
          Email: {member.email}
        </p>
        <p className="mt-2 text-sm text-gray-500">
          Member ID: {member.memberId}
        </p>
        <p className="mt-2 text-sm text-gray-500">
          Role: <span className="font-semibold text-blue-600">{member.role}</span>
        </p>
      </div>
    </div>
  );
}

export default MyProfile;
