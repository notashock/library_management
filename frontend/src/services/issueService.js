import API from "./api";

// ISSUE BOOK

export const issueBook = async (
  data
) => {
  const response = await API.post(
    "/issues/issue",
    data
  );

  return response.data;
};

// RETURN BOOK

export const returnBook = async (
  issueId
) => {
  const response = await API.put(
    `/issues/return/${issueId}`
  );

  return response.data;
};

// MEMBER ISSUES

export const getIssuesByMember =
  async (memberId) => {
    const response = await API.get(
      `/members/${memberId}/issues`
    );

    return response.data;
  };