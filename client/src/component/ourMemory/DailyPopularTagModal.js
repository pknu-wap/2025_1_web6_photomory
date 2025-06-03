import React from 'react';
import styles from './DailyPopularTagModal.module.css';


const DailyPopularTagModal = ({ isOpen, onClose, imageForModal, post}) => { //post 추가해야 함 지금은 안 써서 안 씀
  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} onClick={onClose}> 
        <img className={styles.image} src={imageForModal? imageForModal : post} alt=''
        onClick={e => e.stopPropagation()}></img>
    </div>
  )
} 

export default DailyPopularTagModal;