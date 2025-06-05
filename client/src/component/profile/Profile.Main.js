import React, { useEffect, useState, useCallback, useRef } from "react";
import { useAuth } from "../../contexts/AuthContext";
import styles from "./Profile.Main.module.css";
import FriendManage from "../friend/Friend.Manage";
import SearchFriend from "../friend/Search.Friend";
import logout from '../../assets/logout.svg'
import defaultProfile from "../../assets/defaultProfileIcon.svg";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCamera } from "@fortawesome/free-solid-svg-icons";

const getNonFriendsList = async (retries=0, maxRetries=3) => {
    const refreshToken= localStorage.getItem('refreshToken')
    let accessToken=localStorage.getItem('accessToken');
  try {
    // 친구가 아닌 전체 사용자 목록 가져오기
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/friend-requests/non-friends/search`,{
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${accessToken}`, //이제 유저에 따라 친구 검색이 달라지니 엑세스 토큰을 추가.
      }
    });
    if (!response.ok) {
      throw new Error('친구가 아닌 사용자 데이터를 가져오는 대에 실패했습니다.');
    }
    const NonFriendsData = await response.json();
    if (Array.isArray(NonFriendsData)) {
      return NonFriendsData;
    } else {
      throw new Error('잘못된 친구가 아닌 사용자 데이터 형식입니다.');
    }
  } 
  catch (error) {
    if(error.message==='Unauthorized' && refreshToken && retries<maxRetries){
      accessToken= await refreshAccessToken(refreshToken)
      if(accessToken){
        const response=getNonFriendsList(retries+1, maxRetries)
        return response
      }
    }
    console.error('Failed to get non-Friend List')
    return null
  }
};

