import styles from "./MainPage.Main.module.css";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  faUsers,
  faLock,
  faTrophy,
  faHeart,
} from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";
import logo from "../../assets/photomory_logo.svg";
import image1 from "../../assets/mainPageImage1.svg";
import image2 from "../../assets/mainPageImage2.svg";
import image3 from "../../assets/mainPageImage3.svg";
import image4 from "../../assets/mainPageImage4.svg";
import DailyPopularTagModal from "../ourMemory/DailyPopularTagModal"; //일간은 아니지만 그냥 쓰는 거
import { useState, useEffect } from "react";

async function fetchUserEveryPosts(accessToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/every/posts`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`,
            },
        });

        if (!response.ok) {
            if (response.status===401) {
                throw new Error('Unauthorized'); //토큰 만료
            }
            throw new Error(`HTTP error! status: ${response.status}`); //http......
        }

        const posts = await response.json();
        return posts;
    } 
    catch (error) {
        console.error('Error fetching user every posts:', error);
        throw error;
    }
}

async function fetchUserOurAlbums(accessToken) {
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/our-album/album/1?page=0&size=10`,{
      method: 'GET',
      headers:{
        'Content_type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
    if(!response.ok){
      if(response.status==='401'){
        throw new Error('Unathorized')
      }
      throw new Error(`Http error! status: ${response.status}`)
    }
    const ourPost= await response.jsom();
    return ourPost;
  }
  catch (error){
    console.error('Error fetching user our posts')
    throw error;
  }
}

async function fetchUserMyAlbums(accessToken) {
  try{
    const response= await fetch(`${process.env.REACT_APP_API_URL}/api/my-albums/all`,{
      method: 'GET',
      headers:{
        'Content_type': 'application/json',
        'Authorization': `Bearer ${accessToken}`
      }
    })
    if(!response.ok){
      if(response.status==='401'){
        throw new Error('Unathorized')
      }
      throw new Error(`Http error! status: ${response.status}`)
    }
    const myPost= await response.jsom();
    return myPost;
  }
  catch (error){
    console.error('Error fetching user my posts')
    throw error;
  }
}

async function refreshAccessToken(refreshToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/refresh-token`, {
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
        const posts = await fetchUserEveryPosts(accessToken)
        const ourAlbums = await fetchUserOurAlbums(accessToken)
        const myAlbums = await fetchUserMyAlbums(accessToken)
        return {posts: posts, ourAlbums: ourAlbums, myAlbums:myAlbums}
    }
    catch (error){
        if (error.message === 'Unauthorized' && refreshToken) { //리프토큰 없으면 요청 안 되게게
            accessToken=await refreshAccessToken(refreshToken);
            if (accessToken) {
                localStorage.setItem('accessToken', accessToken);
                const posts = await fetchUserEveryPosts(accessToken);
                return posts
            }
        }
        console.log('Failed to fetch user posts')
        return null
    }
} //여까지 리프, 엑세 토큰 및 유저 포스트 가져오기 여기가 먼저 드가지니, 에브리에 포스트로 순위 매기는 건 여기서 처리하고 넘겨주는 게 좋을 듯

