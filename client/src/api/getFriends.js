import profile_data from "./profile_data.json";
//해당 유저의 친구 목록 반환
export function getFriends() {
  return profile_data.friends;
}
