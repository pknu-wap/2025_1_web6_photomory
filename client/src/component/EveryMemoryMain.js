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
    const [posts, setPosts] = useState([]) //모든 태그의 포스트 중 좋아요 순을 위한
    const [error, setError] = useState();
    const [randomTagText, setRandomTagText] = useState();
    const [randomPosts, setRandomPosts]= useState(['oh god!']); //랜덤 태그에 해당하는 포스트 중 좋아요 순을 위한
                                                    // 지금은 undefined가 뜨기에 일단 해둠
    useEffect(()=>{
        async function fetchPosts(){
            try{
                const posts = await getUserPosts();
                if (posts && Array.isArray(posts)) {
                    const sortedPosts = posts.sort((a,b)=>b.likes_count-a.likes_count);
                    setPosts(sortedPosts); // 태그 상관 없이 좋아요 내림차순으로 posts 객체 정리
                    const allTag=[...new Set(posts.flatMap((post)=>post.tags))] //중복 없는 하나의 배열로 만들기
                    if (allTag.length>0) {
                        const randomIndex = Math.floor(Math.random()*allTag.length); //0이상 allTag.length이하의 난수 생성
                        setRandomTagText(allTag[randomIndex])
                        console.log('selected tag:', allTag[randomIndex])
                        const filteredPosts= sortedPosts.filter((post)=>(post.tags || []).includes(allTag[randomIndex]));
                        setRandomPosts(filteredPosts);
                    }
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
    }, []);
    const weeklyPosts= randomPosts.slice(0,3); //아 여기선 먼저 useState([])에서[]로 됐다가 다시 비동기로 값을 받는다 usestate에서 useState() 그냥 이렇게 하면 비동기라서 이 코드가 먼저 실행될 떄 undefined가 떠서 타입 오류가 뜬다. slice는 undefined이면 오류가 뜬다. 따라서 []을 쓴다. 그 후 값이 들어온다.
    console.log(weeklyPosts)

    const dailyPosts = posts.filter(
        (post)=>!post.post_id=== weeklyPosts.post_id
    )



        return (
        <div>
            <div className={styles.mainContainer}>
                {error && <p className={styles.error}>{error}</p>}
                <p className={styles.weeklyTag}>
                    <img src={camera} alt='' className={styles.weeklyTagCamera}></img>
                    <span className={styles.weeklyTagText}>
                        오늘의 태그 #{randomTagText} - 주간 인기 {randomTagText} 사진 갤러리
                    </span>
                </p>
                <div className={styles.forFlexTagBox}>
                    <div className={styles.tagBox}>
                        <img src={landscape} alt='' className={styles.tagBoxLandscape}></img>
                        <span className={styles.tagBoxText}>#{randomTagText}</span>
                    </div>
                </div>
                <div className={styles.forFlexweeklyTag1}>
                    <div className={styles.weeklyTagContainer}>
                        <div className={styles.weeklyTagImage}
                        style={{backgroundImage:`url(${weeklyPosts[0].photo_url})`}}></div>
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>
                        <div className={styles.weeklyTagNthPlace}>1등:</div>
                        <div className={styles.weeklyTagAlbumName}>&nbsp;{weeklyPosts[0].post_text}{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}
                            style={{backgroundImage:`url(${weeklyPosts[0].user_photourl})`}}>{/*사진 받기*/}</div>
                            <div className={styles.userName}>{weeklyPosts[0].user_name}{/*이름 받기? 아이딘지 이메일인지 잘 모르겠음*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    {weeklyPosts[0].likes_count}{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    {weeklyPosts[0].comments_count}{/*댓글 갯수 받기*/}
                                </p>
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                    <div className={styles.weeklyTagContainer}>
                        <div className={styles.weeklyTagImage}
                        style={{backgroundImage:`url(${weeklyPosts[1].photo_url})`}}></div>
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>
                        <div className={styles.weeklyTagNthPlace}>2등:</div>
                        <div className={styles.weeklyTagAlbumName}>&nbsp;{weeklyPosts[1].post_text}{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}
                            style={{backgroundImage:`url(${weeklyPosts[1].user_photourl})`}}>{/*사진 받기*/}</div>
                            <div className={styles.userName}>{weeklyPosts[1].user_name}{/*이름 받기? 아이딘지 이메일인지 잘 모르겠음*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    {weeklyPosts[1].likes_count}{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    {weeklyPosts[1].comments_count}{/*댓글 갯수 받기*/}
                                </p>
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                    <div className={styles.weeklyTagContainer}>
                        <div className={styles.weeklyTagImage}
                        style={{backgroundImage:`url(${weeklyPosts[2].photo_url})`}}></div>
                        <img src={trophy} alt='' className={styles.trophyIcon}></img>
                        <div className={styles.weeklyTagNthPlace}>3등:</div>
                        <div className={styles.weeklyTagAlbumName}>&nbsp;{weeklyPosts[2].post_text}{/*앨범 이름 받기*/}</div>
                        <div className={styles.forFlexUserInfo}>
                            <div className={styles.userImage}
                            style={{backgroundImage:`url(${weeklyPosts[2].user_photourl})`}}>{/*사진 받기*/}</div>
                            <div className={styles.userName}>{weeklyPosts[2].user_name}{/*이름 받기? 아이딘지 이메일인지 잘 모르겠음*/}</div>
                        </div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                <img src={heart} alt='' className={styles.heartIcon}></img>
                                <p className={styles.heartNum}>
                                    {weeklyPosts[2].likes_count}{/*하트 갯수 받기*/}
                                </p>
                            </div>
                            <div className={styles.commentContainer}>
                                <img src={comment} alt='' className={styles.commentIcon}></img>
                                <p className={styles.commentNum}>
                                    {weeklyPosts[2].comments_count}{/*댓글 갯수 받기*/}
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
                                <span className={styles.todayTagImageName}>{dailyPosts.post_text}{/*앨범 제목 받아오기*/}</span>
                                <span className={styles.view}>{dailyPosts.}{/*조회수 받기*/}</span>
                            </div>
                            <p className={styles.todayTagExplain}>{dailyPosts.post_description}{/*설명 받기*/}</p>
                            <div className={styles.forFlexTodayTag3}>
                                <div className={styles.heartContainer}>
                                    <img src={heart} alt='' className={styles.todayTagHeartIcon}></img>
                                    {dailyPosts[0].likes_count}{/*하트 갯수 받기*/}
                                </div>
                                <div className={styles.commentContainer}>
                                    <img src={comment} alt='' className={styles.todayTagCommentIcon}></img>
                                    {dailyPosts.comments_count}{/*댓글 갯수 받기*/}
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