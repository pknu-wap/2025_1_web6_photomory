import styles from './Search.Friend.module.css';
import defaultProfileIcon from '../../assets/defaultProfileIcon.svg'

function SearchFriend({userId, userName, addFriend, userImage}) {

    return (
        <>
            <div className={styles.friendListContainer}>
                <div className={styles.forFlexLeft}>
                    <img src={userImage || defaultProfileIcon} alt='' className={styles.image}></img>
                    <div className={styles.forFlex}>
                        <p className={styles.name}>{userName || 'Unknown'}</p>
                        <p className={styles.id}>id:{userId || 'Unknown'}</p>
                    </div>
                </div>
                <button className={styles.addFriend}
                onClick={()=>{
                    addFriend(userId)
                }}>친구 추가</button>
            </div>
        </>
    );
}

export default SearchFriend;