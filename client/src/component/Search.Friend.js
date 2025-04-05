import styles from './Search.Friend.module.css'


function SearchFriend(){
    return(
        <>
            <div className={styles.friendListContainer}>
                <div className={styles.forFlexLeft}>
                    <img src='#' alt='#'></img>
                    <p className={styles.name}>권동욱</p>
                    <p className={styles.id}>id:1</p>
                </div>
                <button className={styles.forFlexRight}>친구 추가</button>
            </div>
        </>
    )
}

export default SearchFriend