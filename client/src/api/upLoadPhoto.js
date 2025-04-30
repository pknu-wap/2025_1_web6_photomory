export async function uploadPhoto(formData) {
  try {
    //토큰 가져오기
    const token = localStorage.getItem("accessToken");

    console.log(token);
    const response = await fetch("http://3.38.237.115:8080/api/images/upload", {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData, // FormData 전송
    });

    if (!response.ok) {
      throw new Error("사진 업로드 실패");
    }

    const result = await response.json();
    console.log("서버 업로드 성공:", result);
    return result;
  } catch (error) {
    console.error("업로드 실패:", error.message);
    return null;
  }
}
