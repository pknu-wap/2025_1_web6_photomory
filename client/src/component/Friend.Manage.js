import styles from './Friend.Manage.module.css'

function FriendManage(){
    return(
        <>
            <div className={styles.myFriendItemContainer}>
                <div className={styles.myFriendItemContainerForFlexLeft}>
                    <div className={styles.myFieldItemImage}></div>
                    <div className={styles.myFieldInfoContainerForFlex}>
                        <p className={styles.myFieldItemName}>권동욱</p>
                        <p className={styles.myFriendItemShortIntro}>사진찍기</p>
                    </div>
                </div>
                <button className={styles.myFriendItemDeleteForFlexRight}>삭제하기</button>
            </div>
        </>
    )
}

export default FriendManage