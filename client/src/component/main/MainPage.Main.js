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
import { useRandomIndex } from "../../contexts/RandomIndexContext";

async function fetchUserEveryPosts(retries = 0, maxRetries = 3) {
  let accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  try {
    const response = await fetch(
      `${process.env.REACT_APP_API_URL}/api/every/posts`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (!response.ok) {
      if (response.status === 401) {
        throw new Error("Unauthorized"); //토큰 만료
      }
      throw new Error(`HTTP error! status: ${response.status}`); //http......
    }

    const posts = await response.json();
    return posts;
  } catch (error) {
    if (
      error.message === "Unauthorized" &&
      refreshToken &&
      retries < maxRetries
    ) {
      //리프토큰 없으면 요청 안 되게게
      accessToken = await refreshAccessToken(refreshToken);
      if (accessToken) {
        const result = await fetchUserEveryPosts(retries + 1, maxRetries);
        return result;
      }
    }
    console.error("Failed to get post");
    return null;
  }
}

async function fetchUserOurAlbums(retries = 0, maxRetries = 3) {
  let accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  try {
    const response = await fetch(
      `${process.env.REACT_APP_API_URL}/api/our-album`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (!response.ok) {
      if (response.status === 401) {
        throw new Error("Unauthorized");
      }
      throw new Error(`Http error! status: ${response.status}`);
    }
    const ourPost = await response.json();
    return ourPost;
  } catch (error) {
    if (
      error.message === "Unauthorized" &&
      refreshToken &&
      retries < maxRetries
    ) {
      accessToken = await refreshAccessToken(refreshToken);
      if (accessToken) {
        const result = await fetchUserOurAlbums(retries + 1, maxRetries);
        return result;
      }
    }
    console.error("Failed to get ourAlbums");
    return [];
  }
}

async function fetchUserMyAlbums(retries = 0, maxRetries = 3) {
  let accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  try {
    const response = await fetch(
      `${process.env.REACT_APP_API_URL}/api/my-albums/all`,
      {
        method: "GET",
        headers: {
          Authorization: `Bearer ${accessToken}`,
        },
      }
    );
    if (!response.ok) {
      if (response.status === 401) {
        throw new Error("Unauthorized");
      }
      throw new Error(`Http error! status: ${response.status}`);
    }
    const myPost = await response.json();
    return myPost;
  } catch (error) {
    if (
      error.message === "Unauthorized" &&
      refreshToken &&
      retries < maxRetries
    ) {
      accessToken = await refreshAccessToken(refreshToken);
      if (accessToken) {
        const result = await fetchUserMyAlbums(retries + 1, maxRetries);
        return result;
      }
    }
    console.error("Failed to get myAlbums");
    return [];
  }
}

async function updateLikeCount(postId, retries = 0, maxRetries = 3) {
  let accessToken = localStorage.getItem("accessToken");
  const refreshToken = localStorage.getItem("refreshToken");
  try {
    const response = await fetch(
      `${process.env.REACT_APP_API_URL}/api/every/posts`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${accessToken}`,
        },
        body: JSON.stringify({ postId }),
      }
    );
    if (!response.ok) {
      if (response.status === 401) {
        throw new Error("Unauthorized");
      }
      throw new Error("Failed to upload count:"`${response.status}`);
    }
    return await response.json();
  } catch (error) {
    if (
      error.message === "Unauthorized" &&
      refreshToken &&
      retries < maxRetries
    ) {
      //리프토큰 없으면 요청 안 되게게
      accessToken = await refreshAccessToken(refreshToken);
      if (accessToken) {
        const result = await fetchUserEveryPosts(retries + 1, maxRetries);
        return result;
      }
    }
    console.error("Failed to get ourAlbums");
    return null;
  }
}
async function refreshAccessToken(refreshToken) {
  try {
    const response = await fetch(
      `${process.env.REACT_APP_API_URL}/refresh-token`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ refreshToken }),
      }
    );

    if (!response.ok) {
      throw new Error(`Token refredh failed status: ${response.status}`);
    }

    const data = await response.json();
    return data.accessToken;
  } catch (error) {
    console.error("Error fetching token:", error);
    return null;
  }
}

function MainPageMain() {
  const [posts, setPosts] = useState([]);
  const [ourAlbums, setOurAlbums] = useState([]);
  const [myAlbums, setMyAlbums] = useState([]);
  const [error, setError] = useState();
  const [randomTagText, setRandomTagText] = useState();
  const [randomPosts, setRandomPosts] = useState([]);
  const [isOpen, setIsOpen] = useState(false);
  const [imageForModal, setImageForModal] = useState("");
  const { randomIndex, updateRandomIndex } = useRandomIndex();
  console.log(error);

  const fetchPosts = async () => {
    try {
      const posts = await fetchUserEveryPosts();
      const ourAlbums = await fetchUserOurAlbums();
      const myAlbums = await fetchUserMyAlbums();

      if (posts) {
        const sortedPosts = [...posts].sort(
          (a, b) => b.likesCount - a.likesCount
        );
        setPosts(sortedPosts);
      }
      if (ourAlbums) {
        setOurAlbums(ourAlbums);
      }
      if (myAlbums) {
        setMyAlbums(myAlbums);
      }

      if (!posts && !ourAlbums && !myAlbums) {
        setError("데이터를 불러오지 못했습니다.");
      }
    } catch (error) {
      console.log("Error in fetchPosts", error);
      setError("서버 오류가 발생했습니다. 나중에 다시 시도해주세요.");
    }
  };

  useEffect(() => {
    fetchPosts();
  }, []); //무한루프 때문에.

  useEffect(() => {
    if (posts.length > 0) {
      const allTag = [...new Set(posts.flatMap((post) => post.tags))];
      if (allTag.length > 0) {
        if (randomIndex === null) {
          const newRandomIndex = Math.floor(Math.random() * allTag.length);
          updateRandomIndex(newRandomIndex);
        }
        setRandomTagText(allTag[randomIndex]);
        const filteredPosts = posts.filter((post) =>
          (post.tags || []).includes(allTag[randomIndex])
        );
        setRandomPosts(filteredPosts);
      }
    }
  }, [posts, randomIndex, updateRandomIndex]);

  const handleLikeNum = async (postId) => {
    //이거 islikecountup을 기준으로 크게 두 개로 나눠야 함
    const rollBackPosts = [...posts];
    try {
      setPosts(
        (
          prevPosts //낙관적 업뎃(하트 증가)
        ) =>
          prevPosts
            .map((post) =>
              post.postId === postId
                ? post.isLikeCountUp === false
                  ? {
                      ...post,
                      likesCount: post.likesCount + 1,
                      isLikeCountUp: !post.isLikeCountUp,
                    } //서버에서 어떤 값을 주는지 정해지면 또 수정하자..
                  : {
                      ...post,
                      likesCount: post.likesCount - 1,
                      isLikeCountUp: !post.isLikeCountUp,
                    }
                : post
            )
            .sort((a, b) => b.likesCount - a.likesCount)
      );
      await updateLikeCount(postId); //서버 업뎃
    } catch (error) {
      console.error("Error uploading like count", error);
      setPosts(rollBackPosts); //낙관적 업뎃 롤백
    }
  };

  const weeklyPosts = randomPosts.slice(0, 3); //아 여기선 먼저 useState([])에서[]로 됐다가 다시 비동기로 값을 받는다 usestate에서 useState() 그냥 이렇게 하면 비동기라서 이 코드가 먼저 실행될 떄 undefined가 떠서 타입 오류가 뜬다. slice는 undefined이면 오류가 뜬다. 따라서 []을 쓴다. 그 후 값이 들어온다.

  const nav = useNavigate();
  const onClickHandle = (event) => nav(event.currentTarget.dataset.value);

  const imageModalOpen = (e, post) => {
    setIsOpen(true);
    setImageForModal(post?.photoUrl || "");
    e.stopPropagation(); // 이벤트 전파 중단
  };

  const imageModalclose = () => {
    setIsOpen(false);
  };

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
          <img
            src={myAlbums?.[0]?.myphotos?.[0]?.myphotoUrl || image1}
            alt=""
            className={styles.myMemoryImage1} //이미지도 가꼬 와야 한다.
            onClick={(e) =>
              imageModalOpen(e, myAlbums?.[0]?.myphotos?.[0]?.myphotoUrl || "")
            }
          />
          <img
            src={myAlbums?.[1]?.myphotos?.[0]?.myphotoUrl || image2}
            alt=""
            className={styles.myMemoryImage2}
            onClick={(e) =>
              imageModalOpen(e, myAlbums?.[1]?.myphotos?.[0]?.myphotoUrl || "")
            }
          />
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
            src={ourAlbums?.[0]?.albums?.[0]?.photos?.[0] || image3} 
            alt="" 
            className={styles.ourMemoryImage1}
            onClick={(e) => imageModalOpen(e, ourAlbums?.[0]?.albums?.[0]?.photos?.[0] || '')}
          />
          <img 
            src={ourAlbums?.[0]?.albums?.[0]?.photos?.[1] || image3} 
            alt="" 
            className={styles.ourMemoryImage1}
            onClick={(e) => imageModalOpen(e, ourAlbums?.[0]?.albums?.[0]?.photos?.[1] || '')}
          />
        </div>
      </div>
      <div className={styles.weeklyMemoryTitleContainer}>
        <div className={styles.weeklyMemoryTitleBox}>#이번_주의_추억</div>
        <div className={styles.weeklyMemoryTitleText}>
          #이번_주의_추억&nbsp;
          <span className={styles.weeklyMemoryTitleTextSmall}>
            태그의 인기 사진 TOP 3
            <FontAwesomeIcon
              icon={faTrophy}
              style={{ color: "#FFD43B" }}
              className={styles.weeklyMemoryTitleTrophy}
            />
          </span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer1}>
        <img
          src={weeklyPosts[0]?.photoUrl || ""}
          alt=""
          className={styles.weeklyMemoryImage1}
          onClick={(e) => imageModalOpen(e, weeklyPosts[0])}
        ></img>
        <div className={styles.weeklyMemoryImageText1}>
          <FontAwesomeIcon
            icon={faTrophy}
            style={{ color: "#FFD43B" }}
            className={styles.weeklyMemoryImageTrophy1}
          />
          {randomTagText ? randomTagText : "느낌 좋은 사진"} 부문 1등!! by @
          {weeklyPosts[0]?.userName || ""}
        </div>
        <div
          className={styles.weeklyMemoryLikesContainer1}
          onClick={() => {
            handleLikeNum(weeklyPosts[0]?.postId || "");
          }}
        >
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes1}
          />
          &nbsp;
          <span className={styles.heartNum}>
            {weeklyPosts[0]?.likesCount || "1.4k"}
          </span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer2}>
        <img
          src={weeklyPosts[1]?.photoUrl || ""}
          alt=""
          className={styles.weeklyMemoryImage2}
          onClick={(e) => imageModalOpen(e, weeklyPosts[1])}
        ></img>
        <div className={styles.weeklyMemoryImageText2}>
          <FontAwesomeIcon
            icon={faTrophy}
            style={{ color: "#C0C0C0" }}
            className={styles.weeklyMemoryImageTrophy2}
          />
          {randomTagText ? randomTagText : "느낌 좋은 사진"} 부문 2등!! by @
          {weeklyPosts[1]?.userName || ""}
        </div>
        <div
          className={styles.weeklyMemoryLikesContainer2}
          onClick={() => {
            handleLikeNum(weeklyPosts[1]?.postId || "");
          }}
        >
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes2}
          />
          &nbsp;
          <span className={styles.heartNum}>
            {weeklyPosts[1]?.likesCount || "1.4k"}
          </span>
        </div>
      </div>
      <div className={styles.weeklyMemoryContainer3}>
        <img
          src={weeklyPosts[2]?.photoUrl || ""}
          alt=""
          className={styles.weeklyMemoryImage3}
          onClick={(e) => imageModalOpen(e, weeklyPosts[2])}
        ></img>
        <div className={styles.weeklyMemoryImageText3}>
          <FontAwesomeIcon
            icon={faTrophy}
            style={{ color: "#CD7F32" }}
            className={styles.weeklyMemoryImageTrophy2}
          />
          {randomTagText ? randomTagText : "느낌 좋은 사진"} 부문 3등!! by @
          {weeklyPosts[2]?.userName || ""}
        </div>
        <div
          className={styles.weeklyMemoryLikesContainer2}
          onClick={() => {
            handleLikeNum(weeklyPosts[2]?.postId || "");
          }}
        >
          <FontAwesomeIcon
            icon={faHeart}
            style={{ color: "#ff4646" }}
            className={styles.weeklyMemoryLikes2}
          />
          &nbsp;
          <span className={styles.heartNum}>
            {weeklyPosts[2]?.likesCount || "1.4k"}
          </span>
        </div>
      </div>
      <div className={styles.forFlexMorePictureContainer}>
        <div className={styles.morePictureContainer}>
          <div
            className={styles.morePicture}
            onClick={onClickHandle}
            data-value="/everyMemory"
          >
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
        isOpen={isOpen}
        onClose={imageModalclose}
        imageForModal={imageForModal}
      />
    </div>
  );
}

export default MainPageMain;
