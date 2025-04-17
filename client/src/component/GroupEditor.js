import FriendSearchBar from "./FriendSearchBar";
import AddedMemberList from "./AddedMemberList";
import AddFriendList from "./AddFriendList";

function GroupEditor({
  groupName,
  friends, //현재 유저의 친구들
  addedMembers,
  setAddedMembers,
  onFriendSearch, //친구 검색 헨들러
  onRemoveMember, //현재 그룹 인원 삭제 헨들러
}) {
  return (
    <div
      style={{
        width: "1088px",
        height: "1050px",
        padding: "32px",
        borderRadius: "8px",
        background: "#ffffff",
        margin: "0px auto",
        boxShadow:
          "0px 4px 6px -4px rgba(0, 0, 0, 0.1),0px 10px 15px -3px rgba(0, 0, 0, 0.1)",
      }}
    >
      <h2
        style={{
          marginBottom: "24px",
          fontSize: "24px",
          fontWeight: "bold",
          lineHeight: "32px",
        }}
      >
        {groupName} 그룹관리
      </h2>
      <FriendSearchBar friends={friends} onSearch={onFriendSearch} />
      <div
        style={{
          display: "flex",
          justifyContent: "space-between",
        }}
      >
        <AddFriendList friends={friends} />
        <AddedMemberList
          addedMembers={addedMembers}
          onRemoveMember={onRemoveMember}
        />
      </div>
    </div>
  );
}

export default GroupEditor;
