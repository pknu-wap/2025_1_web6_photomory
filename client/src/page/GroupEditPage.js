import { useState, useEffect } from "react";
import { getMyGroupById } from "../api/getMyGroupById";
import { getFriends } from "../api/getFriends";
import GroupEditor from "../component/GroupEditor";

function GroupEditPage() {
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

    const myGroup = getMyGroupById(1); // 임시로 groupId 1로 고정
    if (myGroup) {
      setGroupName(myGroup.groupName);
      setAddedMembers(myGroup.members);
    }
  }, []);

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
    <div style={{ height: "1489px" }}>
      <GroupEditor
        groupName={groupName} //그룹명
        friends={filteredFriends} // 검색된 친구 목록만 넘겨줌
        addedMembers={addedMembers}
        setAddedMembers={setAddedMembers}
        onFriendSearch={handleFriendSearch} // 검색 핸들러
        onRemoveMember={handleRemoveMember} //삭제 헨들러
      />
    </div>
  );
}

export default GroupEditPage;
