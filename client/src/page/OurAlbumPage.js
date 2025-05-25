import React, { useState, useEffect } from "react";
import Header from "../component/common/Header";
import Footer from "../component/common/Footer";
import Container from "../component/common/Container";
import Calender from "../component/calender/Calender";
import AddAlbum from "../component/add/AddAlbum";
import CurrentGroup from "../component/group/CurrentGroup";
import Groups from "../component/group/Groups";
import AlbumList from "../component/album/AlbumList";

import { getOurAlbumData } from "../api/ourAlbumApi"; //서버 연동 준비용
import { normalizeOurAlbumData } from "../utils/normalizers";

function OurAlbumPage() {
  const [groupList, setGroupList] = useState([]); // 그룹명과 해당 그룹 멤버들의 리스트
  const [selectedGroupId, setSelectedGroupId] = useState(""); // 선택된 그룹 ID를 App에서 관리
  const [groupAlbums, setGroupAlbums] = useState([]); // 그룹별 앨범 리스트
  const [albumTitlesByGroup, setAlbumTitlesByGroup] = useState({}); //그룹ID에 대한 앨범 목록 객체

  //초기 그룹 정보, 앨범 가져오기
  useEffect(() => {
    (async () => {
      try {
        const rawData = await getOurAlbumData();
        const normalizedData = normalizeOurAlbumData(rawData);

        // 그룹 정보만 추출
        const minimalGroupList = normalizedData.map(
          ({ group_id, group_name, members }) => ({
            group_id,
            group_name,
            members,
          })
        );

        setGroupList(minimalGroupList);

        if (normalizedData.length > 0) {
          const firstGroup = normalizedData[0];
          setSelectedGroupId(firstGroup.group_id);
          setGroupAlbums(firstGroup.albums);

          const titlesByGroup = {};
          for (const group of normalizedData) {
            titlesByGroup[group.group_id] = group.albums.map(
              (album) => album.album_name
            );
          }
          setAlbumTitlesByGroup(titlesByGroup);
        }
      } catch (error) {
        console.error("서버 데이터 로드 실패:", error);
      }
    })();
  }, []);

  useEffect(() => {
    if (selectedGroupId && groupList.length > 0) {
      const group = groupList.find((g) => g.groupId === selectedGroupId);
      if (group && group.albums) {
        setGroupAlbums(group.albums);
      }
    }
  }, [selectedGroupId, groupList]);

  // 새로운 그룹 추가 핸들러 (AddGroupButton에서 사용할 예정)
  const handleAddGroup = (newGroup) => {
    setGroupList((prev) => [...prev, newGroup]);
  };

  return (
    <>
      <Header />
      <Container
        style={{
          margin: "28px auto",
          padding: "0 40px",
          position: "relative",
          height: "2942px",
          opacity: "1",
        }}
      >
        <Calender
          type="group"
          groupAlbums={groupAlbums}
          selectedGroupId={selectedGroupId}
          albumTitlesByGroup={albumTitlesByGroup}
        />

        {/* 앨범 추가 오른쪽 영역을 가로 배치 */}
        <div style={{ display: "flex", gap: "24px", marginTop: "32px" }}>
          {/* 왼쪽: 앨범 추가 영역 */}
          <AddAlbum
            type="group"
            selectedGroupId={selectedGroupId} //해당그룹의 ID
            albumTitlesByGroup={albumTitlesByGroup} // 앨범 제목 목록 객체
            setAlbumTitlesByGroup={setAlbumTitlesByGroup} // 앨범 제목 목록 객체 세터 함수
            setGroupAlbums={setGroupAlbums} //현재 앨범 목록 변경용
          />

          {/* 오른쪽 영역 */}
          <div style={{ flex: 1 }}>
            {/* 현재 그룹을 나타내는 컴포넌트*/}
            <CurrentGroup
              groupList={groupList} // 모든 그룹 리스트
              setSelectedGroupId={setSelectedGroupId} //선택된 그룹 ID 세터
              onAddGroup={handleAddGroup}
            />
            {/*그룹 목록을 보여주는 컴포넌트*/}
            <Groups
              groupList={groupList} // 모든 그룹 리스트
              selectedGroupId={selectedGroupId} // 그룹 강조에 사용
            />
            <div>
              {/*그룹별 앨범 목록을 보여주는 컴포넌트*/}
              <AlbumList
                albums={groupAlbums}
                type="group"
                selectedGroupId={selectedGroupId}
                basePath="/our-album"
              />
            </div>
          </div>
        </div>
      </Container>
      <Footer />
    </>
  );
}

export default OurAlbumPage;
