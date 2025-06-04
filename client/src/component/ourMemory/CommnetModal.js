import React, { useState } from 'react';
import styles from './CommentModal.module.css';
import sendIcon from '../../assets/sendIcon.svg'

const CommentModal = ({ isOpen, onClose, handleCommentNum, post }) => { //post 추가해야 함 지금은 안 써서 안 씀
    const [commentInput, setCommentInput]= useState('')
    if (!isOpen) return null;

    const handleCommentInput=(e)=>{
        setCommentInput(e.target.value)
    }

    const checkComment = () => {
        if (commentInput.trim()) {  // 빈 댓글은 전송하지 않음
            handleCommentNum(commentInput);
            setCommentInput('');  // 댓글 전송 후 입력창 초기화
        }
    }

    const handleKeyPress = (e) => {
        if (e.key === 'Enter') {
            checkComment();
        }
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
                    <div className={styles.commentContainer} key={commentInfo.userId}>
                        <img
                            className={styles.userImage}
                            src={commentInfo.user_photourl || ''} 
                            alt=""
                        />
                        <div className={styles.forFlex}>
                            <div className={styles.forFlex2}>
                                <div className={styles.userName}>{commentInfo.userName || '권동욱'}</div>
                                <div className={styles.date}>{formatDate()}</div>
                            </div>
                            <div className={styles.commentText}>{commentInfo.commentText || ''}</div>
                        </div>
                    </div>
                ))
                ) : (
                <div className={styles.nopost}>댓글이 없습니다 🥺</div>
                )}
                <div className={styles.uploadCommentContainer}>
                    <input 
                        className={styles.uploadComment} 
                        placeholder='댓글 작성...'
                        value={commentInput}
                        onChange={handleCommentInput}
                        onKeyPress={handleKeyPress}
                    />
                    <div className={styles.buttonContainer}>
                        <img 
                            src={sendIcon} 
                            alt='' 
                            className={styles.uploadButton}
                            onClick={checkComment}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CommentModal;