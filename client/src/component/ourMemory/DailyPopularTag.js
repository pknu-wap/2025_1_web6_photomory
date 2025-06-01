import styles from "./EveryMemoryMain.module.css";
import heart from "../../assets/heart.svg";
import comment from "../../assets/comment.svg";
import emptyImage from "../../assets/emptyImage.svg";

export default function DailyPopularTag({
  post,
  handleLikeClick,
  handleCommentClick,
}) {
  return (
    <div className={styles.todayTagContainer}>
      <span
        className={styles.todayTagImage}
        style={{ backgroundImage: `url(${post?.photo_url || { emptyImage }})` }}
      ></span>
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
          <div className={styles.heartContainer} onClick={handleLikeClick}>
            <img src={heart} alt="" className={styles.todayTagHeartIcon}></img>
            <span className={styles.todayTagheartText}>
              {post?.likes_count || "3.2k"}
              {/*하트 갯수 받기*/}
            </span>
          </div>
          <div className={styles.commentContainer} onClick={handleCommentClick}>
            <img
              src={comment}
              alt=""
              className={styles.todayTagCommentIcon}
            ></img>
            <span className={styles.forFlextodayTagCommentText}>
              <span className={styles.todayTagCommentText}>
                {post?.comments_count || "80"}
                {/*댓글 갯수 받기*/}
              </span>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
