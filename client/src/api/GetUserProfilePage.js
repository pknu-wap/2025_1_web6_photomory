import UserProfilePage from "./UserProfilePage.json";

export default function GetUserProfilePage() {
  const UserList = UserProfilePage.map(
    ({
      user_id,
      user_name,
      user_field,
      user_friends,
      user_photourl,
      user_introduction,
      user_password,
      user_email,
      user_job,
      user_equipment,
      is_friend,
    }) => ({
      id: user_id,
      name: user_name,
      field: user_field,
      friends: user_friends,
      photoUrl: user_photourl,
      introduction: user_introduction,
      password: user_password,
      email: user_email,
      job: user_job,
      equipment: user_equipment,
      isFriend: is_friend,
    })
  );

  return UserList;
}
