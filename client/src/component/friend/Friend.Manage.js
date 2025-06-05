import styles from './Friend.Manage.module.css'
import defaultProfileIcon from '../../assets/defaultProfileIcon.svg'

function FriendManage({ userId, userName="이름 없음", userPhotoUrl, handleRemoveFriend }) {
    const handleRemove=()=>{
        handleRemoveFriend(userId)
    }
    return(
        <>
            <div className={styles.myFriendItemContainer}>
                <div className={styles.myFriendItemContainerForFlexLeft}>
                    <img src={userPhotoUrl || defaultProfileIcon} alt='' className={styles.myFieldItemImage}></img>
                    <div className={styles.myFieldInfoContainerForFlex}>
                        <p className={styles.myFieldItemName}>{userName}</p>
                        <p className={styles.myFriendItemShortIntro}>id:{userId || 'Unknown'}</p>
                    </div>
                </div>
                <button className={styles.myFriendItemDeleteRight}
                onClick={handleRemove}>
                    삭제하기
                </button>
            </div>
        </>
    )
}

export default FriendManage