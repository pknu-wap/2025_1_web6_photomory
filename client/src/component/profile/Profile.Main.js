import React, { useEffect, useMemo, useState, useCallback } from "react";
import { useAuth } from "../../contexts/AuthContext";
import styles from "./Profile.Main.module.css";
import FriendManage from "../friend/Friend.Manage";
import SearchFriend from "../friend/Search.Friend";
import logout from '../../assets/logout.svg'
import defaultProfile from "../../assets/defaultProfileIcon.svg";

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

const postMyinfo= async (myInfo, retries=0, maxRetries=3)=>{ 
  let accessToken= localStorage.getItem('accessToken');
  const refreshToken= localStorage.getItem('refreshToken');
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/user/profile `,{
      method:'PUT',
      headers:{
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      },
      body: JSON.stringify({myInfo})
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
      },
      body: JSON.stringify({id})
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
        const response = await addFriend(id, retries+1, maxRetries);
        return response
      }
    }
    console.error('Failed to update Friend')
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
  const [profileData, setProfileData] = useState(initialProfileState);
  const [nonFriendUsers, setNonFriendUsers] = useState([]);
  const [friendUsers, setFriendUsers] = useState([])
  const [search, setSearch] = useState("");
  const [filteredUsers, setFilteredUsers] = useState([])
  const [isEdit, setIsEdit]= useState(false)

  
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
  }

  const handleRemoveFriend= async(userId)=>{ //친구 제거 핸들러
    if(!userId) return;
    const rollBackNonFriends= [...nonFriendUsers]
    const rollBackFriends= [...friendUsers]
    const findMatchUser=nonFriendUsers.find((user)=>user.userId === userId)
    const matchUser= {...findMatchUser, isFriend: false};

    setFriendUsers((prevUsers) => //친구인 거에서 제거
      prevUsers.filter((user) => 
        user.userId !== userId
      )
    );
    setNonFriendUsers((prevUsers)=>[...prevUsers, matchUser]) //친구인 거에 추가가
    try{
      const response = await editFriend(userId)
      if(!response || !response.success){
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
  }, []);

  // 검색 핸들러
  const handleSearch = useCallback((e) => {
    setSearch(e.target.value);
  }, []);

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

  useEffect(()=>{ //친구 검색
    if(search){
      const filtered=nonFriendUsers.filter((user)=>
        user.userName.toLowerCase().includes(search.toLowerCase())
      )
      setFilteredUsers(filtered)
    } else{
      setFilteredUsers(nonFriendUsers)
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
    // 저장 시에만 양 끝의 공백을 제거
    const trimmedData = {
      ...profileData,
      user_name: profileData?.name?.trim() || '',
      user_introduction: profileData?.introduction?.trim() || '',
      user_job: profileData?.job?.trim() || '',
      user_equipment: profileData?.equipment?.trim() || '',
      user_field: profileData?.field?.trim() || '',
      user_photourl: profileData?.photourl?.trim() || '',
    };
      setIsEdit((prev)=>!prev)
    try {
      if(e.target.value==='save'){
        const response = await postMyinfo(trimmedData);
        if (response) {
          setProfileData(trimmedData);
        }
      }
    } catch (error) {
      console.error('Error saving profile:', error);
    }
  };


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
                value={profileData?.name ||'unKnown'}
                placeholder="이름을 알려주세요."
              />
              <input
                className={styles.job}
                onChange={handleInputChange}
                value={profileData?.job || "unKnown"}
                placeholder="직업을 알려주세요."
              />
              <div className={styles.id}>ID: {profileData?.id ||'unKnown'}</div>
            </div>
          </div>
          <div className={styles.forFlexSetting}>
            <button 
              className={styles.logOutForFlexRight}
              onClick={handleLogout}>
              <img src={logout} alt="" className={styles.logoutIcon}></img>
              로그아웃
            </button>
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
              value={profileData?.field || 'unKnown'}
            />
          </div>
          <div className={styles.myEquipmentContainer}>
            <p className={styles.myEquipment}>사용 장비</p>
            <input
              type="text"
              placeholder="sony A7 IV"
              className={styles.myEquipmentInput}
              onChange={handleInputChange}
              value={profileData?.equipment || 'unKnown'}
            />
          </div>
          <div className={styles.myAreaContainer}>
            <p className={styles.myArea}>활동 지역</p>
            <input
              type="text"
              placeholder="서울, 강원"
              className={styles.myAreaInput}
              onChange={handleInputChange}
              value={profileData?.area || 'unKnown'}
            />
          </div>
        </div>

        <div className={styles.myDetailInfoContainer3}>
          <p className={styles.introTop}>소개</p>
          <textarea
            className={styles.introduction}
            placeholder="제가 누구냐면요.."
            onChange={handleInputChange}
            value={profileData?.introduction || 'unKnown'}
          />
        </div>
      </div>

      <div className={styles.manageFriendContainer}> {/*애니 효과 추가하기*/}
        <p className={styles.manageFriendTop}>친구 관리</p>
        <div className={styles.forFlexFriend}>
          <div className={styles.myFriendsListContainer}>
            <p className={styles.myFriendListTop}>내 친구 목록</p>
            {friends.length > 0 ? friends.map((user) => ( //친구가 어느 정도 이상이면 오버플로우로 스크롤할 수 있게 
              <FriendManage
                key={user.id}
                userId={user.id}
                userName={user.name}
                userField={user.field}
                isFriend={user.isFriend}
                handleRemoveFriend={handleRemoveFriend}
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
            <div className={styles.forFlexFriendList}>
              {numLimitedFilteredUsers.map(user => (
                <SearchFriend
                  key={user.id}
                  userId={user.id} 
                  userName={user.name} 
                  userImage={user.Userphotourl}
                  addFriend={addFriendhandle} //유저 아이디 보내줌
                />))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ProfileMain;
