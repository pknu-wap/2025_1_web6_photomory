import FriendSearchBar from "./FriendSearchBar";
import AddedMemberList from "./AddedMemberList";
import AddFriendList from "./AddFriendList";

function GroupEditor({
  friends,
  addedMembers,
  setAddedMembers,
  onFriendSearch,
}) {
  return (
    <div>
      <FriendSearchBar friends={friends} onSearch={onFriendSearch} />
      <AddFriendList friends={friends} />
      <AddedMemberList addedMembers={addedMembers} />
    </div>
  );
}

export default GroupEditor;
