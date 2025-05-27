import React, { useEffect, useMemo, useState, useCallback } from "react";
import styles from "./Profile.Main.module.css";
import FriendManage from "../friend/Friend.Manage";
import SearchFriend from "../friend/Search.Friend";
import logout from '../../assets/logout.svg'
import defaultProfile from "../../assets/defaultProfileIcon.svg";

const getUserList = async (retries=0, maxRetries=3) => {
    const refreshToken= localStorage.getItem('refreshToken')
    let accessToken=localStorage.getItem('accessToken');
  try {
    // 전체 사용자 목록 가져오기
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/friend-requests/non-friends/search`,{
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`, //이제 유저에 따라 친구 검색이 달라지니 엑세스 토큰을 추가.
      }
    });
    if (!response.ok) {
      throw new Error('사용자 데이터를 가져오는 대에 실패했습니다.');
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
    if(error.message==='Unauthorized' && refreshToken && retries<maxRetries){
      accessToken= await refreshAccessToken(refreshToken)
      if(accessToken){
        const response=getUserList(retries+1, maxRetries)
        return response
      }
    }
  }
};

const getMyInfo= async (retries=0, maxRetries=3)=>{ //내 정보 가져오기
    const refreshToken= localStorage.getItem('refreshToken')
    let accessToken=localStorage.getItem('accessToken');
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/user/profile`,{
      method: 'GET',
      headers:{
        'Content-Type': 'application/json',
        'Authorization': `Bearer${accessToken}`
      }
    })
      if(!response.ok){
        if(response.status===401){
          throw new Error('Unauthorized')
        }
        throw new Error('Failed to get myInfo:' `${response.status}`)
      }
    const myInfo= await response.json();
    return myInfo
  }
  catch(error){
    if(error.message==='Unauthorized' && refreshToken && retries< maxRetries){
      accessToken= await refreshAccessToken(refreshToken)
      if(accessToken){
        const response= await getMyInfo(retries+1, maxRetries)
        return response
      }
    }
  }
}

const postMyinfo= async (myInfo, retries=0, maxRetries=3)=>{ 
  let accessToken= localStorage.getItem('accessToken');
  const refreshToken= localStorage.getItem('refreshToken');
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api아직서버에 없음`,{
      method:'POST',
      headers:{
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      },
      body: JSON.stringify(myInfo)
    })
    if(!response.ok){
      if(response.status===401){
        throw new Error('Unauthorized')
      }
      throw new Error('Failed to post MyInfo:' `${response.status}`)
    }
    return response.json();
  }
  catch(error){
    if (error.message === 'Unauthorized' && refreshToken && retries<maxRetries) { //리프토큰 없으면 요청 안 되게게
      accessToken=await refreshAccessToken(refreshToken);
      if (accessToken) {
        const response = await postMyinfo(myInfo, retries+1, maxRetries);
        return response
      }
    }
    console.log('Failed to post MyInfo')
    return null
  }
}

