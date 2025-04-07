import styles from './Search.Friend.module.css';
import { useContext } from 'react';
import { context } from '../App'; // context 임포트 추가

function SearchFriend() {
    const { users } = useContext(context); 
    const user=users[0] //예시를 위한 것

    return (
        <>
            <div className={styles.friendListContainer}>
                <div className={styles.forFlexLeft}>
                    <div className={styles.image}></div>
                    <div className={styles.forFlex}>
                        <p className={styles.name}>{user?.user_name || '이름 없음'}</p>
                        <p className={styles.id}>{user ? 'id: ' + user.user_id : 'ID 없음'}</p>
                    </div>
                </div>
                <button className={styles.addFriend}>친구 추가</button>
            </div>
        </>
    );
}

export default SearchFriend;