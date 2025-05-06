import styles from './EveryMemoryMain.module.css'
import leftButton from '../assets/leftButton.svg'
import rightButton from '../assets/rightButton.svg'
import num1 from '../assets/num1.svg'
import num2 from '../assets/num2.svg'
import num3 from '../assets/num3.svg'
import num4 from '../assets/num4.svg'




export default function EveryMemoryMain(){
    
    return (
        /* 이번 주의 컨테이너, 하트 박스, 코멘트 클래스 공유 */
        //하트에서 부터 아이콘 svg 파일로 받기
        <div>
            <div className={styles.mainContainer}>
                <p className={styles.weeklyTag}>오늘의 태그 #{/* 태그 받기 */} - 이달의 인기 {/* 태그 받기 */} 사진 갤러리</p>
                <div className={styles.tagBox}>
                    {/*아이콘*/}
                    #{/* 태그 받기 */}
                </div>
                <div className={styles.forFlexweeklyTag1}>
                    <div className={styles.weeklyTagContainer}>
                        <span className={styles.weeklyTagImage}></span>
                        {/*아이콘*/}
                        <div className={styles.weeklyTagNthPlace}>1등:</div>
                        <div className={styles.weeklyTagAlbumName}>{/*앨범 이름 받기*/}</div>
                        <div className={styles.userImage}>{/*사진 받기*/}</div>
                        <div className={styles.userEmail}>{/*아이디 받기*/}</div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                    <div className={styles.weeklyTagContainer}>
                        <span className={styles.weeklyTagImage}></span>
                        {/*아이콘*/}
                        <div className={styles.weeklyTagNthPlace}>2등:</div>
                        <div className={styles.weeklyTagAlbumName}>{/*앨범 이름 받기*/}</div>
                        <div className={styles.userImage}>{/*사진 받기*/}</div>
                        <div className={styles.userEmail}>{/*아이디 받기*/}</div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                    <div className={styles.weeklyTagContainer}>
                        <span className={styles.weeklyTagImage}></span>
                        {/*아이콘*/}
                        <div className={styles.weeklyTagNthPlace}>3등:</div>
                        <div className={styles.weeklyTagAlbumName}>{/*앨범 이름 받기*/}</div>
                        <div className={styles.userImage}>{/*사진 받기*/}</div>
                        <div className={styles.userEmail}>{/*아이디 받기*/}</div>
                        <div className={styles.forFlexweeklyTag2}>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                        </div>
                    </div>
                    {/*------*/}
                </div>
                {/*아이콘 넣기*/}
                <p className={styles.todayTag}>오늘의 태그 인기 사진</p>
                <div className={styles.todayTagAllContainer}>
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            {/*여기 아이콘은 빼야 할 듯*/}
                            <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                            <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                        </div>
                        <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                    </div>
                    {/*-------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            {/*여기 아이콘은 빼야 할 듯*/}
                            <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                            <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                        </div>
                        <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            {/*여기 아이콘은 빼야 할 듯*/}
                            <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                            <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                        </div>
                        <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            {/*여기 아이콘은 빼야 할 듯*/}
                            <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                            <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                        </div>
                        <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            {/*여기 아이콘은 빼야 할 듯*/}
                            <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                            <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                        </div>
                        <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                    </div>
                    {/*--------*/}
                    <div className={styles.todayTagContainer}>
                        <span className={styles.todayTagImage}></span>
                        <div className={styles.forFlexTodayTag1}>
                            {/*여기 아이콘은 빼야 할 듯*/}
                            <span className={styles.todayTagImageName}>겨울 마을의 정경{/*앨범 제목 받아오기*/}</span>
                            <span className={styles.view}>조회수 3.2k{/*조회수 받기*/}</span>
                        </div>
                        <p className={styles.todayTagExplain}>전통 마을 전경{/*설명 받기*/}</p>
                            <div className={styles.heartContainer}>
                                {/*하트 아이콘 넣기*/}
                                999{/*하트 갯수 받기*/}
                            </div>
                            <div className={styles.commentContainer}>
                                {/*댓글 아이콘 넣기*/}
                                999{/*댓글 갯수 받기*/}
                            </div>
                    </div>
                    {/*--------*/}
                </div>
                <div className={styles.forFlexButton}>
                        <button className={styles.leftButton}>{leftButton}</button>
                        <button className={styles.num1Button}>{num1}</button>
                        <button className={styles.num2Button}>{num2}</button>
                        <button className={styles.num3Button}>{num3}</button>
                        <button className={styles.num4Button}>{num4}</button>
                        <button className={styles.rightButton}>{rightButton}</button>
                </div>
                <div className={styles.postImageContainerOutter}>
                    <div className={styles.postImageIconOutter}>{/*아이콘 넣기*/}</div>
                    <span className={styles.postImageTextOutter}>사진 올리기</span>
                </div>
                <div className={styles.postImageContainerInner}>
                    <span className={styles.postImageIconInner}>{/*아이콘 넣기*/}</span>
                    <span className={styles.postImageTextInner}>새로운 풍경 사진 업로드</span>
                    <div className={styles.postImageToolContainer}>
                        <div className={styles.cloudIcon}>{/*아이콘 넣기*/}</div>
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
                            <span className={styles.twinkleIcon}>{/*아이콘 넣기*/}</span>
                            <span className={styles.upLoadImageText}>사진 업로드하기</span>
                        </button>
                        <button className={styles.cancleButton}>취속하기</button>
                    </div>
                </div>
            </div>
        </div>
    )
}