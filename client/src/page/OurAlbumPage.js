import React, { useState, useEffect } from "react";
import Container from "../component/Container";
import Calender from "../component/Calender";
import AddAlbum from "../component/AddAlbum";
import CurrentGroup from "../component/CurrentGroup";
import Groups from "../component/Groups";
import AlbumList from "../component/AlbumList";
import getGroup from "../api/getGroup";
import getGroupAlbums from "../api/getGroupAlbums";

function OurAlbumPage() {
  const [groupList, setGroupList] = useState([]); // 그룹명과 해당 그룹 멤버들의 리스트
  const [selectedGroupId, setSelectedGroupId] = useState(""); // 선택된 그룹 ID를 App에서 관리
  const [groupAlbums, setGroupAlbums] = useState([]); // 그룹별 앨범 리스트
  const [albumTitlesByGroup, setAlbumTitlesByGroup] = useState({}); //그룹ID에 대한 앨범 목록 객체
  //초기 그룹 정보, 앨범 가져오기
  useEffect(() => {
    const groups = getGroup(); // 그룹 데이터 불러오기
    setGroupList(groups);

    if (groups.length > 0) {
      const firstGroup = groups[0]; //항상 첫번째 그룹 선택
      const firstGroupAlbums = getGroupAlbums(firstGroup.id); //항상 첫번째 그룹의 앨범 선택

      setSelectedGroupId(firstGroup.id); // 선택된 그룹 ID
      setGroupAlbums(firstGroupAlbums); // 선택된 그룹의 앨범 전체 데이터
      const initTitlesByGroup = {};
      groups.forEach((group) => {
        //처음부터 모든 그룹의 앨범 제목 목록을 한 번에 저장
        const albums = getGroupAlbums(group.id);
        initTitlesByGroup[group.id] = albums.map((album) => album.title);
      });
      setAlbumTitlesByGroup(initTitlesByGroup);
    }
  }, []);

  //그룹id가 바뀔 대마다 그룹 앨범 가져오기
  useEffect(() => {
    if (selectedGroupId) {
      const albums = getGroupAlbums(selectedGroupId); // 그룹 ID로 앨범 가져오기
      setGroupAlbums(albums); // 상태에 저장
    }
  }, [selectedGroupId]);

  // 새로운 그룹 추가 핸들러 (AddGroupButton에서 사용할 예정)
  const handleAddGroup = (newGroup) => {
    setGroupList((prev) => [...prev, newGroup]);
  };

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
        <Calender
          groupAlbums={groupAlbums}
          selectedGroupId={selectedGroupId}
          albumTitlesByGroup={albumTitlesByGroup}
        />

        {/* 앨범 추가 오른쪽 영역을 가로 배치 */}
        <div style={{ display: "flex", gap: "24px", marginTop: "32px" }}>
          {/* 왼쪽: 앨범 추가 영역 */}
          <AddAlbum
            selectedGroupId={selectedGroupId} //해당그룹의 ID
            albumTitlesByGroup={albumTitlesByGroup} // 앨범 제목 목록 객체
            setAlbumTitlesByGroup={setAlbumTitlesByGroup} // 앨범 제목 목록 객체 세터 함수
            setGroupAlbums={setGroupAlbums} //현재 앨범 목록 변경용
          />

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
              onAddGroup={handleAddGroup}
            />
            {/*그룹 목록을 보여주는 컴포넌트*/}
            <Groups
              groupList={groupList} // 모든 그룹 리스트
              selectedGroupId={selectedGroupId} // 그룹 강조에 사용
            />
            <div
              style={{
                padding: "16px",
                borderRadius: "8px",
              }}
            >
              {/*그룹별 앨범 목록을 보여주는 컴포넌트*/}
              <AlbumList albums={groupAlbums} />
            </div>
          </div>
        </div>
      </Container>
    </div>
  );
}

export default OurAlbumPage;
