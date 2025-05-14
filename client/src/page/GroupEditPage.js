import { useState, useEffect } from "react";
import { getMyGroupById } from "../api/getMyGroupById";
import { getFriends } from "../api/getFriends";
import GroupEditor from "../component/group/GroupEditor";
import { useParams } from "react-router-dom";
import Header from "../component/Header";
import Footer from "../component/common/Footer";

function GroupEditPage() {
  const { groupId } = useParams(); //GroupId 불러오기
  const [friends, setFriends] = useState([]); //친구 목록 배열 원본
  const [filteredFriends, setFilteredFriends] = useState([]); // 검색 결과 상태 추가
  const [groupName, setGroupName] = useState(""); //그룹명
  const [addedMembers, setAddedMembers] = useState([]); //현재 그룹 멤버 상태

  useEffect(() => {
    const myFriends = getFriends();
    if (myFriends) {
      setFriends(myFriends);
      setFilteredFriends(myFriends); // 초기에는 전체 친구를 보여줌
    }

    const myGroup = getMyGroupById(Number(groupId)); // 현재 선택된 그룹 Id로 맴버 불러오기
    if (myGroup) {
      setGroupName(myGroup.groupName);
      setAddedMembers(myGroup.members);
    }
  }, [groupId]); //groupId가 바뀔때 마다 다시 불러오기

  //친구 검색 핸들러
  const handleFriendSearch = (keyword) => {
    if (keyword.trim() === "") {
      // 검색어가 없으면 전체 friends 복원
      setFilteredFriends(friends);
    } else {
      // 검색어가 있으면 필터링
      const filtered = friends.filter(
        (friend) => friend.name.toLowerCase().includes(keyword.toLowerCase()) //keyword를 하나라도 포함한다면 배열에 추가
      );
      setFilteredFriends(filtered);
    }
  };
  //현재 그룹 인원 삭제 핸들러
  const handleRemoveMember = (userId) => {
    setAddedMembers((prev) => prev.filter((m) => m.user_id !== userId));
  };

  return (
    <div style={{ height: "auto" }}>
      <Header />
      <GroupEditor
        groupName={groupName} //그룹명
        friends={filteredFriends} // 검색된 친구 목록만 넘겨줌
        addedMembers={addedMembers}
        setAddedMembers={setAddedMembers}
        onFriendSearch={handleFriendSearch} // 검색 핸들러
        onRemoveMember={handleRemoveMember} //삭제 헨들러
      />
      <Footer />
    </div>
  );
}

export default GroupEditPage;
