import styles from "./EveryMemoryMain.module.css";
import heart from "../../assets/heart.svg";
import emptyHeart from '../../assets/emptyHeart.svg'
import comment from "../../assets/comment.svg";
import trophy from "../../assets/trophy.svg";
import defaultProfileIcon from "../../assets/defaultProfileIcon.svg";
import emptyImage from "../../assets/emptyImage.svg";

export default function WeeklyPopularTag({
  post, //여기에 isLikeCountUp 포함됨
  handleLikeNum,
  handleCommentClickForModal,
  handleImageClick,
  index
}) 
{
  const postData = post[0]; // 배열의 첫 번째 요소를 가져옴
  return (
    <div className={styles.weeklyTagContainer}>
      <img
        className={styles.weeklyTagImage}
        src={postData?.photoUrl || emptyImage}
        alt=""
        onClick={()=>{
          handleImageClick(postData)
        }}
      ></img> 
      <div className={styles.forPadding}>
        <img src={trophy} alt="" className={styles.trophyIcon}></img>
        <div className={styles.weeklyTagNthPlace}>{index}등:</div>
        <div className={styles.weeklyTagAlbumName}> {postData?.postText || 'Unknown'}</div>
        <div className={styles.forFlexUserInfo}>
          <img
            className={styles.userImage}
            src={postData?.userPhotourl ||  defaultProfileIcon}
            alt=""/>
          <div className={styles.userName}>{postData?.userName || 'Unknown'}</div>
        </div>
        <div className={styles.forFlexweeklyTag2}
        onClick={e => e.stopPropagation()}>
          <div className={styles.heartContainer} onClick={()=>handleLikeNum(postData?.postId)}> 
            {postData?.liked===false ? ( 
              <img src={emptyHeart} alt="" className={styles.heartIcon}></img>
            ) : (
              <img src={heart} alt="" className={styles.heartIcon} />
            )}
            <p className={styles.heartNum}>{postData?.likesCount || "0"}</p>
          </div>
          <div className={styles.commentContainer} onClick={handleCommentClickForModal}>
            <img src={comment} alt="" className={styles.commentIcon}></img>
            <p className={styles.commentNum}>{postData?.commentCount || "0"}</p>
          </div>
        </div>
      </div>
    </div>
  );
}

