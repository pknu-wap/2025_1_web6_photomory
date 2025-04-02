import React, { useState, useEffect } from "react";
import Container from "./component/Container";
import Calender from "./component/Calender";
import AddAlbum from "./component/AddAlbum";
import CurrentGroup from "./component/CurrentGroup";
import Groups from "./component/Groups";
import getGroup from "../src/api/getGroup";

function App() {
  const [groupList, setGroupList] = useState([]); // 그룹명과 해당 그룹 멤버들의 리스트

  useEffect(() => {
    const groups = getGroup(); // 그룹 이름 + 멤버만
    setGroupList(groups);
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
              groupList={groupList}
            />
            {/*그룹 목록을 보여주는 컴포넌트*/}
            <Groups />
            <div
              style={{
                backgroundColor: "#f9f9f9",
                padding: "16px",
                borderRadius: "8px",
              }}
            >
              <h4>최근 앨범</h4>
              <ul>
                <li>가을 여행</li>
                <li>생일 파티</li>
                <li>2025 새해</li>
              </ul>
            </div>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default App;
