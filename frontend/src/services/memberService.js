import API from "./api";

export const getAllMembers = async () => {
  return await API.get("/members");
};

export const addMember = async (memberData) => {
  return await API.post("/members", memberData);
};

export const getMemberById = async (id) => {
  return await API.get(`/members/${id}`);
};

export const getMyProfile = async () => {
  return await API.get("/members/profile");
};