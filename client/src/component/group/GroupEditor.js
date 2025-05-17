import "./GroupEditor.css";
import FriendSearchBar from "../friend/FriendSearchBar";
import AddedMemberList from "../add/AddedMemberList";
import AddFriendList from "../../component/friend/AddFriendList";

function GroupEditor({
  groupName,
  friends,
  addedMembers,
  setAddedMembers,
  onFriendSearch,
  onRemoveMember,
}) {
  return (
    <div className="groupEditorContainer">
      <h2 className="groupEditorTitle">{groupName} 그룹관리</h2>
      {/*친구 검색 컴포넌트*/}
      <FriendSearchBar friends={friends} onSearch={onFriendSearch} />
      <div className="groupEditorBody">
        {/*친구 현황 목록 및 초대 가능 컴포넌트*/}
        <AddFriendList friends={friends} />
        {/*그룹 멤버 현황 목록 및 초대 가능 컴포넌트*/}
        <AddedMemberList
          addedMembers={addedMembers}
          onRemoveMember={onRemoveMember}
        />
      </div>
    </div>
  );
}

export default GroupEditor;
