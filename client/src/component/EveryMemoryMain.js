import styles from './EveryMemoryMain.module.css'
import leftButton from '../assets/leftButton.svg'
import rightButton from '../assets/rightButton.svg'
import num1 from '../assets/num1.svg'
import num2 from '../assets/num2.svg'
import num3 from '../assets/num3.svg'
import num4 from '../assets/num4.svg'
import heart from '../assets/heart.svg'
import comment from '../assets/comment.svg'
import trophy from '../assets/trophy.svg'
import camera from '../assets/camera.svg'
import landscape from '../assets/landscape.svg'
import cloud from '../assets/cloud.svg'
import twinkle from '../assets/twinkle.svg'



export default function EveryMemoryMain(){
        return (
        /* 이번 주의 컨테이너, 하트 박스, 코멘트 클래스 공유 */
        <div>
            <div className={styles.mainContainer}>
                <p className={styles.weeklyTag}>
                    <img src={camera} alt='' className={styles.weeklyTagCamera}></img>
                    <span className={styles.weeklyTagText}>
                        오늘의 태그 #풍경{/* 태그 받기 */} - 이달의 인기 풍경{/* 태그 받기 */} 사진 갤러리
                    </span>
                </p>
                <div className={styles.forFlexTagBox}>
                    <div className={styles.tagBox}>
                        <img src={landscape} alt='' className={styles.tagBoxLandscape}></img>
                        <span className={styles.tagBoxText}>#풍경{/* 태그 받기 */}</span>
                    </div>
                </div>
                <div className={styles.forFlexweeklyTag1}>
                    <div className={styles.weeklyTagContainer}>
                        <div className={styles.weeklyTagImage}></div>
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>
                        <div className={styles.weeklyTagNthPlace}>1등:</div>
                        <div className={styles.weeklyTagAlbumName}>&nbsp;봄날의 벚꽃{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}>{/*사진 받기*/}</div>
                            <div className={styles.userEmail}>kdw061224{/*아이디 받기*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    999{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    999{/*댓글 갯수 받기*/}
                                </p>
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                    <div className={styles.weeklyTagContainer}>
                        <div className={styles.weeklyTagImage}></div>
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>
                        <div className={styles.weeklyTagNthPlace}>2등</div>
                        <div className={styles.weeklyTagAlbumName}>: 봄날의 벚꽃{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}>{/*사진 받기*/}</div>
                            <div className={styles.userEmail}>kdw061224{/*아이디 받기*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    999{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    999{/*댓글 갯수 받기*/}
                                </p>
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                    <div className={styles.weeklyTagContainer}>
                        <div className={styles.weeklyTagImage}></div>
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>
                        <div className={styles.weeklyTagNthPlace}>3등</div>
                        <div className={styles.weeklyTagAlbumName}>: 봄날의 벚꽃{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}>{/*사진 받기*/}</div>
                            <div className={styles.userEmail}>kdw061224{/*아이디 받기*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    999{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    999{/*댓글 갯수 받기*/}
                                </p>
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                </div>
                <div className={styles.todayTagTopContainer}>
                    <img src={twinkle} alt=''className={styles.twinkleIcon}></img>
                    <span className={styles.todayTag}>오늘의 태그 인기 사진</span>
                </div>
                <div className={styles.todayTagAllContainer}>
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            <div className={styles.forFlexTodayTag2}>
                                {/*여기 아이콘은 빼야 할 듯*/}
                                <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    999{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    999{/*댓글 갯수 받기*/}
                                </div>
                            </div>
                        </div>
                    </div>
                    {/*-------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            <div className={styles.forFlexTodayTag2}>
                                {/*여기 아이콘은 빼야 할 듯*/}
                                <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    999{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    999{/*댓글 갯수 받기*/}
                                </div>
                            </div>
                        </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            <div className={styles.forFlexTodayTag2}>
                                {/*여기 아이콘은 빼야 할 듯*/}
                                <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    999{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    999{/*댓글 갯수 받기*/}
                                </div>
                            </div>
                        </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            <div className={styles.forFlexTodayTag2}>
                                {/*여기 아이콘은 빼야 할 듯*/}
                                <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    999{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    999{/*댓글 갯수 받기*/}
                                </div>
                            </div>
                        </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            <div className={styles.forFlexTodayTag2}>
                                {/*여기 아이콘은 빼야 할 듯*/}
                                <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    999{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    999{/*댓글 갯수 받기*/}
                                </div>
                            </div>
                        </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            <div className={styles.forFlexTodayTag2}>
                                {/*여기 아이콘은 빼야 할 듯*/}
                                <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    999{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    999{/*댓글 갯수 받기*/}
                                </div>
                            </div>
                        </div>
                    </div>
                    {/*--------*/}
                </div>
                <div className={styles.forFlexButton}>
                    <img alt='' src={leftButton} className={styles.leftButton}></img>
                    <img alt='' src={num1} className={styles.num1Icon}></img>
                    <img alt='' src={num2} className={styles.num2Icon}></img>
                    <img alt='' src={num3} className={styles.num3Icon}></img>
                    <img alt='' src={num4} className={styles.num4Icon}></img>
                    <img alt='' src={rightButton} className={styles.rightButton}></img>
                </div>
                <div className={styles.postImageContainerOutter}>
                    <img src={camera} alt='' className={styles.postImageIconOutter}></img>
                    <span className={styles.postImageTextOutter}>사진 올리기</span>
                </div>
                <div className={styles.postImageContainerInner}>
                    <span className={styles.postImageTextInner}>
                        <img src={camera} alt='' className={styles.postImageIconInner}></img>
                        새로운 풍경 사진 업로드
                    </span>
                    <div className={styles.postImageToolContainer}>
                        <img src={cloud} alt='' className={styles.cloudIcon}></img>
                        <p className={styles.postImageToolText}>이곳을 클릭하거나 사진을 드래그하여 업로드하세요</p>
                        <p className={styles.ImageInfo}>지원 형식: JPG, PNG, HEIC / 최대 파일 크기: 20MB</p>
                    </div>
                    <p className={styles.postImageTitle}>제목</p>
                    <input className={styles.inputTitle} 
                    placeholder='예: 제주도 성산일출봉의 아름다운 일출'></input>
                    <p className={styles.postImageExplain}></p>
                    <textarea className={styles.inputExplain} 
                    placeholder='사진에 담긴 이야기나 촬영 시 느낀 감정을 자류롭게 작성해주세요.'></textarea>
                    <p className={styles.postImageLocation}>위치</p>
                    <input className={styles.inputLocation}
                    placeholder='예: 제주도특별자치도 서귀포시 성산읍'></input>
                    <div className={styles.forflexPostImage}>
                        <button className={styles.uploadImageButtonContainer}>
                            <img src={twinkle} alt='' className={styles.twinkleIcon2}></img>
                            <span className={styles.upLoadImageText}>사진 업로드하기</span>
                        </button>
                        <button className={styles.cancleButton}>취속하기</button>
                    </div>
                </div>
            </div>
        </div>
    )
}