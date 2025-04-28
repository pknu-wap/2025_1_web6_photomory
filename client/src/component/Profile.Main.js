import styles from './Profile.Main.module.css'
import FriendManage from './Friend.Manage'
import SearchFriend from './Search.Friend'
import GetUserProfilePage from '../api/GetUserProfilePage'
import GetMy from '../api/GetMy' 
import {useEffect, useMemo, useState} from 'react'


function ProfileMain() {
    const [users, setUsers]= useState([])
    const [id, setId] = useState();
    const [name, setName] = useState('');
    const [job, setJob] = useState('');
    const [field, setField]=useState('');
    const [myEquipment, setMyEquipment]=useState('');
    const [myArea, setMyArea]=useState('');
    const [introduction, setIntroduction]=useState('');
    const [search, setSearch]=useState('')


    const onChangeHandle = (e) => {
        if (e.target.className === styles.myFieldInput) {
            setField(e.target.value);
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
        if (e.target.className === styles.job) {
            setJob(e.target.value);
        }
    };

    const handleRemoverFriend=(userId)=>{
        setUsers((prevUsers)=>{ //prevUsers는 setUsers를 부를 때 자동으로 나오는 이전 상태태
            const updateUsers=prevUsers.map((user)=>
            user.id===userId ? {...user, isFriend: false} : user) //...user 속성 바꾸기
            return updateUsers
        })
    }

    useEffect(()=>{
        const fetchUsers= async ()=>{
            try{
                const userData= await GetUserProfilePage();
                setUsers(userData); 

                const myData= await GetMy();
                if (myData) {
                    setId(myData.id || '');
                    setName(myData.name || '');
                    setJob(myData.job || ''); 
                    setField(myData.field || '');
                    setMyEquipment(myData.equipment || '');
                    setMyArea(myData.area || '');
                    setIntroduction(myData.introduction || '');
                }
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
                        <input className={styles.name}
                        onChange={onChangeHandle}
                        value={name}
                        placeholder={name? name :'이름을 알려줘!'}></input>
                        <input className={styles.job}
                        onChange={onChangeHandle}
                        value={job}
                        placeholder={job? job: '직업을 알려줘!' }></input>
                        <div className={styles.id}>id:{id}</div>
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
                <div className={styles.myEquipmentContainer}>
                    <p className={styles.myEquipment}>사용 장비</p>
                    <input type='text' placeholder='sony A7 IV'
                    className={styles.myEquipmentInput}
                    onChange={onChangeHandle}
                    value={myEquipment}></input>
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
                <textarea className={styles.introduction}
                type="text" placeholder="제가 누구냐면요.."
                onChange={onChangeHandle}
                value={introduction}></textarea>
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
                            userField={user.field}
                            isFriend={user.isFriend}
                            onRemoveFriend={handleRemoverFriend}
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