const getFriendsList = async (retries=0, maxRetries=3) => { //안 나옴
    const refreshToken= localStorage.getItem('refreshToken')
    let accessToken=localStorage.getItem('accessToken');
  try {
    // 친구인 전체 사용자 목록 가져오기
    const response = await fetch(`${process.env.REACT_APP_API_URL}/api/friend-list`,{
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${accessToken}`, //이제 유저에 따라 친구 검색이 달라지니 엑세스 토큰을 추가.
      }
    });
    if (!response.ok) {
      throw new Error('친구 사용자 데이터를 가져오는 대에 실패했습니다.');
    }
    const friendData = await response.json();
    if (Array.isArray(friendData)) {
      return friendData;
    } else {
      throw new Error('잘못된 친구 사용자 데이터 형식입니다.');
    }
  } 
  catch (error) {
    if(error.message==='Unauthorized' && refreshToken && retries<maxRetries){
      accessToken= await refreshAccessToken(refreshToken)
      if(accessToken){
        const response=getFriendsList(retries+1, maxRetries)
        return response
      }
    }
    console.error('Failed to get friend List')
    return null
  }
};

const getMyInfo= async (retries=0, maxRetries=3)=>{ //내 정보 가져오기
    const refreshToken= localStorage.getItem('refreshToken')
    let accessToken=localStorage.getItem('accessToken');
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/user/profile`,{
      method: 'GET',
      headers:{
        'Authorization': `Bearer ${accessToken}`
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
    console.error('Failed to get MyInfo')
    return null
  }
}

const postMyinfo= async (formData, retries=0, maxRetries=3)=>{ 
  let accessToken= localStorage.getItem('accessToken');
  const refreshToken= localStorage.getItem('refreshToken');
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/user/profile`,{
      method:'PUT',
      headers:{
        'Authorization': `Bearer ${accessToken}`,
      },
      body: formData
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
    if (error.message === 'Unauthorized' && refreshToken && retries<maxRetries) {
      accessToken=await refreshAccessToken(refreshToken);
      if (accessToken) {
        const response = await postMyinfo(formData, retries+1, maxRetries);
        return response
      }

    }
    console.error('Failed to post MyInfo')
    return null
  }
}

const addFriend= async (id, retries=0, maxRetries=3)=>{ //친추
  let accessToken= localStorage.getItem('accessToken');
  const refreshToken= localStorage.getItem('refreshToken');
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/friend-requests/send?receiverId=${id}`,{
      method:'POST',
      headers:{
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
    if(!response.ok){
      if(response.status===401){
        throw new Error('Unauthorized')
      }
      throw new Error('Failed to send friend request:' `${response.status}`)
    }
    return await response.json();
  }
  catch(error){
    if (error.message === 'Unauthorized' && refreshToken && retries<maxRetries) {
      accessToken=await refreshAccessToken(refreshToken);
      if (accessToken) {
        const response = await addFriend(id, retries+1, maxRetries);
        return response
      }
    }
    console.error('Failed to send friend request')
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
    console.error('Failed to post MyInfo')
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
  user_id:'',
  user_name: "",
  user_introduction:'',
  user_job: "",
  user_equipment: "",
  user_field: "",
  user_area: "",
  user_photourl: defaultProfile
};

// 입력 필드 매핑
const FIELD_MAPPING = {
  [styles.name]: "name",
  [styles.job]: "job",
  [styles.myFieldInput]: "field",
  [styles.myEquipmentInput]: "equipment",
  [styles.myAreaInput]: "user_area",
  [styles.introduction]: "introduction"
};

function ProfileMain() {
  const [profileData, setProfileData] = useState(initialProfileState);
  const [nonFriendUsers, setNonFriendUsers] = useState([]);
  const [friendUsers, setFriendUsers] = useState([]);
  const [search, setSearch] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [isEdit, setIsEdit] = useState(false);
  const [profileImage, setProfileImage] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const fileInputRef = useRef(null);
  const canvasRef = useRef(null);
  const [imageScale, setImageScale] = useState(1);
  const [imagePosition, setImagePosition] = useState({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState(false);
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 });

  // 사용자 데이터 가져오기
  const fetchProfileAndUsers = useCallback(async () => {
    try {
      // 내 프로필 데이터 가져오기
      const myData = await getMyInfo();
      if (myData) {
        setProfileData({
          user_id: myData.user_id || "Unknown",
          name: myData.name || "Unknown",
          job: myData.job || "Unknown",
          field: myData.field || "Unknown",
          equipment: myData.equipment || "Unknown",
          user_area: myData.user_area || "Unknown",
          introduction: myData.introduction || "Unknown",
          user_photourl: myData.user_photourl || defaultProfile
        });
      }

      // 친구가 아닌 사용자 목록 가져오기
      const nonFriendsList = await getNonFriendsList();
      if (Array.isArray(nonFriendsList)) {
        setNonFriendUsers(nonFriendsList);
        setFilteredUsers(nonFriendsList);
      }

      // 친구인 사용자 목록 가져오기
      const friendList = await getFriendsList();
      if (Array.isArray(friendList)) {
        setFriendUsers(friendList);
      }
    } catch (error) {
      console.error(error);
    }
  }, []);

  useEffect(() => {
    fetchProfileAndUsers();
  }, [fetchProfileAndUsers]);

  const addFriendhandle = useCallback(async (userId) => {
    if (!userId) return;

    // 현재 상태 백업
    const rollBackNonFriends = [...nonFriendUsers];
    const rollBackFiltered = [...filteredUsers];

    // UI 즉시 업데이트
    setNonFriendUsers(prev => prev.filter(user => user.userId !== userId));
    setFilteredUsers(prev => prev.filter(user => user.userId !== userId));

    try {
      const response = await addFriend(userId);
      if (!response) {
        throw new Error('친구 추가에 실패했습니다.');
      }
    } catch (error) {
      // 실패 시 롤백
      setNonFriendUsers(rollBackNonFriends);
      setFilteredUsers(rollBackFiltered);
      console.error('친구 추가 실패:', error);
    }
  }, [nonFriendUsers, filteredUsers]);

  const handleRemoveFriend= async(userId)=>{ //친구 제거 핸들러
    const rollBackNonFriends= [...nonFriendUsers]
    const rollBackFriends= [...friendUsers]
    const findMatchUser=nonFriendUsers.find((user)=>user.userId === userId)
    const matchUser= {...findMatchUser};

    setFriendUsers((prevUsers) => //친구인 거에서 제거
      prevUsers.filter((user) => 
        user.userId !== userId
      )
    );
    setNonFriendUsers((prevUsers)=>[...prevUsers, matchUser]) //친구 아닌 거에 추가
    try{
      const response = await editFriend(userId)
      if(!response){
        throw new Error('친구 제거에 실패했습니다.')
      }
    } catch(error){
      setNonFriendUsers(rollBackNonFriends)
      setFriendUsers(rollBackFriends)
    }
  }

  const { setIsLogged } = useAuth();

  // 로그아웃 핸들러
  const handleLogout = useCallback(() => {
    try {
      setIsLogged(false)
      window.location.href = '/login';
      localStorage.removeItem('accessToken');
    } catch (error) {
      console.error('로그아웃 중 오류 발생:', error);
    }
  }, [setIsLogged]);

  // 검색 핸들러
  const handleSearch = useCallback((e) => {
    const searchValue = e.target.value;
    setSearch(searchValue);
    
    if (searchValue) {
      const filtered = nonFriendUsers.filter((user) =>
        user.userName.toLowerCase().includes(searchValue.toLowerCase())
      );
      setFilteredUsers(filtered);
    } else {
      setFilteredUsers(nonFriendUsers);
    }
  }, [nonFriendUsers]);

  useEffect(() => {
    // 검색어가 없을 때는 전체 목록 표시
    if(!search) {
      setFilteredUsers(nonFriendUsers);
    }
  }, [search, nonFriendUsers]);

  // 입력 필드 변경 핸들러
  const handleInputChange = useCallback((e) => {
    const { className, value } = e.target;
    const field = FIELD_MAPPING[className];
    if (field) {
      setProfileData(prev => ({
        ...prev,
        [field]: value  // 입력 중에는 trim() 하지 않음
      }));
    }
  }, []);



  const handleFileChange = useCallback((e) => {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (event) => {
            const img = new Image();
            img.onload = () => {
                setImageScale(1);
                setImagePosition({ x: 0, y: 0 });
                setProfileImage(event.target.result);
                // 이미지가 선택되면 바로 profileData의 user_photourl 업데이트
                setProfileData(prev => ({
                    ...prev,
                    user_photourl: event.target.result
                }));
                setIsEditing(true);
            };
            img.src = event.target.result;
        };
        reader.readAsDataURL(file);
    }
  }, []);

  const editHandle = useCallback(async (e) => {
    e.preventDefault();
    if (e.target.value === 'save') {
      // 저장 시에만 양 끝의 공백을 제거하고 빈 값은 'Unknown'으로 설정
      const trimmedData = {
        ...profileData,
        user_name: profileData?.name?.trim() || 'Unknown',
        user_id: profileData?.user_id || 'Unknown',
        user_introduction: profileData?.introduction?.trim() || 'Unknown',
        user_job: profileData?.job?.trim() || 'Unknown',
        user_equipment: profileData?.equipment?.trim() || 'Unknown',
        user_field: profileData?.field?.trim() || 'Unknown',
        user_area: profileData?.user_area?.trim() || 'Unknown',
        user_photourl: profileData?.user_photourl || defaultProfile
      };
      setIsEdit(false);
      try {
        const formData = new FormData();
        // 프로필 이미지가 변경되었고 base64 형식인 경우
        if (profileImage && profileImage.startsWith('data:image')) {
          // base64를 Blob으로 변환
          const response = await fetch(profileImage);
          const blob = await response.blob();
          formData.append('user_photourl', blob, 'profile.jpg');
        }
        // 다른 프로필 정보 추가
        Object.keys(trimmedData).forEach(key => {
          if (key !== 'user_photourl') { // 이미지는 별도로 처리했으므로 제외
            formData.append(key, trimmedData[key]);
          }
        });
        const response = await postMyinfo(formData); 
        console.log(response.user_photourl)
        if (response) {
          setProfileData({...trimmedData, user_photourl:response.user_photourl});
          // 프로필 저장 성공 후 데이터 새로고침
          await fetchProfileAndUsers();
        }
      } catch (error) {
        console.error('Error saving profile:', error);
      }
    } else {
      setIsEdit(true);
    }
  }, [profileData, profileImage, fetchProfileAndUsers]);

  const handleImageClick = () => {
    if (fileInputRef.current) {
        fileInputRef.current.click();
    }
  };

  const handleMouseDown = (e) => {
    if (!isEditing) return;
    setIsDragging(true);
    setDragStart({
        x: e.clientX - imagePosition.x,
        y: e.clientY - imagePosition.y
    });
  };

  const handleMouseMove = (e) => {
    if (!isDragging || !isEditing) return;
    setImagePosition({
        x: e.clientX - dragStart.x,
        y: e.clientY - dragStart.y
    });
  };

  const handleMouseUp = () => {
    setIsDragging(false);
  };

  const handleWheel = (e) => {
    if (!isEditing) return;
    e.preventDefault();
    const delta = e.deltaY > 0 ? 0.9 : 1.1;
    const newScale = Math.min(Math.max(imageScale * delta, 0.5), 3);
    setImageScale(newScale);
  };

  console.log(friendUsers)
  return (
    <div className={styles.allContainer}>
      <div className={styles.myInfoContainer}>
        <div className={styles.myDetailInfoContainer1}>
          <div className={styles.forFlexLeft}>
            <div className={styles.profileImageWrapper}>
              <div 
                className={styles.profileImageContainer}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
                onWheel={handleWheel}
              >
                  <img
                    src={profileData.user_photourl}
                    alt="Profile"
                    className={styles.profileImage}
                    style={{
                      transform: `scale(${imageScale})`,
                      transformOrigin: 'center',
                      position: isEditing ? 'absolute' : 'relative',
                      left: isEditing ? imagePosition.x : 'auto',
                      top: isEditing ? imagePosition.y : 'auto',
                      cursor: isEditing ? 'move' : 'default'
                    }}
                  />
                <input
                  type="file"
                  ref={fileInputRef}
                  onChange={handleFileChange}
                  
                  accept="image/*"
                  style={{ display: 'none' }}
                />
                <canvas
                  ref={canvasRef}
                  width={200}
                  height={200}
                  style={{ display: 'none' }}
                />
              </div>
              {isEdit && (
                <div 
                  className={styles.editImageIcon}
                  onClick={handleImageClick}
                >
                  <FontAwesomeIcon icon={faCamera} />
                </div>
              )}
            </div>
            <div className={styles.forFlex}>
              <input
                className={styles.name}
                onChange={handleInputChange}
                value={profileData?.name || ''}
                placeholder="이름을 알려주세요."
                disabled={!isEdit}
              />
              <input
                className={styles.job}
                onChange={handleInputChange}
                value={profileData?.job || ''}
                placeholder="직업을 알려주세요."
                disabled={!isEdit}
              />
              <div className={styles.id}>ID: {profileData?.user_id || ''}</div>
            </div>
          </div>
          <div className={styles.forFlexSetting}>
            <button 
              className={styles.logOutForFlexRight}
              onClick={handleLogout}>
              <img src={logout} alt="" className={styles.logoutIcon}></img>
              로그아웃
            </button>
            {isEdit ? (
              <button 
                className={styles.save}
                onClick={editHandle}
                value='save'
              >
                저장하기
              </button> 
            ) : (
              <button 
                className={styles.edit}
                onClick={editHandle}
                value='eidt'
              >
                수정하기
              </button>
            )}
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
              value={profileData?.field || ''}
              disabled={!isEdit}
            />
          </div>
          <div className={styles.myEquipmentContainer}>
            <p className={styles.myEquipment}>사용 장비</p>
            <input
              type="text"
              placeholder="sony A7 IV"
              className={styles.myEquipmentInput}
              onChange={handleInputChange}
              value={profileData?.equipment || ''}
              disabled={!isEdit}
            />
          </div>
          <div className={styles.myAreaContainer}>
            <p className={styles.myArea}>활동 지역</p>
            <input
              type="text"
              placeholder="활동 지역"
              className={styles.myAreaInput}
              onChange={handleInputChange}
              value={profileData?.user_area || ''}
              disabled={!isEdit}
            />
          </div>
        </div>

        <div className={styles.myDetailInfoContainer3}>
          <p className={styles.introTop}>소개</p>
          <textarea
            className={styles.introduction}
            placeholder="제가 누구냐면요.."
            onChange={handleInputChange}
            value={profileData?.introduction || ''}
            disabled={!isEdit}
          />
        </div>
      </div>

      <div className={styles.manageFriendContainer}>
        <p className={styles.manageFriendTop}>친구 관리</p>
        <div className={styles.forFlexFriend}>
          <div className={styles.myFriendsListContainer}>
            <p className={styles.myFriendListTop}>내 친구 목록</p>
            {friendUsers.length > 0 ? (
              friendUsers.map((user) => (
                <FriendManage
                  key={user.userId}
                  userId={user.userId}
                  userName={user.userName}
                  userPhotoUrl={user.userPhotoUrl}
                  handleRemoveFriend={handleRemoveFriend}
                />
              ))
            ) : (
              <p className={styles.zeroFriend}>앗, 친구가 없어요.😓</p>
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
            <div className={styles.forFlexFriendList}>
              {filteredUsers.map(user => (
                <SearchFriend
                  key={user.userId}
                  userId={user.userId}
                  userName={user.userName}
                  userImage={user.user_photourl}
                  addFriend={addFriendhandle}
                />
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfileMain;
