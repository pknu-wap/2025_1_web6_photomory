import React, { useState } from 'react';
import styles from './CommentModal.module.css';
import sendIcon from '../../assets/sendIcon.svg'
import { useAuth } from "../../contexts/AuthContext";
import defaultProfileIcon from '../../assets/defaultProfileIcon.svg'

const CommentModal = ({ isOpen, onClose, handleCommentNum, post }) => {
    const [commentInput, setCommentInput] = useState('');
    const [localComments, setLocalComments] = useState([]);
    const {name}= useAuth()
    if (!isOpen) return null;

    const handleCommentInput = (e) => {
        setCommentInput(e.target.value);
    }

    const checkComment = () => {
        if (commentInput.trim()) {  // 빈 댓글은 전송하지 않음
            // 새로운 댓글 객체 생성
            const newComment = {
                userName: name, // 실제 사용자 이름으로 대체 필요
                comment: commentInput,    // 서버와 일치하도록 comment 키 사용
                date: formatDate(),
                userPhotourl: ''         // 실제 사용자 사진 URL로 대체 필요
            };
            // 로컬 상태에 즉시 추가
            setLocalComments(prev => [...prev, newComment]);
            // 서버로 전송 (비동기 처리, UI는 기다리지 않음)
            handleCommentNum(post, commentInput);
            setCommentInput('');  // 입력창 초기화
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
    console.log(post)
    return (
        <div className={styles.modalOverlay} onClick={onClose}>
            <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
                <div className={styles.commentsList}>
                    {post && post.comments.length>0 ? (
                        [...post.comments, ...localComments].map((commentInfo, index) => (
                            <div className={styles.commentContainer} key={index}>
                                <img
                                    className={styles.userImage}
                                    src={commentInfo.userPhotourl || defaultProfileIcon} 
                                    alt=""
                                />
                                <div className={styles.forFlex}>
                                    <div className={styles.forFlex2}>
                                        <div className={styles.userName}>{commentInfo.userName || 'Unknown'}</div>
                                        <div className={styles.date}>{commentInfo.date || formatDate()}</div>
                                    </div>
                                    <div className={styles.commentText}>{commentInfo.comment || ''}</div>
                                </div>
                            </div>
                        ))
                    ) : (
                        <div className={styles.nopost}>댓글이 없습니다 🥺</div>
                    )}
                </div>
                <div className={styles.uploadCommentContainer}>
                    <input 
                        className={styles.uploadComment}
                        placeholder='댓글 작성...'
                        value={commentInput}
                        onChange={handleCommentInput}
                        onKeyPress={handleKeyPress}
                    />
                    <div className={styles.buttonContainer}
                        onClick={checkComment}>
                        <img 
                            src={sendIcon} 
                            alt='' 
                            className={styles.uploadButton}
                        />
                    </div>
                </div>
            </div>
        </div>
    );
};

export default CommentModal;