const editFriend= async(userId, retries=0, maxRetries=3)=>{ //친삭
  let accessToken=localStorage.getItem('accessToken')
  const refreshToken=localStorage.getItem('refreshToken')
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/friend-list/${userId}`,{
      method: 'DELETE',
      headers:{
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
    if(!response.ok){
      if(response.status===401){
        throw new Error('Unauthorized')
      }
      throw new Error('Failed to post MyInfo:' `${response.status}`)
    }
    return {success: true};
  
  }catch(error){
    if (error.message === 'Unauthorized' && refreshToken && retries<maxRetries) { //리프토큰 없으면 요청 안 되게게
      accessToken=await refreshAccessToken(refreshToken);
      if (accessToken) {
        const response = await editFriend(userId, retries+1, maxRetries);
        return response
      }
    }
    console.log('Failed to post MyInfo')
    return null
  }
}

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
      if (data.accessToken) {
        localStorage.setItem('accessToken', data.accessToken);
      }
      return data.accessToken;
    }
    catch (error) {
        console.error('Error fetching token:', error);
        return null;
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
  const [filteredUsers, setFilteredUsers] = useState([])
  const [isEdit, setIsEdit]= useState(false)

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
  const handleRemoveFriend = useCallback(async (userId) => {
    if (!userId) return;

    setUsers(prevUsers => //낙관적 
      prevUsers.map(user => 
        user.id === userId 
          ? { ...user, isFriend: false }
          : user
        )
      );
    try{
      const response= await editFriend(userId) //서버
      if(!response || !response.success){
        throw new Error('친구 삭제에 실패했습니다.')
      }
    } catch(error){
      console.error('')
      setUsers((prevUsers)=> //낙관적 업뎃 롤백
        prevUsers.map((user) => 
          user.id === userId 
            ? { ...user, isFriend: true }
            : user
        )
      )
    }
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
    const fetchProfileAndUsers = async () => {
      try {
        // 내 프로필 데이터 가져오기
        const myData = await getMyInfo();
        if (myData) {
          setProfileData({
            id: myData.id || "",
            name: myData.name || "",
            job: myData.job || "",
            field: myData.field || "",
            equipment: myData.equipment || "",
            area: myData.area || "",
            introduction: myData.introduction || "",
            profileImage: myData.user_photourl || defaultProfile
          });
        }

        // 전체 사용자 목록 가져오기
        const userData = await getUserList();
        if (Array.isArray(userData)) {
          setUsers(userData);
        } else {
          throw new Error('잘못된 사용자 데이터 형식입니다.');
        }
      } catch (error) {
        console.error(error);
      }
    };
    fetchProfileAndUsers();
  }, []);

  useEffect(()=>{
    if(search){
      const filtered=users.filter((user)=>
        user.userName.toLowerCase().includes(search.toLowerCase())
      )
      setFilteredUsers(filtered)
    } else{
      setFilteredUsers(users)
    }
  }, [search, users])

  const numLimitedFilteredUsers= search? filteredUsers.slice(0,4) : users.slice(0,2)

  // 친구 목록 필터링
  const friends = useMemo(() => 
    users.filter(user => user?.isFriend) || []
  , [users]);

  const numLimitedFriends= friends.slice(0,4)

  const editHandle= async (e)=>{
    try{
      setIsEdit((prev)=>!prev)
        if(e.target.value==='save'){
          const afterProfileData= await postMyinfo(profileData)
          setProfileData(afterProfileData)
      }
    }
    catch(arror){
      console.error('failed to get myInfo')
    }
  }

  return (
    <div className={styles.allContainer}>
      <div className={styles.myInfoContainer}>
        <div className={styles.myDetailInfoContainer1}>
          <div className={styles.forFlexLeft}>
            <img 
              src={profileData?.profileImage || defaultProfile} 
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
          <div className={styles.forFlexSetting}>
            {isEdit? (
              <button className={styles.save}
              onClick={editHandle}
              value='save'
              >저장하기</button>
            ):(
              <button className={styles.edit}
              onClick={editHandle}
              >수정하기</button>
            )}
            <button 
              className={styles.logOutForFlexRight}
              onClick={handleLogout}>
              <img src={logout} alt="" className={styles.logoutIcon}></img>
              로그아웃
            </button>
          </div>
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
            {numLimitedFriends.length > 0 ? friends.map((user) => (
              <FriendManage
                key={user.id}
                userId={user.id}
                userName={user.name}
                userField={user.field}
                isFriend={user.isFriend}
                onRemoveFriend={handleRemoveFriend}
              />
            )) : (
              <p className={styles.zeroFriend}>앗, 친구가 없어요.😓</p>
            )}
          </div>
          <div className={styles.searchFriendContainer}>
            <p className={styles.searchMyFriendTop}>친구 검색</p> {/*아이콘 넣기 */}
            <input
              type="text"
              className={styles.searchBar}
              placeholder="친구 id를 입력하세요!"
              value={search}
              onChange={handleSearch}
            />
            {numLimitedFilteredUsers.map(user => (
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
