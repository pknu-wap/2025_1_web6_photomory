import React, { useState, useEffect } from "react";
import Container from "./component/Container";
import Calender from "./component/Calender";
import AddAlbum from "./component/AddAlbum";
import CurrentGroup from "./component/CurrentGroup";
import Groups from "./component/Groups";
import AlbumList from "./component/AlbumList";
import getGroup from "../src/api/getGroup";

function App() {
  const [groupList, setGroupList] = useState([]); // 그룹명과 해당 그룹 멤버들의 리스트
  const [selectedGroupId, setSelectedGroupId] = useState(""); // 선택된 그룹 ID를 App에서 관리

  useEffect(() => {
    const groups = getGroup(); // 그룹 데이터 불러오기
    setGroupList(groups);

    // 초기 렌더링 시 첫 번째 그룹 선택, setGroupList는 비동기함수 이므로 groups값 사용용
    if (groups.length > 0) {
      setSelectedGroupId(groups[0].id);
    }
  }, []);

  return (
    <div
      style={{
        margin: "0 auto",
        position: "relative",
        width: "1440px",
        height: "2942px",
        opacity: "1",
      }}
    >
      <nav
        style={{
          width: "100%",
          height: "80px",
          backgroundColor: "#f2f2f2",
          display: "flex",
          alignItems: "center",
          paddingLeft: "20px",
          marginBottom: "28px",
        }}
      >
        임시 메뉴 바
      </nav>

      <Container>
        <Calender />

        {/* 앨범 추가 오른쪽 영역을 가로 배치 */}
        <div style={{ display: "flex", gap: "24px", marginTop: "32px" }}>
          {/* 왼쪽: 앨범 추가 영역 */}
          <AddAlbum />

          {/* 오른쪽 영역 */}
          <div style={{ flex: 1 }}>
            {/* 현재 그룹을 나타내는 컴포넌트*/}
            <CurrentGroup
              style={{
                backgroundColor: "#f9f9f9",
                padding: "16px",
                borderRadius: "8px",
                marginBottom: "16px",
              }}
              groupList={groupList} // 모든 그룹 리스트
              setSelectedGroupId={setSelectedGroupId} //선택된 그룹 ID 세터
            />
            {/*그룹 목록을 보여주는 컴포넌트*/}
            <Groups
              groupList={groupList} // 모든 그룹 리스트
              selectedGroupId={selectedGroupId} // 그룹 강조에 사용
            />
            <div
              style={{
                backgroundColor: "#f9f9f9",
                padding: "16px",
                borderRadius: "8px",
              }}
            >
              <AlbumList />
            </div>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default App;
