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
import { useState, useEffect } from "react";

async function fetchUserposts(accessToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/user/posts`, {
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
} //여까지 리프, 엑세 토큰 및 유저 포스트 가져오기 여기가 먼저 드가지니, 에브리에 포스트로 순위 매기는 건 여기서 처리하고 넘겨주는 게 좋을 듯

async function updateLikeCommentCount(post_id){
    try{
        const accessToken= localStorage.getItem('accessToken')
        const response= await fetch(`${process.env.REACT_APP_API_URL}/posts`,{/* 이거 엔드포인트 뭐임..?*/
            method: 'POST',
            headers:{
                'Content-Type': 'application/json',
                Authorization: `Bearer ${accessToken}`
            },
            body: JSON.stringify({post_id})
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
  const [error, setError]= useState();
  const [randomTagText, setRandomTagText]= useState()
  const [randomPosts, setRandomPosts]= useState([])


  const fetchPosts = async () => {
    try {
      const posts = await getUserPosts();
      if (posts && Array.isArray(posts)) {
        const sortedPosts = [...posts].sort((a, b) => b.likes_count - a.likes_count);
        setPosts(sortedPosts); // 태그 상관 없이 좋아요 내림차순으로 posts 객체 정리
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
  }, [posts]);

  const handleLikeClick = async (post_id) => {
    try {
      setPosts((prevPosts) => // 낙관적 업뎃
        prevPosts
          .map((post) =>
            post.post_id === post_id
              ? { ...post, likes_count: post.likes_count + 1 } // 이미 {}여기엔 속성이라 post.을 안 붙임
              : post
          )
          .sort((a, b) => b.likes_count - a.likes_count)
      );

      const updatedPostByLike = await updateLikeCommentCount(post_id); // 서버 업뎃
      setPosts((prevPosts) =>
        prevPosts
          .map((post) =>
            post.post_id === post_id
              ? { ...post, likes_count: updatedPostByLike.likes_count }
              : post
          )
          .sort((a, b) => b.likes_count - a.likes_count)
      );
    } catch (error) {
      console.error('Error uploading like count', error);
    }
  };

  const handleCommentClick = async (post_id) => {
    try {
      setPosts((prevPosts) => // 낙관적 업뎃
        prevPosts.map((post) =>
          post.post_id === post_id
            ? { ...post, comments_count: post.comments_count + 1 }
            : post
        )
      );

      const updatedPostByComment = await updateLikeCommentCount(post_id); // 서버 업뎃
      setPosts((prevPosts) =>
        prevPosts.map((post) =>
          post.post_id === post_id
            ? { ...post, comments_count: updatedPostByComment.comments_count }
            : post
        )
      );
    } catch (error) {
      console.error('Error uploading like count', error);
    }
  };

    const weeklyPosts= randomPosts.slice(0,3); //아 여기선 먼저 useState([])에서[]로 됐다가 다시 비동기로 값을 받는다 usestate에서 useState() 그냥 이렇게 하면 비동기라서 이 코드가 먼저 실행될 떄 undefined가 떠서 타입 오류가 뜬다. slice는 undefined이면 오류가 뜬다. 따라서 []을 쓴다. 그 후 값이 들어온다.

  const nav = useNavigate();
  const onClickHandle = (event) => nav(event.currentTarget.dataset.value);

  const weeklyImage = "./Image.png";

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
          <img src={image1} alt="" className={styles.myMemoryImage1}></img>
          <img src={image2} alt="" className={styles.myMemoryImage2}></img>
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
          <img src={image3} alt="" className={styles.ourMemoryImage1}></img>
          <img src={image4} alt="" className={styles.ourMemoryImage2}></img>
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
      <div className={styles.weeklyMemoryContainer1} onClick={onClickHandle}>
        <img
          src={weeklyImage}
          alt=""
          className={styles.weeklyMemoryImage1}
        ></img>
        <div className={styles.weeklyMemoryImageText1}>
          <FontAwesomeIcon icon={faTrophy} style={{ color: "#FFD43B" }} className={styles.weeklyMemoryImageTrophy1}/>
          {randomTagText? randomTagText : '느낌 좋은 사진'} 부문 1등!! by @id
        </div>
        <div className={styles.weeklyMemoryLikesContainer1}>
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes1}
          />
          &nbsp;
          <span>{weeklyPosts[0]?.likes_count || '1.4k'}</span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer2} onClick={onClickHandle}>
        <img
          src={weeklyImage}
          alt=""
          className={styles.weeklyMemoryImage2}
        ></img>
        <div className={styles.weeklyMemoryImageText2}>
          <FontAwesomeIcon icon={faTrophy} style={{ color: "#C0C0C0" }} className={styles.weeklyMemoryImageTrophy2}/>
          {randomTagText? randomTagText : '느낌 좋은 사진'} 부문 2등!! by @id
        </div>
        <div className={styles.weeklyMemoryLikesContainer2}>
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes2}
          />
          &nbsp;
          <span>{weeklyPosts[1]?.likes_count || '1.4k'}</span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer3} onClick={onClickHandle}>
        <img
          src={weeklyImage}
          alt=""
          className={styles.weeklyMemoryImage3}
        ></img>
        <div className={styles.weeklyMemoryImageText3}>
          <FontAwesomeIcon icon={faTrophy} style={{ color: "#CD7F32" }} className={styles.weeklyMemoryImageTrophy2}/>
          {randomTagText? randomTagText : '느낌 좋은 사진'} 부문 3등!! by @id
        </div>
        <div className={styles.weeklyMemoryLikesContainer2}>
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes2}
          />
          &nbsp;
          <span>{weeklyPosts[2]?.likes_count || '1.4k'}</span>
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
    </div>
  );
}

export default MainPageMain;
