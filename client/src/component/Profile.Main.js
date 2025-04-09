import styles from './Profile.Main.module.css'
import FriendManage from './Friend.Manage'
import SearchFriend from './Search.Friend'
import GetUser from '../api/GetUser'
import {useEffect, useMemo, useState} from 'react'


function ProfileMain() {
    const [users, setUsers]= useState([])
    
    const [field, setField]=useState('');
    const [myTool, setMyTool]=useState('');
    const [myArea, setMyArea]=useState('');
    const [longIntro, setLongIntro]=useState('');
    const [search, setSearch]=useState('')

    
const onChangeHandle=(e)=>{
    if (e.target.className === styles.myFieldInput) {
        setField(e.target.value)
    }
    if (e.target.className === styles.myToolInput) {
        setMyTool(e.target.value)
    }
    if (e.target.className === styles.myAreaInput) {
        setMyArea(e.target.value)
    }
    if (e.target.className === styles.longIntro) {
        setLongIntro(e.target.value)
    }
}

useEffect(()=>{
    const fetchUsers= async ()=>{
        try{
            const data= await GetUser();
            setUsers(data);
        }
        catch(error){
            console.error('Error fetching users:',error);
        }
    }
    fetchUsers();
},[]);

const onChangeSearch=(e)=>{
    setSearch(e.target.value);
};

const filterUsers = useMemo(() => {
    return users.filter((user) =>
        user && user.id ? user.id.toString().includes(search) : false
    );
}, [users, search]);

    return(
    <>
        <div className={styles.myInfoContainer}>
            <div className={styles.myDetailInfoContainer1}>
                <div className={styles.forFlexLeft}>
                    <div className={styles.image}></div>
                    <div className={styles.forFlex}>
                        <div className={styles.name}>권동욱</div>
                        <div className={styles.shortIntro}>자연 풍경 전문 사진작가</div>
                        <div className={styles.photoNum}>사진 999</div>
                    </div>
                </div>
                <button className={styles.logOutForFlexRight}>log out</button>
            </div>
            <div className={styles.myDetailInfoContainer2}>
                <div className={styles.myFieldContainer}>
                    <p className={styles.myField}>전문 분야</p>
                    <input type='text' placeholder='풍경 사진'
                    className={styles.myFieldInput}
                    onChange={onChangeHandle}
                    value={field}></input>
                </div>
                <div className={styles.myToolContainer}>
                    <p className={styles.myTool}>사용 장비</p>
                    <input type='text' placeholder='sony A7 IV'
                    className={styles.myToolInput}
                    onChange={onChangeHandle}
                    value={myTool}></input>
                </div>
                <div className={styles.myAreaContainer}>
                    <p className={styles.myArea}>활동 지역</p>
                    <input type='text' placeholder='서울, 강원'
                    className={styles.myAreaInput}
                    onChange={onChangeHandle}
                    value={myArea}></input>
                </div>
            </div>
            <div className={styles.myDetailInfoContainer3}>
                <p className={styles.introTop}>소개</p>
                <textarea className={styles.longIntro}
                type="text" placeholder="제가 누구냐면요.."
                onChange={onChangeHandle}
                value={longIntro}></textarea>
            </div>
        </div>
        <p className={styles.manageFriendTop}>친구 관리</p>
        <div className={styles.manageFriendContainer}>
            <div className={styles.myFriendsListContainer}>
                <p className={styles.myFriendListTop}>내 친구 목록</p>
                {users.map((user) => (
                    user.isFriend ? (
                        <FriendManage
                            key={user.id}
                            userId={user.id}
                            userName={user.name}
                        />
                    ) : null
                ))}
            </div>
            <div className={styles.searchFriendContainer}>
                <p className={styles.searchMyFriendTop}>친구 검색</p>
                <input type='text' className={styles.searchBar} 
                placeholder='친구 id를 입력하세요!'
                onChange={onChangeSearch}></input>
                {filterUsers.map((user)=>(
                    <SearchFriend
                    key={user.id}
                    userId={user.id}
                    userName={user.name}    
                    />
                ))}
            </div>
        </div>
    </>
    )
}

export default ProfileMain