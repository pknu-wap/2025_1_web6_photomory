async function signupUser(data) {
  try {
    const response = await fetch("http://3.38.237.115:8080/api/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data), // JSON으로 문자열 변환
    });

    if (!response.ok) {
      throw new Error("회원가입 실패!");
    }
    //회원가입 확인 메시지 받기기
    const result = await response.json();

    return result;
  } catch (error) {
    throw error;
  }
}

export default signupUser;
