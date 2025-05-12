async function signupUser(formData) {
  try {
    const response = await fetch(`${process.env.REACT_APP_API_URL}`, {
      method: "POST",
      body: formData, // 회원가입 정보 formData 그대로 보내기
    });

    if (!response.ok) {
      throw new Error("회원가입 실패!");
    }

    const result = await response.json();
    console.log("회원가입 성공:", result);
    return result;
  } catch (error) {
    console.error("회원가입 에러:", error.message);
    throw error;
  }
}

export default signupUser;
