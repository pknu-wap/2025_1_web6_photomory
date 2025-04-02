import groupAlbums from "./groupAlbums.json";

// 그룹 이름과 멤버만 추출해서 반환
export default function getGroup(groupName) {
  const groupList = groupAlbums.map(({ id, groupName, members }) => ({
    id,
    groupName,
    members,
  }));

  return groupList; // 전체 그룹
}
