import styles from './MainPage.Main.module.css'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faUsers, faLock, faTrophy } from '@fortawesome/free-solid-svg-icons';

function MainPageMain(){
    
    return(
        <div className={styles.mainContainer}>
            <div className={styles.MainLogoContainer}>
                <div className={styles.logo}>
                    logo
                </div>
                <p className={styles.photomory}>
                    photomory 
                </p>
            </div>
            <div className={styles.myMemoryContainer}>
                <FontAwesomeIcon icon={faLock} className={styles.icon} /> <br></br>
                <p className={styles.myMemoryText}>나만의 추억</p>
                <p className={styles.myMemoryExplain}>나만 볼 수 있는 특별한 순간을 안전하게 보관하세요</p>
                <div className={styles.myMemoryImageContainer}>
                    <img src="" alt="" className={styles.myMemoryImage1}></img>
                    <img src="" alt="" className={styles.myMemoryImage2}></img>
                </div>
                
            </div>
            <div className={styles.ourMemoryContainer}>
                <FontAwesomeIcon icon={faUsers} className={styles.icon}/> <br></br>
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
                    #이번_주의_추억
                    <span 
                    className={styles.weeklyMemoryTitleText}>
                        태그의 인기 사진 TOP 3 <FontAwesomeIcon icon={faTrophy} style={{color: "#FFD43B",}} />
                    </span>
                </div>
            </div>
            <div className={styles.weeklyMemoryContainer1}>
                <img src="#" alt="" className={styles.weeklyMemoryImage1}></img>
                <div className={styles.weeklyMemoryImageText1}>
                    도시 야경 부문 1등!! by @id
                </div>
            </div>
            <div className={styles.weeklyMemoryContainer2}>
                <img src="#" alt="" className={styles.weeklyMemoryImage2}></img>
                <div className={styles.weeklyMemoryImageText2}>
                    도시 야경 부문 2등!! by @id
                </div>
            </div>
            <div className={styles.weeklyMemoryContainer3}>
                <img src="#" alt="" className={styles.weeklyMemoryImage3}></img>
                <div className={styles.weeklyMemoryImageText3}>
                    도시 야경 부문 3등!! by @id
                </div>
            </div>
            <div className={styles.morePicture}>
                morePicture
            </div>
        </div>
    )
}

export default MainPageMain