import groupAlbums from "./groupAlbums.json";
//선택된 그룹의 앨범, 각각의 사진들 반환환
export default function getGroupAlbums(groupId) {
  if (!groupId) return [];
  const group = groupAlbums.find((g) => g.id === groupId);
  return group ? group.albums : [];
}
