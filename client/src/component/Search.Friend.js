import styles from './Search.Friend.module.css';

function SearchFriend(userId, userName) {

    return (
        <>
            <div className={styles.friendListContainer}>
                <div className={styles.forFlexLeft}>
                    <div className={styles.image}></div>
                    <div className={styles.forFlex}>
                        <p className={styles.name}>{userName}</p>
                        <p className={styles.id}>{userId}</p>
                    </div>
                </div>
                <button className={styles.addFriend}>친구 추가</button>
            </div>
        </>
    );
}

export default SearchFriend;