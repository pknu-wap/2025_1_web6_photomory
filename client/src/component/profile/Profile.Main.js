import React, { useEffect, useMemo, useState, useCallback, useRef } from "react";
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
  [styles.myAreaInput]: "area",
  [styles.introduction]: "introduction"
};

function ProfileMain() {
  const [users, setUsers] = useState([]);
  const [id, setId] = useState();
  const [name, setName] = useState("");
  const [job, setJob] = useState("");
  const [field, setField] = useState("");
  const [myEquipment, setMyEquipment] = useState("");
  const [myArea, setMyArea] = useState("");
  const [introduction, setIntroduction] = useState("");
  const [search, setSearch] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([])
  const [isEdit, setIsEdit]= useState(false)
  const [profileImage, setProfileImage] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const fileInputRef = useRef(null);
  const canvasRef = useRef(null);
  const [imageScale, setImageScale] = useState(1);
  const [imagePosition, setImagePosition] = useState({ x: 0, y: 0 });
  const [isDragging, setIsDragging] = useState(false);
  const [dragStart, setDragStart] = useState({ x: 0, y: 0 });

  
    // 사용자 데이터 가져오기
  useEffect(() => {
    const fetchProfileAndUsers = async () => {
      try {
        // 내 프로필 데이터 가져오기
        const myData = await getMyInfo();
        if (myData) {
          setProfileData({
            id: myData.id || "Unknown",
            name: myData.name || "Unknown",
            job: myData.job || "Unknown",
            field: myData.field || "Unknown",
            equipment: myData.equipment || "Unknown",
            area: myData.area || "Unknown",
            introduction: myData.introduction || "Unknown",
            profileImage: myData.user_photourl || defaultProfile
          });
        }

        // 친구가 아닌 사용자 목록 가져오기
        const nonFriendsList = await getNonFriendsList();
        if (Array.isArray(nonFriendsList)) {
          setNonFriendUsers(nonFriendsList);
        } else {
          throw new Error('잘못된 사용자 데이터 형식입니다.');
        }

        //친구인 사용자 목록 가져오기
        const friendList= await getFriendsList()
        if (Array.isArray(friendList)) {
          setFriendUsers(friendList);
        } else {
          throw new Error('잘못된 사용자 데이터 형식입니다.');
        }
      } catch (error) {
        console.error(error);
      }
    };
    fetchProfileAndUsers();
  }, []);



  const addFriendhandle= async(userId)=>{ //친구 추가
    if(!userId) return;
    const rollBackNonFriends= [...nonFriendUsers]
    const rollBackFriends= [...friendUsers]
    const findMatchUser=nonFriendUsers.find((user)=>user.userId === userId)
    const matchUser= {...findMatchUser, isFriend: true};

    setNonFriendUsers((prevUsers) => //친구 아닌 곳에서 제외외
      prevUsers.filter((user) => 
        user.userId !== userId
      )
    );
    setFriendUsers((prevUsers)=>[...prevUsers, matchUser]) //친구 추가
    try{
      const response = await addFriend(userId)
      if(!response || !response.success){
        throw new Error('친구 추가에 실패했습니다.')
      }
    } catch(error){
      setNonFriendUsers(rollBackNonFriends)
      setFriendUsers(rollBackFriends)
    }
    if (e.target.className === styles.myEquipmentInput) {
      setMyEquipment(e.target.value);
    }
    if (e.target.className === styles.myAreaInput) {
      setMyArea(e.target.value);
    }
    if (e.target.className === styles.introduction) {
      setIntroduction(e.target.value);
    }
    if (e.target.className === styles.name) {
      setName(e.target.value);
    }
  }, [search, nonFriendUsers])

  //이것도 갯수제한 두지 말고 그냥 스크롤로 해두자자
  const numLimitedFilteredUsers= search? filteredUsers : nonFriendUsers //혹시나해서서

  // 친구 목록 필터링
  const friends = useMemo(() => 
    friendUsers.filter(user => user?.isFriend)
  , [friendUsers]);

  const editHandle = async (e) => {
    e.preventDefault();
    if (e.target.value === 'save') {
      // 저장 시에만 양 끝의 공백을 제거하고 빈 값은 'Unknown'으로 설정
      const trimmedData = {
        ...profileData,
        user_name: profileData?.name?.trim() || 'Unknown',
        user_introduction: profileData?.introduction?.trim() || 'Unknown',
        user_job: profileData?.job?.trim() || 'Unknown',
        user_equipment: profileData?.equipment?.trim() || 'Unknown',
        user_field: profileData?.field?.trim() || 'Unknown',
        user_area: profileData?.area?.trim() || 'Unknown',
      };

      setIsEdit(false);
      try {
        const formData = new FormData();
        
        // 프로필 이미지가 변경되었고 base64 형식인 경우
        if (profileImage && profileImage.startsWith('data:image')) {
          // base64를 Blob으로 변환
          const response = await fetch(profileImage);
          const blob = await response.blob();
          formData.append('profile_photourl', blob, 'profile.jpg');
        }

        // 다른 프로필 정보 추가
        Object.keys(trimmedData).forEach(key => {
          if (key !== 'user_photourl') { // 이미지는 별도로 처리했으므로 제외
            formData.append(key, trimmedData[key]);
          }
        });

        const response = await postMyinfo(formData);
        if (response) {
          setProfileData(trimmedData);
        }
      } catch (error) {
        console.error('Error saving profile:', error);
      }
    } else {
      setIsEdit(true);
    }
  };

  const handleImageClick = () => {
    if (fileInputRef.current) {
        fileInputRef.current.click();
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = (event) => {
            const img = new Image();
            img.onload = () => {
                setImageScale(1);
                setImagePosition({ x: 0, y: 0 });
                setProfileImage(event.target.result);
                setIsEditing(true);
            };
            img.src = event.target.result;
        };
        reader.readAsDataURL(file);
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

  return (
    <>
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
                {profileImage ? (
                  <img
                    src={profileImage}
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
                ) : (
                  <img src={defaultProfile} alt=""></img>
                )}
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
              <div className={styles.id}>ID: {profileData?.id || ''}</div>
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
              value={profileData?.area || ''}
              disabled={!isEdit}
            />
          </div>
        </div>
        <div className={styles.myDetailInfoContainer3}>
          <p className={styles.introTop}>소개</p>
          <textarea
            className={styles.introduction}
            type="text"
            placeholder="제가 누구냐면요.."
            onChange={handleInputChange}
            value={profileData?.introduction || ''}
            disabled={!isEdit}
          />
        </div>
      </div>
      <p className={styles.manageFriendTop}>친구 관리</p>
      <div className={styles.manageFriendContainer}>
        <div className={styles.myFriendsListContainer}>
          <p className={styles.myFriendListTop}>내 친구 목록</p>
          {users.length > 0 && users.some(user => user.isFriend) ? (
            users.filter(user => user.isFriend).map(user => (
              <FriendManage
                key={user.id}
                userId={user.id}
                userName={user.name}
                userField={user.field}
                isFriend={user.isFriend}
                onRemoveFriend={handleRemoverFriend}
              />
            ))
          ) : (
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
            <div className={styles.forFlexFriendList}>
              {numLimitedFilteredUsers.map(user => (
                <SearchFriend
                  key={user.userId}
                  userId={user.userId} 
                  userName={user.userName} 
                  userImage={user.Userphotourl}
                  addFriend={addFriendhandle}
                />))}
            </div>
          </div>
        </div>
    </>
  );
}

export default ProfileMain;
