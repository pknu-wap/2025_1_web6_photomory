import styles from './Friend.Manage.module.css'

function FriendManage({ userId, userName="이름 없음", userField, onRemoveFriend }) {
    const handleRemove=()=>{
        onRemoveFriend(userId)
    }
    return(
        <>
            <div className={styles.myFriendItemContainer}>
                <div className={styles.myFriendItemContainerForFlexLeft}>
                    <div className={styles.myFieldItemImage}></div>
                    <div className={styles.myFieldInfoContainerForFlex}>
                        <p className={styles.myFieldItemName}>{userName}</p>
                        <p className={styles.myFriendItemShortIntro}>{userField}</p>
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