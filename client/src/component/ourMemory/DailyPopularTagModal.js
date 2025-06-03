import React from 'react';
import styles from './DailyPopularTagModal.module.css';

const DailyPopularTagModal = ({ isOpen, onClose, post, onLikeCommentClick }) => {
  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} onClick={onClose}> 
        <img className={styles.image} src={imageForModal? imageForModal : post} alt=''
        onClick={e => e.stopPropagation()}></img>
    </div>
  )
}

export default DailyPopularTagModal; 