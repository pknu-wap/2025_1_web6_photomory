import final_group_album_data from "./final_group_album_data";
//선택된 그룹의 앨범, 각각의 사진들 반환
export default function getGroupAlbumsTest(group_id) {
  if (!group_id) return [];
  const group = final_group_album_data.find((g) => g.group_id === group_id);
  return group ? group.albums : [];
}
