import Users from "./User.json"

export default function GetUser() {
    const UserList = Users.map(({ user_id, user_name, user_field, user_friendList, is_friend }) => ({
        id: user_id,
        name: user_name,
        field: user_field,
        friends: user_friendList,
        isFriend: is_friend
    }));
    
    return UserList;
}