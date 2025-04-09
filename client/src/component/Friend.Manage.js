import styles from './Friend.Manage.module.css'

function FriendManage({ name = '이름 없음', field = '분야 없음' }) {
    return(
        <>
            <div className={styles.myFriendItemContainer}>
                <div className={styles.myFriendItemContainerForFlexLeft}>
                    <div className={styles.myFieldItemImage}></div>
                    <div className={styles.myFieldInfoContainerForFlex}>
                        <p className={styles.myFieldItemName}>{name}</p>
                        <p className={styles.myFriendItemShortIntro}>{field}</p>
                    </div>
                </div>
                <button className={styles.myFriendItemDeleteRight}>삭제하기</button>
            </div>
        </>
    )
}

export default FriendManage