async function updateLikeCommentCount(postId){
    try{
        const accessToken= localStorage.getItem('accessToken')
        const response= await fetch(`${process.env.REACT_APP_API_URL}/api/every/posts`,{
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}`
            },
            body: JSON.stringify({postId})
        })
        if(!response.ok){
            if(response.status===401){
                throw new Error('Unauthorized')
            }
            throw new Error('Failed to upload count:' `${response.status}`)
        }
        return await response.json();
    }
    catch(error){
        console.error('Error updating count:', error)
        throw error;
    }
}

function MainPageMain() {
  const [posts, setPosts]= useState([])
  const [ourAlbums, setOurAlbums]= useState([])
  const [myAlbums, setMyAlbums]= useState([])
  const [error, setError]= useState();
  const [randomTagText, setRandomTagText]= useState()
  const [randomPosts, setRandomPosts]= useState([])
  const [isopen, setIsopen]= useState(false);
  const [imageForModal, setImageForModal]= useState('')
  console.log(error)

  const fetchPosts = async () => {
    try {
      const posts = await getUserPosts().posts;
      const ourAlbums = await getUserPosts().ourAlbums;
      const myAlbums = await getUserPosts().myAlbums;
      if (posts || ourAlbums || myAlbums) {
        const sortedPosts = [...posts].sort((a, b) => b.likes_count - a.likes_count);
        setPosts(sortedPosts); // 태그 상관 없이 좋아요 내림차순으로 posts 객체 정리
        setOurAlbums(ourAlbums)
        setMyAlbums(myAlbums)
      } else {
        setError('데이터를 불러오지 못했습니다.');
      }
    } catch (error) {
      console.log('Error in fetchPosts', error);
      setError('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.');
    }
  }; 

  useEffect(() => {
    fetchPosts();
  }, []);  //무한루프 때문에.

  useEffect(() => {
    if (posts.length > 0) {
      const allTag = [...new Set(posts.flatMap((post) => post.tags))]; //중복 없는 하나의 배열로 만들기
      if (allTag.length > 0) { //set은 생성자 함수, 하지만 일반 함수처럼 호출 불가. 따라서 new랑 짝궁=>set 객체 만들어짐=>[...new~]=>배열열
        const randomIndex = Math.floor(Math.random() * allTag.length);//0이상 allTag.length이하의 난수 생성
        setRandomTagText(allTag[randomIndex]);
        console.log('selected tag:', allTag[randomIndex]);
        const filteredPosts = posts.filter((post) => (post.tags || []).includes(allTag[randomIndex]));
        setRandomPosts(filteredPosts);//뭔가 posts말고 posts 좋아요 순서가 바뀐다면으로 하는 게 더 좋을 거 같은데..
      }
    }
  }, [posts]); //이거 에브리에서 받아오든 여기서 에브리가 받아 가든으로 고쳐야 한다.


  const handleLikeNum = async (postId) => {
    try {
      setPosts((prevPosts) => // 낙관적 업뎃
        prevPosts
          .map((post) =>
            post.postId === postId
              ? { ...post, likes_count: post.likes_count + 1 } // 이미 {}여기엔 속성이라 post.을 안 붙임
              : post
          )
          .sort((a, b) => b.likes_count - a.likes_count)
      );

      const updatedPostByLike = await updateLikeCommentCount(postId); // 서버 업뎃
      setPosts((prevPosts) =>
        prevPosts
          .map((post) =>
            post.postId === postId
              ? { ...post, likes_count: updatedPostByLike.likes_count }
              : post
          )
          .sort((a, b) => b.likes_count - a.likes_count)
      );
    } catch (error) {
      console.error('Error uploading like count', error);
    }
  };

  const weeklyPosts= randomPosts.slice(0,3); //아 여기선 먼저 useState([])에서[]로 됐다가 다시 비동기로 값을 받는다 usestate에서 useState() 그냥 이렇게 하면 비동기라서 이 코드가 먼저 실행될 떄 undefined가 떠서 타입 오류가 뜬다. slice는 undefined이면 오류가 뜬다. 따라서 []을 쓴다. 그 후 값이 들어온다.

  const nav = useNavigate();
  const onClickHandle = (event) => nav(event.currentTarget.dataset.value);

  const imageModalOpen = (e, post > { // 모달 안 뜨는 거 수정하기 일부러 오류 냄
    e.stopPropagation(); // 이벤트 전파 중단
    setIsopen(true);
    setImageForModal(post?.photoUrl || '');
  };

  const imageModalclose=()=>{
    setIsopen(false);
  }

  return (
    <div className={styles.mainContainer}>
      <img src={logo} alt="PHOTOMORY" className={styles.mainLogo}></img>
      <div
        className={styles.myMemoryContainer}
        onClick={onClickHandle}
        data-value="/my-album"
      >
        <FontAwesomeIcon icon={faLock} className={styles.icon} /> <br></br>
        <p className={styles.myMemoryText}>my memory</p>
        <p className={styles.myMemoryExplain}>
          나만 볼 수 있는 특별한 순간을 안전하게 보관하세요
        </p>
        <div className={styles.myMemoryImageContainer}>
          <img src={image1} alt="" className={styles.myMemoryImage1}
          onClick={(e) => imageModalOpen(e, myAlbums[0]?.myphotos[0])}></img>
          <img src={image2} alt="" className={styles.myMemoryImage2}
          onClick={(e) => imageModalOpen(e, myAlbums[1]?.myphotos[0])}></img>
        </div>
      </div>
      <div
        className={styles.ourMemoryContainer}
        onClick={onClickHandle}
        data-value="/our-album"
      >
        <FontAwesomeIcon icon={faUsers} className={styles.icon} /> <br></br>
        <p className={styles.ourMemoryText}>our memory</p>
        <p className={styles.ourMemoryExplain}>
          특별한 순간을 다른 사람들과 함께 나누고 소통하세요
        </p>
        <div className={styles.ourMemoryImageContainer}>
          <img 
            src={image3} 
            alt="" 
            className={styles.ourMemoryImage1}
            onClick={(e) => imageModalOpen(e, ourAlbums[0]?.posts[0])}
          ></img>
          <img 
            src={image4} 
            alt="" 
            className={styles.ourMemoryImage2}
            onClick={(e) => imageModalOpen(e, ourAlbums[0]?.posts[1])}
          ></img>
        </div>
      </div>
      <div className={styles.weeklyMemoryTitleContainer}>
        <div className={styles.weeklyMemoryTitleBox}>#이번_주의_추억</div>
        <div className={styles.weeklyMemoryTitleText}>
          #이번_주의_추억&nbsp;
          <span className={styles.weeklyMemoryTitleTextSmall}>
            태그의 인기 사진 TOP 3
            <FontAwesomeIcon icon={faTrophy} style={{ color: "#FFD43B" }} className={styles.weeklyMemoryTitleTrophy}/>
          </span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer1}>
        <img
          src={weeklyPosts[0]?.photo_url || ''}
          alt=""
          className={styles.weeklyMemoryImage1}
          onClick={(e) => imageModalOpen(e, weeklyPosts[0])}
        ></img>
        <div className={styles.weeklyMemoryImageText1}>
          <FontAwesomeIcon icon={faTrophy} style={{ color: "#FFD43B" }} className={styles.weeklyMemoryImageTrophy1}/>
          {randomTagText? randomTagText : '느낌 좋은 사진'} 부문 1등!! by @{weeklyPosts[0]?.user_name || ''}
        </div>
        <div className={styles.weeklyMemoryLikesContainer1}
        onClick={()=>{
          handleLikeNum(weeklyPosts[0]?.post_id || '')
        }}>
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes1}
          />
          &nbsp;
          <span className={styles.heartNum}>{weeklyPosts[0]?.likes_count || '1.4k'}</span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer2}>
        <img
          src={weeklyPosts[1]?.photo_url || ''}
          alt=""
          className={styles.weeklyMemoryImage2}
          onClick={(e) => imageModalOpen(e, weeklyPosts[1])}
        ></img>
        <div className={styles.weeklyMemoryImageText2}>
          <FontAwesomeIcon icon={faTrophy} style={{ color: "#C0C0C0" }} className={styles.weeklyMemoryImageTrophy2}/>
          {randomTagText? randomTagText : '느낌 좋은 사진'} 부문 2등!! by @{weeklyPosts[1]?.user_name || ''}
        </div>
        <div className={styles.weeklyMemoryLikesContainer2}
        onClick={()=>{
          handleLikeNum(weeklyPosts[1]?.post_id || '')
        }}>
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes2}
          />
          &nbsp;
          <span className={styles.heartNum}>{weeklyPosts[1]?.likes_count || '1.4k'}</span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer3}>
        <img
          src={weeklyPosts[2]?.photo_url || ''}
          alt=""
          className={styles.weeklyMemoryImage3}
          onClick={(e) => imageModalOpen(e, weeklyPosts[2])}
        ></img>
        <div className={styles.weeklyMemoryImageText3}>
          <FontAwesomeIcon icon={faTrophy} style={{ color: "#CD7F32" }} className={styles.weeklyMemoryImageTrophy2}/>
          {randomTagText? randomTagText : '느낌 좋은 사진'} 부문 3등!! by @id
        </div>
        <div className={styles.weeklyMemoryLikesContainer2}
        onClick={()=>{
          handleLikeNum(weeklyPosts[2]?.post_id || '')
        }}>
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes2}
          />
          &nbsp;
          <span className={styles.heartNum}>{weeklyPosts[2]?.likes_count || '1.4k'}</span>
        </div>
      </div>
      <div className={styles.forFlexMorePictureContainer}>
        <div className={styles.morePictureContainer}>
          <div
            className={styles.morePicture}
            onClick={onClickHandle}
            data-value="/everyMemory"> 
          <FontAwesomeIcon
            icon={faTrophy}
            style={{ color: "#FFD43B" }}
            className={styles.morePictureTrophy}
          />
            모두의 추억 인기 사진 보기
          </div>
        </div>
      </div>
      <DailyPopularTagModal
      isopen={isopen}
      onClose={imageModalclose}
      imageForModal={imageForModal}/>
    </div>
  ); 
}

export default MainPageMain;
