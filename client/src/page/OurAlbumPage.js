import React, { useState, useEffect } from "react";
import Header from "../component/common/Header";
import Footer from "../component/common/Footer";
import Container from "../component/common/Container";
import Calender from "../component/calender/Calender";
import AddAlbum from "../component/add/AddAlbum";
import AllAlbumTags from "../component/tag/AllAlbumTags";
import CurrentGroup from "../component/group/CurrentGroup";
import Groups from "../component/group/Groups";
import AlbumList from "../component/album/AlbumList";
import { getOurAlbumData } from "../api/ourAlbumApi";
import { normalizeOurAlbumData } from "../utils/normalizers";

function OurAlbumPage() {
  const [groupList, setGroupList] = useState([]); // 그룹명과 해당 그룹 멤버들의 리스트
  const [selectedGroupId, setSelectedGroupId] = useState(""); // 선택된 그룹 ID를 App에서 관리
  const [groupAlbums, setGroupAlbums] = useState([]); // 그룹별 앨범 리스트
  const [albumTitlesByGroup, setAlbumTitlesByGroup] = useState({}); //그룹ID에 대한 앨범 목록 객체
  const [albumsByGroupId, setAlbumsByGroupId] = useState({}); //그룹 id별 앨범 데이터
  const [selectedTags, setSelectedTags] = useState([]); //선택된 태그 배열 상태
  const [currentTags, setCurrentTags] = useState([]); //현재 그룹의 태그 배열 상태

  //태그 선택 헨들러
  const handleTagClick = (tag) => {
    setSelectedTags((prev) =>
      //기존 태그 선택 취소, 태그 선택
      prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag]
    );
  };

  //태그 추가 헨들러
  const handleAddTagClick = (tags) => {
    setCurrentTags((prev) => Array.from(new Set([...prev, ...tags])));
  };

  // 새로운 그룹 추가 핸들러 (AddGroupButton에서 사용할 예정)
  const handleAddGroup = (newGroupRaw) => {
    const myName = localStorage.getItem("userName") || "나";

    // groupList에 맞는 필드 구조로 정규화
    const newGroup = {
      group_id: newGroupRaw.groupId,
      group_name: newGroupRaw.groupName,
      members: [
        {
          user_id: "me",
          user_name: myName,
          user_photourl: null,
        },
      ],
    };
    //그룹 리스트 상태 최신화
    setGroupList((prev) => [...prev, newGroup]);
  };

  //선택된 태그에 따라 필터링된 앨범들
  const filteredGroupAlbums =
    selectedTags.length === 0
      ? groupAlbums
      : groupAlbums.filter((album) =>
          selectedTags.every((tag) => album.album_tag?.includes(tag))
        );

  //초기 그룹 정보, 앨범 가져오기
  useEffect(() => {
    (async () => {
      try {
        const rawData = await getOurAlbumData();
        const normalizedData = normalizeOurAlbumData(rawData); // 데이터 정규화

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

          const titlesByGroup = {}; //그룹별 앨범명
          const albumsMap = {}; //그룹별 앨범 데이터

          //그룹 id에 따른 앨범명, 앨범 정보 매핑
          for (const group of normalizedData) {
            titlesByGroup[group.group_id] = group.albums.map(
              (a) => a.album_name
            );
            albumsMap[group.group_id] = group.albums;
          }
          setAlbumTitlesByGroup(titlesByGroup);
          setAlbumsByGroupId(albumsMap);

          // 초기 태그 설정
          const tags = Array.from(
            new Set(firstGroup.albums.flatMap((album) => album.album_tag || []))
          );
          setCurrentTags(tags);
        }
      } catch (error) {
        console.error("서버 데이터 로드 실패:", error);
      }
    })();
  }, []);

  // 그룹 선택될 때 해당 그룹의 앨범을 albumsByGroupId에서 꺼냄, 태그 변경
  useEffect(() => {
    if (selectedGroupId && albumsByGroupId[selectedGroupId]) {
      const albums = albumsByGroupId[selectedGroupId];
      setGroupAlbums(albums);

      const tags = Array.from(
        new Set(albums.flatMap((album) => album.album_tag || []))
      );
      setCurrentTags(tags);
    } else {
      setGroupAlbums([]);
      setCurrentTags([]);
    }
  }, [selectedGroupId, albumsByGroupId]);

  // 전체 앨범 개수 구하기
  const allAlbumsCount = groupAlbums.length;

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
          {/* 왼쪽: 앨범 추가 + 태그 필터 */}
          <div
            style={{
              display: "flex",
              flexDirection: "column",
              width: "256px",
              gap: "24px",
            }}
          >
            <AddAlbum
              type="group"
              selectedGroupId={selectedGroupId}
              albumTitlesByGroup={albumTitlesByGroup}
              setAlbumTitlesByGroup={setAlbumTitlesByGroup}
              setGroupAlbums={setGroupAlbums}
              handleAddTagClick={handleAddTagClick}
            />
            <AllAlbumTags
              tags={currentTags}
              selectedTags={selectedTags}
              onTagClick={handleTagClick}
            />
          </div>

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
                albums={filteredGroupAlbums}
                type="group"
                selectedGroupId={selectedGroupId}
                basePath="/our-album"
                allAlbumsCount={allAlbumsCount}
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
