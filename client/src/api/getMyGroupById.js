import groupAlbumData from "./final_group_album_data.json"; // 공유 앨범 데이터 불러오기
//그룹ID를 통한 그룹명, 그룹멤버 가져오기
export function getMyGroupById(groupId) {
  const group = groupAlbumData.find((g) => g.group_id === groupId);

  if (!group) {
    console.error("해당 ID의 그룹을 찾을 수 없습니다:", groupId);
    return null;
  }

  // 여기서 이름과 멤버만 리턴
  return {
    groupName: group.group_name,
    members: group.members,
  };
}
