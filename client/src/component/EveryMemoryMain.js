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
import { useState, useEffect } from 'react'

async function fetchUserposts(accessToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_BASE_URL}/user/posts`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`,
            },
        });

        if (!response.ok) {
            if (response.status===401) {
                throw new Error('Unauthorized'); //토큰 말료
            }
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const posts = await response.json();
        return posts;
    } catch (error) {
        console.error('Error fetching user posts:', error);
        throw error;
    }
}

async function refreshAccessToken(refreshToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_BASE_URL}/refresh-token`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({refreshToken})
        });

        if (!response.ok) {
            throw new Error(`Token refredh failed status: ${response.status}`);
        }

        const data = await response.json();
        return data.accessToken;
    }
    catch (error) {
        console.error('Error fetching token:', error);
        return null;
    }
}

async function getUserPosts() {
    let accessToken= localStorage.getItem('accessToken');
    const refreshToken= localStorage.getItem('refreshToken');
    try{
        const posts = await fetchUserposts(accessToken)
        return posts
    }
    catch (error){
        if (error.message === 'Unauthorized' && refreshToken) { //리프토큰 없으면 요청 안 되게게
            accessToken=await refreshAccessToken(refreshToken);
            if (accessToken) {
                localStorage.setItem('accessToken', accessToken);
                const posts = await fetchUserposts(accessToken);
                return posts
            }
        }
        console.log('Failed to fetch user posts')
        return null
    }
}


export default function EveryMemoryMain(){
    const [posts, setPosts] = useState([])
    const [error, setError] = useState();

    useEffect(()=>{ //근데 여기선 try를 안 써도 되나?
        async function fetchPosts(){
            try{
                const posts = await getUserPosts();
                if (posts) {
                    setPosts(posts);
                }
                else{
                    setError('데이터를 불러오지 못했습니다.')
                }
            }
            catch (error){
                console.error('Error in fetchPosts', error)
                setError('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.')
            }
        }
        fetchPosts();
    }, [])

        return (
        <div>
            <div className={styles.mainContainer}>
                {error && <p className={styles.error}>{error}</p>}
                <p className={styles.weeklyTag}>
                    <img src={camera} alt='' className={styles.weeklyTagCamera}></img>
                    <span className={styles.weeklyTagText}>
                        오늘의 태그 #{}{/* 태그 받기 */} - 이달의 인기 풍경{/* 태그 받기 */} 사진 갤러리
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
                        <div className={styles.weeklyTagImage}
                        style={{backgroundImage:`url(${posts.photo_url}})`}}></div> {/*여기서 벡엔드가 좋아요 수에 따라 처리하고 순서 대로 보내주나? 그럼 이건 [0]처럼 순서로 선택하면 되는데 뭔가 말을 맞춰야 함. */}
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>{/*그리고 목데이터에서 처음 저 사진도 배열이겠지?*/}
                        <div className={styles.weeklyTagNthPlace}>1등:</div>
                        <div className={styles.weeklyTagAlbumName}>&nbsp;{posts.post_text}{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}>{posts.user_image}{/*사진 받기*/}</div>
                            <div className={styles.userEmail}>{posts.user_id}{/*아이디 받기? 아이딘지 이메일인지 잘 모르겠음*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    {posts.likes_count}{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    {posts.comments_count}{/*댓글 갯수 받기*/}
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
                    <span className={styles.postImageTextOutter}>#사진 올리기</span>
                </div>
                <div className={styles.postImageContainerInner}>
                    <img src={camera} alt='' className={styles.postImageIconInner}></img>
                    <span className={styles.postImageTextInner}>새로운 풍경 사진 업로드</span>
                    <div className={styles.postImageToolContainer}>
                        <img src={cloud} alt='' className={styles.cloudIcon}></img>
                        <p className={styles.postImageToolText}>이곳을 클릭하거나 사진을 드래그하여 업로드하세요.</p>
                        <p className={styles.ImageInfo}>지원 형식: JPG, PNG, HEIC / 최대 파일 크기: 20MB</p>
                    </div>
                    <p className={styles.postImageTitle}>제목</p>
                    <input className={styles.inputTitle} 
                    placeholder='예: 제주도 성산일출봉의 아름다운 일출'></input>
                    <p className={styles.postImageExplain}>설명</p>
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
                        <button className={styles.cancleButton}>취소하기</button>
                    </div>
                </div>
            </div>
        </div>
    )
}