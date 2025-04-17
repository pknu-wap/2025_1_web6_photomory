import FriendSearchBar from "./FriendSearchBar";
import AddedMemberList from "./AddedMemberList";
import AddFriendList from "./AddFriendList";

function GroupEditor({
  friends, //현재 유저의 친구들
  addedMembers,
  setAddedMembers,
  onFriendSearch, //친구 검색 헨들러
  onRemoveMember, //현재 그룹 인원 삭제 헨들러
}) {
  return (
    <div>
      <FriendSearchBar friends={friends} onSearch={onFriendSearch} />
      <AddFriendList friends={friends} />
      <AddedMemberList
        addedMembers={addedMembers}
        onRemoveMember={onRemoveMember}
      />
    </div>
  );
}

export default GroupEditor;
