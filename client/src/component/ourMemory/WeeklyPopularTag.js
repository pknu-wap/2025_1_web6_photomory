import styles from "./EveryMemoryMain.module.css";
import heart from "../../assets/heart.svg";
import comment from "../../assets/comment.svg";
import trophy from "../../assets/trophy.svg";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";
import emptyImage from "../../assets/emptyImage.svg";

export default function WeeklyPopularTag({
  post,
  handleLikeNum,
  handleCommentClickForModal,
  handleImageClick
}) 

{
  return (
    <div className={styles.weeklyTagContainer}
      onClick={()=>{
        handleImageClick(post)
    }}>
      <div
        className={styles.weeklyTagImage}
        style={{ backgroundImage: `url(${post?.photo_url || { emptyImage }})` }}
      ></div>
      <div className={styles.forPadding}>
        <img src={trophy} alt="" className={styles.trophyIcon}></img>
        <div className={styles.weeklyTagNthPlace}>1등:</div>
        <div className={styles.weeklyTagAlbumName}> {post.post_text}</div>
        <div className={styles.forFlexUserInfo}>
          <div
            className={styles.userImage}
            style={{
              backgroundImage: `url(${
              post.user_photourl || { defaultProfileIcon }
              })`,
            }}
          ></div>
          <div className={styles.userName}>{post.user_name}</div>
        </div>
        <div className={styles.forFlexweeklyTag2}
        onClick={e => e.stopPropagation()}>
          <div className={styles.heartContainer} onClick={handleLikeNum}>
            <img src={heart} alt="" className={styles.heartIcon} />
            <p className={styles.heartNum}>{post?.likes_count || "1.2k"}</p>
          </div>
          <div className={styles.commentContainer} onClick={handleCommentClickForModal}>
            <img src={comment} alt="" className={styles.commentIcon}></img>
            <p className={styles.commentNum}>{post?.comments_count || "80"}</p>
          </div>
        </div>
      </div>
    </div>
  );
}
