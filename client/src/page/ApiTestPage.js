import { useState } from "react";
import { fetchGroupAlbumDetail } from "../api/ourAlbumApi"; // API 경로 확인

function ApiTestPage() {
  const [loading, setLoading] = useState(false);
  const [result, setResult] = useState(null);

  // 앨범 상세 조회 버튼 클릭 핸들러
  const handleFetchAlbumDetail = async () => {
    const albumId = 1; // ✅ 테스트할 앨범 ID
    const page = 0;
    const size = 4;

    setLoading(true);
    try {
      const response = await fetchGroupAlbumDetail(albumId, page, size);
      console.log("✅ 앨범 상세 조회 성공:", response);
      setResult(response);
    } catch (error) {
      console.error("❌ 앨범 상세 조회 실패:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{ padding: "40px" }}>
      <h2>🧪 앨범 상세 조회 테스트</h2>
      <button onClick={handleFetchAlbumDetail} disabled={loading}>
        {loading ? "불러오는 중..." : "앨범 상세 불러오기"}
      </button>

      {result && (
        <pre
          style={{ marginTop: "20px", background: "#f4f4f4", padding: "12px" }}
        >
          {JSON.stringify(result, null, 2)}
        </pre>
      )}
    </div>
  );
}

export default ApiTestPage;
