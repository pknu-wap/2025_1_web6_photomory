import React, { useEffect, useMemo, useState, useCallback } from "react";
import styles from "./Profile.Main.module.css";
import FriendManage from "../friend/Friend.Manage";
import SearchFriend from "../friend/Search.Friend";
import GetMy from "../../api/GetMy";
import defaultProfile from "../../assets/defaultProfileIcon.svg";

const fetchUserData = async (accessToken) => {
  try {
    // 전체 사용자 목록 가져오기
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/users`,{ //아직 벡엔드에 없음
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`, //이제 유저에 따라 친구 검색이 달라지니 엑세스 토큰을 추가.
      }
    });
    if (!response.ok) {
      throw new Error('사용자 데이터를 가져오는데 실패했습니다.');
    }
    const userData = await response.json();
    if (Array.isArray(userData)) {
      // setUsers(userData);
      return userData;
    } else {
      throw new Error('잘못된 사용자 데이터 형식입니다.');
    }
  } 
  catch (error) {
    console.error("데이터 가져오기 오류:", error);
    throw error;
  }
};

async function refreshAccessToken(refreshToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/refresh-token`, { //이건 벡엔드에서 추후 변경 예정
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({refreshToken})
        });

        if (!response.ok) {
            throw new Error(`Token refredh failed status: ${response.status}`);
        }

        const data = await response.json();
        return data.accessToken;
    }
    catch (error) {
        console.error('Error fetching token:', error);
        return null;
    }
}

async function getUserData() {
    let accessToken= localStorage.getItem('accessToken');
    const refreshToken= localStorage.getItem('refreshToken');
    try{
        const userData = await fetchUserData(accessToken)
        return userData
    }
    catch (error){
        if (error.message === 'Unauthorized' && refreshToken) { //리프토큰 없으면 요청 안 되게게
            accessToken=await refreshAccessToken(refreshToken);
            if (accessToken) {
                localStorage.setItem('accessToken', accessToken);
                const userData = await fetchUserData(accessToken);
                return userData
            }
        }
        console.log('Failed to fetch user data')
        return null
    }
}

// 초기 프로필 데이터 상태
const initialProfileState = {
  id: "",
  name: "",
  job: "",
  field: "",
  equipment: "",
  area: "",
  introduction: "",
  profileImage: defaultProfile
};

// 입력 필드 매핑
const FIELD_MAPPING = {
  [styles.name]: "name",
  [styles.job]: "job",
  [styles.myFieldInput]: "field",
  [styles.myEquipmentInput]: "equipment",
  [styles.myAreaInput]: "area",
  [styles.introduction]: "introduction"
};

function ProfileMain() {
  const [profileData, setProfileData] = useState(initialProfileState);
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");

  // 입력 필드 변경 핸들러
  const handleInputChange = useCallback((e) => {
    const { className, value } = e.target;
    const field = FIELD_MAPPING[className];
    
    if (field) {
      setProfileData(prev => ({
        ...prev,
        [field]: value.trim()
      }));
    }
  }, []);

  // 친구 제거 핸들러
  const handleRemoveFriend = useCallback((userId) => {
    if (!userId) return;
    
    setUsers(prevUsers => 
      prevUsers.map(user => 
        user.id === userId 
          ? { ...user, isFriend: false }
          : user
      )
    );
  }, []);

  // 로그아웃 핸들러
  const handleLogout = useCallback(() => {
    try {
      localStorage.removeItem('token');
      window.location.href = '/login';
    } catch (error) {
      console.error('로그아웃 중 오류 발생:', error);
    }
  }, []);

  // 검색 핸들러
  const handleSearch = useCallback((e) => {
    setSearch(e.target.value);
  }, []);

  // 사용자 데이터 가져오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        // 내 프로필 데이터 가져오기
        const myData = await GetMy();
        if (myData) {
          setProfileData({
            id: myData.id || "",
            name: myData.name || "",
            job: myData.job || "",
            field: myData.field || "",
            equipment: myData.equipment || "",
            area: myData.area || "",
            introduction: myData.introduction || "",
            profileImage: myData.profileImage || defaultProfile
          });
        }

        // 전체 사용자 목록 가져오기
        const response = await getUserData();
        if (!response.ok) {
          throw new Error('사용자 데이터를 가져오는데 실패했습니다.');
        }
        
        const userData = await response.json();
        if (Array.isArray(userData)) {
          setUsers(userData);
        } else {
          throw new Error('잘못된 사용자 데이터 형식입니다.');
        }
      } catch (error) {
        console.error('로그아웃 중 오류 발생:', error);
      }
    };
    fetchUserData();
  }, []);

  // 검색된 사용자 필터링
  const filteredUsers = useMemo(() => {
    if (!search.trim()) return users;
    
    return users.filter(user => 
      user?.id?.toString().toLowerCase().includes(search.toLowerCase())
    );
  }, [users, search]);

  // 친구 목록 필터링
  const friends = useMemo(() => 
    users.filter(user => user?.isFriend) || []
  , [users]);

  return (
    <div className={styles.allContainer}>
      <div className={styles.myInfoContainer}>
        <div className={styles.myDetailInfoContainer1}>
          <div className={styles.forFlexLeft}>
            <img 
              src={profileData.profileImage} 
              alt="Profile" 
              className={styles.image}
            />
            <div className={styles.forFlex}>
              <input
                className={styles.name}
                onChange={handleInputChange}
                value={profileData.name}
                placeholder="이름을 알려줘!"
              />
              <input
                className={styles.job}
                onChange={handleInputChange}
                value={profileData.job}
                placeholder="직업을 알려줘!"
              />
              <div className={styles.id}>ID: {profileData.id}</div>
            </div>
          </div>
          <button 
            className={styles.logOutForFlexRight}
            onClick={handleLogout}
          >
            로그아웃
          </button>
        </div>

        <div className={styles.myDetailInfoContainer2}>
          <div className={styles.myFieldContainer}>
            <p className={styles.myField}>전문 분야</p>
            <input
              type="text"
              placeholder="풍경 사진"
              className={styles.myFieldInput}
              onChange={handleInputChange}
              value={profileData.field}
            />
          </div>
          <div className={styles.myEquipmentContainer}>
            <p className={styles.myEquipment}>사용 장비</p>
            <input
              type="text"
              placeholder="sony A7 IV"
              className={styles.myEquipmentInput}
              onChange={handleInputChange}
              value={profileData.equipment}
            />
          </div>
          <div className={styles.myAreaContainer}>
            <p className={styles.myArea}>활동 지역</p>
            <input
              type="text"
              placeholder="서울, 강원"
              className={styles.myAreaInput}
              onChange={handleInputChange}
              value={profileData.area}
            />
          </div>
        </div>

        <div className={styles.myDetailInfoContainer3}>
          <p className={styles.introTop}>소개</p>
          <textarea
            className={styles.introduction}
            placeholder="제가 누구냐면요.."
            onChange={handleInputChange}
            value={profileData.introduction}
          />
        </div>
      </div>

      <div className={styles.manageFriendContainer}> {/*애니 효과 추가하기*/}
        <p className={styles.manageFriendTop}>친구 관리</p>
        <div className={styles.forFlexFriend}>
          <div className={styles.myFriendsListContainer}>
            <p className={styles.myFriendListTop}>내 친구 목록</p>
            {friends.length > 0 ? friends.map((user) => (
              <FriendManage
                key={user.id}
                userId={user.id}
                userName={user.name}
                userField={user.field}
                isFriend={user.isFriend}
                onRemoveFriend={handleRemoveFriend}
              />
            )) : (
              <p className={styles.zeroFriend}>앗, 친구가 없어요😓</p>
            )}
          </div>
          <div className={styles.searchFriendContainer}>
            <p className={styles.searchMyFriendTop}>친구 검색</p>
            <input
              type="text"
              className={styles.searchBar}
              placeholder="친구 id를 입력하세요!"
              value={search}
              onChange={handleSearch}
            />
            {filteredUsers.map(user => (
              <SearchFriend 
                key={user.id} 
                userId={user.id} 
                userName={user.name} 
              />
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfileMain;
