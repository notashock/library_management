import API from "./api";

// MEMBER DETAILS

export const getMemberById =
  async (memberId) => {
    const response = await API.get(
      `/members/${memberId}`
    );

    return response.data;
  };