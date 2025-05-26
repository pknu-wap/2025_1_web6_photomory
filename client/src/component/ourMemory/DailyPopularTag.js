import styles from "./EveryMemoryMain.module.css";
import heart from "../../assets/heart.svg";
import comment from "../../assets/comment.svg";
import emptyImage from "../../assets/emptyImage.svg";

export default function DailyPopularTag({
  post,
  handleLikeNum,
  handleCommentClickForModal,
  handleImageClick
}) {
  return (
    <div className={styles.todayTagContainer}>
      <span
        className={styles.todayTagImage}
        style={{ backgroundImage: `url(${post?.photoUrl || { emptyImage }})` }}
        onClick={handleImageClick}></span>
      <div className={styles.forFlexTodayTag1}>
        <div className={styles.forFlexTodayTag2}>
          {/*여기 아이콘은 빼야 할 듯*/}
          <span className={styles.todayTagImageName}>
            {post.post_text}
            {/*앨범 제목 받아오기*/}
          </span>
        </div>
        <p className={styles.todayTagExplain}>
          {post.post_description}
          {/*설명 받기*/}
        </p>
        <div className={styles.forFlexTodayTag3}>
          <div className={styles.heartContainer} onClick={(e) => {
            e.stopPropagation();
            handleLikeNum();
          }}>
            <img src={heart} alt="" className={styles.todayTagHeartIcon}></img>
            <span className={styles.todayTagheartText}>
              {post?.likesCount || "3.2k"}
              {/*하트 갯수 받기*/}
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
                {post?.commentsCount || "80"}
                {/*댓글 갯수 받기*/}
              </span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
