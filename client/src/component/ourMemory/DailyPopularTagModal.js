import React from 'react';
import styles from './DailyPopularTagModal.module.css';

const DailyPopularTagModal = ({ isOpen, onClose, post, onLikeCommentClick }) => {
  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} onClick={onClose}>
      <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
        <div className={styles.modalHeader}>
          <h2>{post[0]?.title || []}</h2>
          <button className={styles.closeButton} onClick={onClose}>×</button>
        </div>
        <div className={styles.modalBody}>
          <img 
            src={post[0]?.imageUrl || []} 
            alt={post[0]?.title || []} 
            className={styles.modalImage}
          />
          <div className={styles.postInfo}>
            <p className={styles.description}>{post[0]?.description || []}</p>
            <div className={styles.interactionContainer}>
              <div className={styles.heartContainer}>
                <span className={styles.heartIcon}>❤️</span>
                <span className={styles.heartNum}>{post[0]?.likes || []}</span>
              </div>
              <div className={styles.commentContainer}>
                <span className={styles.commentIcon}>💬</span>
                <span className={styles.commentNum}>{post[0]?.comments || []}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default DailyPopularTagModal; 