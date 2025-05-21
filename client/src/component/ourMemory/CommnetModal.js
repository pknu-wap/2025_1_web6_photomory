import React from 'react';
import styles from './CommentModal.module.css';
import profile from '../../assets/defaultProfileIcon.svg'
import send from '../../assets/send.svg'

const CommentModal = ({ isOpen, onClose, handleCommentNum }) => { //post 추가해야 함 지금은 안 써서 안 씀
    if (!isOpen) return null;

    const post={
        "post_id": 1,
        "user_id": 4,
        "user_name": "박지현",
        "user_photourl":"https://www.notion.so/1c31d3377ee08052a9bed8107029f9f2?pvs=25#1c71d3377ee080eaa187f98eb1d90662",
        "post_text": "강릉 노을",
        "post_description": "강릉에서 찍은 사진입니다!",
        "likes_count": 23,
        "location":"부산광역시 남구 용소로 45",
        "photo_url": "https://www.notion.so/1c31d3377ee08052a9bed8107029f9f2?pvs=25#1c71d3377ee080eaa187f98eb1d90662",
        "tags": ["여행", "노을", "강릉"],
        "comment_count": 2,
        "comments": [
            {
            "user_id": 5,
            "user_name": "이민수",
            "user_photourl":"https://www.notion.so/1c31d3377ee08052a9bed8107029f9f2?pvs=25#1c71d3377ee080eaa187f98eb1d90662",
            "comment_text": "와 여기 어디에요? 너무 예뻐요!"
            },
            {
            "user_id": 6,
            "user_name": "정예린",
            "user_photourl":"https://www.notion.so/1c31d3377ee08052a9bed8107029f9f2?pvs=25#1c71d3377ee080eaa187f98eb1d90662",
            "comment_text": "사진 감성 미쳤다... 저장했어요"
            }
        ]
    }

    const formatDate = () => {
        const now = new Date();
        const year = now.getFullYear(); 
        const month = now.getMonth() + 1; 
        const day = now.getDate(); 
        return `${year}.${month}.${day}`;
    };
return (
    <div className={styles.modalOverlay} onClick={onClose}>
        <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
            {post.comments ? ( 
            post.comments.map((commentInfo) => (
                <div className={styles.commentContainer}>
                    <img
                        className={styles.userImage}
                        src={profile || ''} //commentInfo.user_photourl
                        alt=""
                    />
                    <div className={styles.forFlex}>
                        <div className={styles.forFlex2}>
                            <div className={styles.userName}>{commentInfo.user_name || '권동욱'}</div>
                            <div className={styles.date}>{formatDate()}</div>
                        </div>
                        <div className={styles.commentText}>{commentInfo.comment_text || ''}</div>
                    </div>
                </div>
            ))
            ) : (
            <div className={styles.nopost}>댓글이 없습니다 🥺</div>
            )}
            <div className={styles.uploadCommentContainer}>
                <input className={styles.uploadComment} placeholder='댓글 작성...'/>
                <img src={send} alt='' className={styles.uploadButton}
                    onClick={handleCommentNum} //클릭하면 추가되는 기능 추가하기
                />
            </div>
        </div>
    </div>
);
};

export default CommentModal;