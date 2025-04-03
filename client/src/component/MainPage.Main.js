import styles from './MainPage.Main.module.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUsers, faLock, faTrophy, faHeart } from '@fortawesome/free-solid-svg-icons';
import { useNavigate } from 'react-router-dom';
import { useState } from 'react';


function MainPageMain() {
    const nav = useNavigate();
    const [isModalOpen, setIsModalOpen]= useState(false);
    const onClickHandle=(event)=>{
        nav(event.target.value);
    };
    return (
        <div className={styles.mainContainer}>
            <div className={styles.MainLogoContainer}>
                <div className={styles.logo}>
                    logo
                </div>
                <p className={styles.photomory}>
                    photomory
                </p>
            </div>
            <div className={styles.myMemoryContainer}
            onClick={onClickHandle}
            value='/myMemory'>
                <FontAwesomeIcon icon={faLock} className={styles.icon} /> <br></br>
                <p className={styles.myMemoryText}>나만의 추억</p>
                <p className={styles.myMemoryExplain}>나만 볼 수 있는 특별한 순간을 안전하게 보관하세요</p>
                <div className={styles.myMemoryImageContainer}>
                    <img src="" alt="" className={styles.myMemoryImage1}></img>
                    <img src="" alt="" className={styles.myMemoryImage2}></img>
                </div>
            </div>
            <div className={styles.ourMemoryContainer}
            onClick={onClickHandle}
            value='/ourMemory'>
                <FontAwesomeIcon icon={faUsers} className={styles.icon} /> <br></br>
                <p className={styles.ourMemoryText}>우리의 추억</p>
                <p className={styles.ourMemoryExplain}>특별한 순간을 다른 사람들과 함께 나누고 소통하세요</p>
                <div className={styles.ourMemoryImageContainer}>
                    <img src="" alt="" className={styles.ourMemoryImage1}></img>
                    <img src="" alt="" className={styles.ourMemoryImage2}></img>
                </div>
            </div>
            <div className={styles.weeklyMemoryTitleContainer}>
                <div className={styles.weeklyMemoryTitleBox}>#이번_주의_추억</div>
                <div className={styles.weeklyMemoryTitleText}>
                    #이번_주의_추억&nbsp;
                    <span
                        className={styles.weeklyMemoryTitleTextSmall}>
                        태그의 인기 사진 TOP 3 <FontAwesomeIcon icon={faTrophy} style={{ color: "#FFD43B" }} />
                    </span>
                </div>
            </div>
            <div className={styles.weeklyMemoryContainer1}
            onClick={()=>setIsModalOpen(true)}
            >{/*모달 사용 예정*/}
                <img src="#" alt="" className={styles.weeklyMemoryImage1}></img>
                <div className={styles.weeklyMemoryImageText1}>
                    도시 야경 부문 1등!! by @id
                </div>
                <div className={styles.weeklyMemoryLikesContainer1}>
                    <FontAwesomeIcon icon={faHeart}
                        style={{ color: "#ff4646", }}
                        className={styles.weeklyMemoryLikes1} />
                    &nbsp;
                    <span>
                        9999
                    </span>
                </div>
            </div>
            <div className={styles.weeklyMemoryContainer2}
            > {/*모달 사용 예정*/}
                <img src="#" alt="" className={styles.weeklyMemoryImage2}></img>
                <div className={styles.weeklyMemoryImageText2}>
                    도시 야경 부문 2등!! by @id
                </div>
                <div className={styles.weeklyMemoryLikesContainer2}>
                    <FontAwesomeIcon icon={faHeart}
                        style={{ color: "#ff4646", }}
                        className={styles.weeklyMemoryLikes2} />
                    &nbsp;
                    <span>
                        9999
                    </span>
                </div>
            </div>
            <div className={styles.weeklyMemoryContainer3}
            >{/*모달 사용 예정*/}
                <img src="#" alt="" className={styles.weeklyMemoryImage3}></img>
                <div className={styles.weeklyMemoryImageText3}>
                    도시 야경 부문 3등!! by @id
                </div>
                <div className={styles.weeklyMemoryLikesContainer2}>
                    <FontAwesomeIcon icon={faHeart}
                        style={{ color: "#ff4646", }}
                        className={styles.weeklyMemoryLikes2} />
                    &nbsp;
                    <span>
                        9999
                    </span>
                </div>
            </div>
            <div className={styles.morePictureContainer}>
                <div className={styles.morePicture}
                onClick={onClickHandle}
                value='/'>
                    morePicture
                </div>
            </div>
        </div>
    );
};

export default MainPageMain;