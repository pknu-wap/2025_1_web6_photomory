import final_group_album_data from "./final_group_album_data";

// 그룹 이름과 멤버만 추출해서 반환
export default function getGroup() {
  const groupList = final_group_album_data.map(
    ({ group_id, group_name, members }) => ({
      group_id,
      group_name,
      members,
    })
  );

  return groupList; // 전체 그룹
}
