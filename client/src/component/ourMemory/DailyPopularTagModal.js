import React from 'react';
import styles from './DailyPopularTagModal.module.css';
import mokImage from '../../assets/mainPageImage2.svg'

const DailyPopularTagModal = ({ isOpen, onClose, }) => { //post 추가해야 함 지금은 안 써서 안 씀
  if (!isOpen) return null;

  return (
    <div className={styles.modalOverlay} onClick={onClose}>
      <div className={styles.modalContent} onClick={e => e.stopPropagation()}>
        <img className={styles.image} src={mokImage} alt=''></img>
      </div>
    </div>
  )
} 

export default DailyPopularTagModal;