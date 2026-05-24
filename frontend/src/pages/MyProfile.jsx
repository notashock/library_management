import { useEffect, useState } from "react";

import { getMyProfile } from "../services/memberService";

function MyProfile() {
  const [member, setMember] = useState(null);

  useEffect(() => {
    fetchProfile();
  }, []);

  const fetchProfile = async () => {
    try {
      const response =
        await getMyProfile();

      setMember(response.data);
    } catch (error) {
      console.log(error);
    }
  };

  if (!member) {
    return <h1>Loading...</h1>;
  }

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">
        My Profile
      </h1>

      <div className="bg-white shadow-md rounded-xl p-6">
        <h2 className="text-2xl font-semibold">
          {member.name}
        </h2>

        <p className="text-gray-600 mt-2">
          {member.email}
        </p>

        <p className="mt-2">
          Member ID: {member.memberId}
        </p>
      </div>
    </div>
  );
}

export default MyProfile;