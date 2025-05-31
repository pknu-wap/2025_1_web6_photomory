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
}) {
  return (
    <div className={styles.todayTagContainer}>
      <img
        className={styles.todayTagImage}
        src={post?.photoUrl ||  emptyImage }
        alt=""
        onClick={handleImageClick}></img>
      <div className={styles.forFlexTodayTag1}>
        <div className={styles.forFlexTodayTag2}>
          <span className={styles.todayTagImageName}>
            {post?.post_text || 'Unknown'}
          </span>
        </div>
        <p className={styles.todayTagExplain}>
          {post?.post_description || 'Unknown'}
        </p>
        <div className={styles.forFlexTodayTag3}>
          <div className={styles.heartContainer} onClick={(e) => {
            e.stopPropagation();
            handleLikeNum();
          }}>
            {post?.isLikeCountUp===false ? ( 
              <img src={emptyHeart} alt="" className={styles.todayTagHeartIcon}/>
            ) : (
              <img src={heart} alt="" className={styles.todayTagHeartIcon}/>
            )}
            <span className={styles.todayTagheartText}>
              {post?.likesCount || "Unknown"}
            </span>
          </div>
          <div className={styles.commentContainer} 
          onClick={(e) => {
            e.stopPropagation();
            handleCommentClickForModal(post);
          }}>
            <img
              src={comment}
              alt=""
              className={styles.todayTagCommentIcon}
            ></img>
            <span className={styles.forFlextodayTagCommentText}>
              <span className={styles.todayTagCommentText}>
                {post?.commentsCount || "Unknown"}
              </span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
