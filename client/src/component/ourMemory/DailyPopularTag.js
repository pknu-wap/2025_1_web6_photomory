import styles from "./EveryMemoryMain.module.css";
import heart from "../../assets/heart.svg";
import comment from "../../assets/comment.svg";
import emptyImage from "../../assets/emptyImage.svg";
import emptyHeart from '../../assets/emptyHeart.svg'

export default function DailyPopularTag({
  post,
  handleLikeNum,
  handleCommentClickForModal,
  handleImageClick
}){
  const postData=post[0]
  
  return (
    <div className={styles.todayTagContainer}>
      <img
        className={styles.todayTagImage}
        src={postData?.photoUrl || emptyImage}
        alt=""
        onClick={() => handleImageClick(postData)}></img>
      <div className={styles.forFlexTodayTag1}>
        <div className={styles.forFlexTodayTag2}>
          <span className={styles.todayTagImageName}>
            {postData?.postText || 'Unknown'}
          </span>
        </div>
        <p className={styles.todayTagExplain}>
          {postData?.postDescription || 'Unknown'}
        </p>
        <div className={styles.forFlexTodayTag3}>
          <div className={styles.heartContainer} onClick={(e) => {
            e.stopPropagation();
            handleLikeNum(postData?.postId);
          }}>
            {!postData?.liked ? ( 
              <img src={emptyHeart} alt="" className={styles.todayTagHeartIcon}/>
            ) : (
              <img src={heart} alt="" className={styles.todayTagHeartIcon}/>
            )}
            <span className={styles.todayTagheartText}>
              {postData?.likesCount || "0"}
            </span>
          </div>
          <div className={styles.commentContainer} 
          onClick={(e) => {
            e.stopPropagation();
            handleCommentClickForModal(postData);
          }}>
            <img
              src={comment}
              alt=""
              className={styles.todayTagCommentIcon}
            ></img>
            <span className={styles.forFlextodayTagCommentText}>
              <span className={styles.todayTagCommentText}>
                {postData?.commentCount || "0"}
              </span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}