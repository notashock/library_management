import API from "./api";

// GET MEMBER DETAILS

export const getMemberById = async (
  memberId
) => {

  const response = await API.get(
    `/members/${memberId}`
  );

  return response.data;
};

// GET MEMBER ISSUES

export const getMemberIssues =
  async (memberId) => {

    const response = await API.get(
      `/members/${memberId}/issues`
    );

    return response.data;
};