import { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import Header from "../component/common/Header";
import GroupEditor from "../component/group/GroupEditor";
import Footer from "../component/common/Footer";
import { getInvitableFriends } from "../api/ourAlbumApi";
import { inviteFriendToGroup } from "../api/groupApi";
import { removeFriendFromGroup } from "../api/groupApi";
import { fetchGroupInfo } from "../api/ourAlbumApi";
import { normalizeMember } from "../utils/normalizers";

function GroupEditPage() {
  const { groupId } = useParams(); //GroupId 불러오기
  const navigate = useNavigate();

  const [friends, setFriends] = useState([]); //친구 목록 배열 원본
  const [filteredFriends, setFilteredFriends] = useState([]); // 검색 결과 상태 추가
  const [groupName, setGroupName] = useState(""); //그룹명
  const [addedMembers, setAddedMembers] = useState([]); //현재 그룹 멤버 상태

  useEffect(() => {
    (async () => {
      try {
        const myFriend = await getInvitableFriends(groupId);
        if (myFriend) {
          setFriends(myFriend);
          setFilteredFriends(myFriend);
        }

        const myGroup = await fetchGroupInfo(groupId);
        if (myGroup) {
          setGroupName(myGroup.groupName);
          //그룹 멤버 정보 정규화
          setAddedMembers(myGroup.members.map(normalizeMember));
        }
      } catch (error) {
        console.log("그룹 정보 불러오기 중 오류 발생:", error);
      }
    })();
  }, [groupId]); //groupId가 바뀔때 마다 다시 불러오기

  //친구 검색 핸들러
  const handleFriendSearch = (keyword) => {
    if (keyword.trim() === "") {
      // 검색어가 없으면 전체 friends 복원
      setFilteredFriends(friends);
    } else {
      // 검색어가 있으면 필터링
      const filtered = friends.filter(
        (friend) =>
          friend.username.toLowerCase().includes(keyword.toLowerCase()) //keyword를 하나라도 포함한다면 배열에 추가
      );
      setFilteredFriends(filtered);
    }
  };

  //친구 초대  헨들러
  const handleInviteClick = async (friend) => {
    try {
      const message = await inviteFriendToGroup(groupId, friend.userId);
      alert(`${friend.username} ${message}`); //초대 성공 알람
    } catch (error) {
      alert("초대에 실패했습니다. 다시 시도해주세요.");
    }
  };

  const myUsername = localStorage.getItem("userName");

  //그룹 인원 삭제 헨들러
  const handleRemoveMember = async (userId, username) => {
    const confirmed = window.confirm(
      `${username}님을 정말로 그룹에서 삭제하시겠습니까?`
    );
    if (!confirmed) return;

    try {
      const ok = await removeFriendFromGroup(groupId, userId);
      if (ok) {
        if (username === myUsername) {
          alert("당신은 이 그룹에서 나갔습니다.");
          navigate("/our-album"); // 그룹 목록이나 홈 화면으로 이동
        } else {
          alert(`${username}님이 그룹에서 삭제되었습니다`);
          setAddedMembers((prev) => prev.filter((m) => m.user_id !== userId));
        }
      }
    } catch (e) {
      alert("삭제 실패");
    }
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
        handleInviteClick={handleInviteClick} //친구 초대 헨들러
        onRemoveMember={handleRemoveMember} //삭제 헨들러
      />
      <Footer />
    </div>
  );
}

export default GroupEditPage;
