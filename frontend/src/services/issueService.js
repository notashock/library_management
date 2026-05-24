import API from "./api";

export const getAllIssues = async () => {
  return await API.get("/issues");
};

export const issueBook = async (data) => {
  return await API.post("/issues/issue", data);
};

export const returnBook = async (issueId) => {
  return await API.put(`/issues/return/${issueId}`);
};

export const getIssuesByMember = async (
  memberId
) => {
  return await API.get(
    `/issues/member/${memberId}`
  );
};

export const getMyBooks = async () => {
  return await API.get("/issues/my-books");
};