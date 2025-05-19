import "./AddedMemberList.css";

const MAX_MEMBERS = 20; // 최대 인원 20명

//현재 그룹 멤버 컴포넌트
function AddedMemberList({ addedMembers, onRemoveMember }) {
  //삭제 알림 헨들러
  const handleRemoveClick = (member) => {
    if (window.confirm(`${member.user_name}님을 삭제하시겠습니까?`)) {
      onRemoveMember(member.user_id);
    }
  };

  return (
    <div className="addedMemberContainer">
      <h3 className="addedMemberTitle">현재 그룹 멤버</h3>

      <p className="addedMemberCount">
        {addedMembers.length} / {MAX_MEMBERS}명
      </p>

      <ul className="addedMemberList">
        {addedMembers.map((member) => (
          <li key={member.user_id} className="addedMemberItem">
            <img
              src={member.user_photourl}
              alt={member.user_name}
              className="addedMemberImg"
            />
            <span className="addedMemberName">{member.user_name}</span>
            <button
              onClick={() => handleRemoveClick(member)}
              className="removeButton"
            >
              삭제
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default AddedMemberList;
