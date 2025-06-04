import React from 'react';
import styles from './DailyPopularTagModal.module.css';
import emptyImage from "../../assets/emptyImage.svg";

const DailyPopularTagModal = ({ isOpen, onClose, post }) => { 
  if (!isOpen) return null;
  
  const postData = post?.[0];

  return (
    <div className={styles.modalOverlay} onClick={onClose}> 
      <img 
        className={styles.image} 
        src={postData?.photoUrl || emptyImage} 
        alt=""
        onClick={e => e.stopPropagation()}
      />
    </div>
  );
} 

export default DailyPopularTagModal;