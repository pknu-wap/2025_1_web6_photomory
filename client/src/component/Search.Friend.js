import styles from './Search.Friend.module.css'


function SearchFriend(){
    return(
        <>
            <div className={styles.friendListContainer}>
                <div className={styles.forFlexLeft}>
                    <div
                    className={styles.image}></div>
                    <div className={styles.forFlex}>
                        <p className={styles.name}>권동욱</p>
                        <p className={styles.id}>id:1</p>
                    </div>
                </div>
                <button className={styles.addFriend}>친구 추가</button>
            </div>
        </>
    )
}

export default SearchFriend