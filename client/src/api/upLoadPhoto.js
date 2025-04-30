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

    const data = await response.json();

    if (!response.ok) {
      console.error("❌ 업로드 실패 (백엔드 에러):", data);
      alert(`업로드 실패: ${data.message || "서버 오류"}`);
      return null;
    }

    console.log("서버 업로드 성공:", data);
    return data;
  } catch (error) {
    console.error("❌ 업로드 요청 자체 실패:", error.message);
    alert("서버와 통신하지 못했습니다.");
    return null;
  }
}
