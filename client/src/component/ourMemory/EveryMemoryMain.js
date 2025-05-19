import styles from "./EveryMemoryMain.module.css";
import leftButton from "../../assets/leftButton.svg";
import rightButton from "../../assets/rightButton.svg";
import num1 from "../../assets/num1.svg";
import num2 from "../../assets/num2.svg";
import num3 from "../../assets/num3.svg";
import num4 from "../../assets/num4.svg";
import camera from "../../assets/camera.svg";
import landscape from "../../assets/landscape.svg";
import cloud from "../../assets/cloud.svg";
import twinkle from "../../assets/twinkle.svg";
import WeeklyPopularTag from "./WeeklyPopularTag.js";
import DailyPopularTag from "./DailyPopularTag.js";
import DailyPopularTagModal from "./DailyPopularTagModal";
import { useState, useEffect, useMemo, useRef } from "react";

async function fetchUserposts(accessToken) {
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
        const response = await fetch(`${process.env.REACT_APP_API_URL}/refresh-token`, { //이건 벡엔드에서 추후 변경 예정
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

async function updateLikeCommentCount(postId){
    try{
        const accessToken= localStorage.getItem('accessToken')
        const response= await fetch(`${process.env.REACT_APP_API_URL}/api/every/posts`,{/* 이거 엔드포인트 뭐임..?*/
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

async function uploadingImage(uploadImage) {
    try {
        if (!uploadImage || !uploadImage.images || uploadImage.images.length === 0) {
            throw new Error('이미지를 선택해주세요.');
        }

        const accessToken = localStorage.getItem('accessToken');
        if (!accessToken) {
            throw new Error('로그인이 필요합니다.');
        }
        if (
            uploadImage.postText==='' ||
            uploadImage.postDescription==='' ||
            uploadImage.postLocation==='' ||
            uploadImage.postTag==='' || false
        ) {
            throw new Error('사진의 정보를 완성해주세요.')
        }
        const formData = new FormData();
        // 이미지 데이터를 FormData에 추가
        uploadImage.images.forEach((image, index) => { //base64는 data:image/jpeg;base64,실제데이터 이렇게 생김
            // Base64 이미지를 Blob으로 변환
            const byteString = atob(image.split(',')[1]); //실제 데이터를 디코딩(?)한다(바이너리 문자열{바이트 문자열})
            const mimeString = image.split(',')[0].split(':')[1].split(';')[0];//image/jpeg 타입 저장, 이는 나중에 blob만들 때 필요.
            const ab = new ArrayBuffer(byteString.length);// ArrayBuffer은 byteString.length 만큼의 크기로 바이너리 데이터를 저장할 수 있는 메모리 공간 
            const ia = new Uint8Array(ab); //Uint8Array는 ArrayBuffer를 8비트(0~255) 배열로 다룰 수 있게 해준다.
            for (let i = 0; i < byteString.length; i++) {
                ia[i] = byteString.charCodeAt(i); //charCodeAt 유니코드
            }
            const blob = new Blob([ab], { type: mimeString });
            formData.append('photoUrl', blob, `image${index}.${mimeString.split('/')[1]}`);
        });

        // 다른 데이터 추가
        formData.append('postText', uploadImage.postText || '');
        formData.append('postDescription', uploadImage.postDescription || '');
        formData.append('postLocation', uploadImage.postLocation || '');
        formData.append('postTag', uploadImage.postTag || '');

        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/images/upload`, {
            method: 'POST',
            headers:{
                'Authorization': `Bearer ${accessToken}`
            },
            body: formData
        });
        if(!response.ok){
            if (response.status===401) {
                throw new Error('Unauthorized')
            }
            const errorText = await response.text();
            throw new Error(`이미지 업로드 실패: ${response.status} - ${errorText}`);
        }

        const result = await response.json();
        alert('이미지가 성공적으로 업로드되었습니다.');
        return result;
    } catch (error) {
        console.error('Error uploading image:', error);
        alert(error.message);
    }
}

export default function EveryMemoryMain(){
    const [posts, setPosts] = useState([]) //모든 태그의 포스트 중 좋아요 순을 위한
    const [error, setError] = useState();
    const [randomTagText, setRandomTagText] = useState();
    const [randomPosts, setRandomPosts]= useState([]); //랜덤 태그에 해당하는 포스트 중 좋아요 순을 위한
    // 지금은 undefined가 뜨기에 일단 해둠
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedPost, setSelectedPost] = useState(null);
    const [uploadImage, setUploadImage]= useState(null);

    const fetchPosts= async ()=>{
        try{
            const posts= await getUserPosts();
            if (posts && Array.isArray(posts)) {
                const sortedPosts = [...posts].sort((a,b)=>b.likesCount-a.likesCount);
                setPosts(sortedPosts); // 태그 상관 없이 좋아요 내림차순으로 posts 객체 정리
            }
            else{
                setError('데이터를 불러오지 못했습니다.')
            }
        }
        catch(error){
            console.log('Error in fetchPosts', error)
            setError('서버 오류가 발생했습니다. 나중에 다시 시도해주세요.')
        }
    }

    useEffect(()=>{
        fetchPosts();
    },[]); //무한루프 때문에.

    useEffect(()=>{
        if (posts.length>0) {
            const allTag=[...new Set(posts.flatMap((post)=>post.tags))] //중복 없는 하나의 배열로 만들기
            if (allTag.length>0) { //set은 생성자 함수, 하지만 일반 함수처럼 호출 불가. 따라서 new랑 짝궁=>set 객체 만들어짐=>[...new~]=>배열열
                const randomIndex = Math.floor(Math.random()*allTag.length); //0이상 allTag.length이하의 난수 생성
                setRandomTagText(allTag[randomIndex])
                console.log('selected tag:', allTag[randomIndex])
                const filteredPosts= posts.filter((post)=>(post.tags || []).includes(allTag[randomIndex]));
                setRandomPosts(filteredPosts);
            }
        }
    }, [posts]); //뭔가 posts말고 posts 좋아요 순서가 바뀐다면으로 하는 게 더 좋을 거 같은데..

    const handleLikeClick =async(postId)=>{
        try{
            setPosts((prevPosts) => //낙관적 업뎃
                prevPosts.map((post)=> post.postId=== postId
                    ? { ...post, likesCount: post.likesCount + 1 } //이미 {}여기엔 속성이라 post.을 안 붙임
                    : post).sort((a, b) => b.likesCount - a.likesCount)
            );

            const updatedPostByLike = await updateLikeCommentCount(postId); //서버 업뎃
            setPosts((prevPosts) =>
                prevPosts.map((post) =>post.postId=== postId
                    ? { ...post, likesCount: updatedPostByLike.likesCount }
                    :post).sort((a, b) => b.likesCount - a.likesCount)
            );
        }
        catch (error) {
            console.error('Error uploading like count', error);
        }
    }
    const handleCommentClick=async(postId)=>{
        try{
            setPosts((prevPosts)=> //낙관적 업뎃
                prevPosts.map((post)=>post.postId===postId
                ? {...post, commentsCount: post.commentsCount+1}
                    : post)
            );
            const updatedPostByComment= await updateLikeCommentCount(postId) //서버 업뎃
            setPosts((prevPosts)=>
                prevPosts.map((post)=>post.postId===postId
                ? {...post, commentsCount: updatedPostByComment.commentsCount}
                    : post)
            );
        }
        catch(error){
            console.error('Error uploading like count', error);
        }
    }

    const weeklyPosts= randomPosts.slice(0,3); //아 여기선 먼저 useState([])에서[]로 됐다가 다시 비동기로 값을 받는다 usestate에서 useState() 그냥 이렇게 하면 비동기라서 이 코드가 먼저 실행될 떄 undefined가 떠서 타입 오류가 뜬다. slice는 undefined이면 오류가 뜬다. 따라서 []을 쓴다. 그 후 값이 들어온다.

    const weeklyPostIds = useMemo(() => new Set(weeklyPosts.map((weeklyPost) => weeklyPost.postId)), [weeklyPosts]);
    const dailyPosts = useMemo(() => posts.filter((post) => !weeklyPostIds.has(post.postId)), [posts, weeklyPostIds]); //has는 Set,Map에 사용하는 include,some보다 빠르게 작동함.

    const [nextPage, setNextPage] = useState([0,1,2,3,4,5]);
    const onClickNextPage=(value)=>{ 
        const page=[
            [0,1,2,3,4,5],
            [6,7,8,9,10,11],
            [12,13,14,15,16,17],
            [18,19,20,21,22,23]
        ]
        if(value==='<'){
            if(nextPage[0]===0){
                setNextPage(nextPage.map((num)=>num+18))
            }
            else{
                setNextPage(nextPage.map((num)=>num-6))
            }
        }
        else if (value==='>') {
            if (nextPage[0]===18) {
                setNextPage(nextPage.map((num)=>num-18))
            }
            else{
                setNextPage(nextPage.map((num)=>num+6))
            }
        }
        else{
            setNextPage(page[value]);
        }
    }

    const handleTagClick = (post) => {
        setIsModalOpen(true);
        setSelectedPost(post);
    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    const fileInputRef= useRef(null);
    const handleContainerClick=()=>{
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    }

    const [uploadfileUrl, setUploadFileUrl]= useState([])

    const handleFileChange = (e) => {
        if (!e.target.files || e.target.files.length === 0) {
            alert('파일 업로드를 다시 해주세요.');
            return;
        }

        if (e.target.files.length > 5) {
            alert('한 번에 올릴 수 있는 파일 갯수: 5개');
            return;
        }

        const files = Array.from(e.target.files);
        const selectedFiles = files.filter((file) => {
            if (file.size > 20 * 1024 ** 2) {
                alert('파일 크기는 20MB를 초과할 수 없습니다.');
                return false;
            }
            const validTypes = ['image/jpeg', 'image/png', 'image/heic'];
            if (!validTypes.includes(file.type)) {
                alert('지원되는 파일 형식: JPG, PNG, HEIC');
                return false;
            }
            return true;
        });

        if (selectedFiles.length === 0) {
            alert('유효한 파일이 없습니다. 파일 업로드를 다시 해주세요.');
            return;
        }

        const promises = selectedFiles.map((file) => {
            return new Promise((resolve, reject) => {
                const reader = new FileReader();
                reader.onload = (e) => resolve(e.target.result);
                reader.onerror = () => reject(new Error('파일 읽기 중 오류가 발생했습니다.'));
                reader.readAsDataURL(file); //비동기
            });
        });

        Promise.all(promises)
            .then((results) => {
                setUploadFileUrl(results); //배열.
            })
            .catch(() => {
                alert('파일 읽기 중 오류가 발생했습니다.');
            });
    };

    const handleCancelButton=()=>{
        setUploadFileUrl([])
    }
    const [uploadFileInfo, setuploadFileInfo]= useState({})
    const handleOnchangeUploadFileInfo=(e)=>{
        if(e.target.className==='inputTitle'){
            setuploadFileInfo((uploadFileInfo)=>({...uploadFileInfo, postText:e.target.value})) //제목
            return;
        }
        if(e.target.className==='inputExplain'){
            setuploadFileInfo((uploadFileInfo)=>({ ...uploadFileInfo, postDescription: e.target.value })) //설명
            return;
        }
        if(e.target.className==='postImageLocation'){
            setuploadFileInfo((uploadFileInfo)=>({...uploadFileInfo, postLocation:e.target.value})) //설명
            return;
        }
        if(e.target.className==='postImageTagInput'){
            setuploadFileInfo((uploadFileInfo)=>({...uploadFileInfo, postTag:e.target.value})) //설명
            return;
        }
    }

    useEffect(() => {
        const sumUploadImageInfo = { ...uploadFileInfo, images: uploadfileUrl };
        setUploadImage(sumUploadImageInfo);
    }, [uploadFileInfo, uploadfileUrl]);

    return (
        <div >
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
                    <WeeklyPopularTag
                        post= {[weeklyPosts[0]]}
                        handleLikeClick={handleLikeClick}
                        handleCommentClick={handleCommentClick}
                    />
                    {/*------*/}
                    <WeeklyPopularTag
                        post= {[weeklyPosts[1]]}
                        handleLikeClick={handleLikeClick}
                        handleCommentClick={handleCommentClick}
                    />
                    {/*------*/}
                    <WeeklyPopularTag
                        post= {[weeklyPosts[2]]}
                        handleLikeClick={handleLikeClick}
                        handleCommentClick={handleCommentClick}
                    />
                    {/*------*/}
                </div>
                <div className={styles.todayTagTopContainer}>
                    <img src={twinkle} alt=''className={styles.twinkleIcon}></img>
                    <span className={styles.todayTag}>오늘의 태그 인기 사진</span>
                </div>
                <div className={styles.todayTagAllContainer}>
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeClick={handleLikeClick}
                            handleCommentClick={handleCommentClick}
                        />
                    </div>
                    <DailyPopularTagModal
                        isOpen={isModalOpen}
                        onClose={handleCloseModal}
                        post={selectedPost ? [selectedPost] : []}
                        handleLikeClick={handleLikeClick}
                        handleCommentClick={handleCommentClick}
                    />
                    {/*-------*/}
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeClick={handleLikeClick}
                            handleCommentClick={handleCommentClick}
                        />
                    </div>
                    {/*--------*/}
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeClick={handleLikeClick}
                            handleCommentClick={handleCommentClick}
                        />
                    </div>
                    {/*--------*/}
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeClick={handleLikeClick}
                            handleCommentClick={handleCommentClick}
                        />
                    </div>
                    {/*--------*/}
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeClick={handleLikeClick}
                            handleCommentClick={handleCommentClick}
                        />
                    </div>
                    {/*--------*/}
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeClick={handleLikeClick}
                            handleCommentClick={handleCommentClick}
                        />
                    </div>
                    {/*--------*/}
                </div>
                <div className={styles.forFlexButton}>
                    <img alt='' src={leftButton} className={styles.leftButton}
                        onClick={()=>onClickNextPage('<')}/>
                    <img alt='' src={num1} className={styles.num1Icon}
                        onClick={()=>onClickNextPage(0)}/>
                    <img alt='' src={num2} className={styles.num2Icon}
                        onClick={()=>onClickNextPage(1)}/>
                    <img alt='' src={num3} className={styles.num3Icon}
                        onClick={()=>onClickNextPage(2)}/>
                    <img alt='' src={num4} className={styles.num4Icon}
                        onClick={()=>onClickNextPage(3)}/>
                    <img alt='' src={rightButton} className={styles.rightButton}
                        onClick={()=>onClickNextPage('>')}/>
                </div>
                <div className={styles.postImageContainerOutter}>
                    <img src={camera} alt='' className={styles.postImageIconOutter}></img>
                    <span className={styles.postImageTextOutter}>#사진 올리기</span>
                </div>
                <div className={styles.postImageContainerInner}>
                    <img src={camera} alt='' className={styles.postImageIconInner}></img>
                    <span className={styles.postImageTextInner}>새로운 풍경 사진 업로드</span>
                    <div className={styles.postImageToolContainer}>
                    {uploadfileUrl.length>0? 
                            <div className={styles.postImageToolContainer2}>
                        {uploadfileUrl.map((url)=>(
                            <img className={styles.uploadedImage} src={url} alt=''/>
                                ))}
                            </div>
                            :
                            <div className={styles.postImageToolContainer1}
                                onClick={handleContainerClick}>
                                <img src={cloud} alt='' className={styles.cloudIcon}></img>
                                <p className={styles.postImageToolText}>이곳을 클릭하거나 사진을 드래그하여 업로드하세요.</p>
                                <p className={styles.ImageInfo}>지원 형식: JPG, PNG, HEIC / 최대 파일 크기: 5MB</p>
                                <input
                                    type='file'
                                    multiple
                                    ref={fileInputRef}
                                    accept="image/jpeg,image/png,image/heic"
                                    style={{ display: 'none' }}
                                    onChange={handleFileChange}
                                />
                            </div>
                        }
                    </div>
                    <p className={styles.postImageTitle}>제목</p>
                    <input className={styles.inputTitle}
                        placeholder='예: 제주도 성산일출봉의 아름다운 일출'
                        onChange={handleOnchangeUploadFileInfo}></input>
                    <p className={styles.postImageExplain}>설명</p>
                    <textarea className={styles.inputExplain}
                        placeholder='사진에 담긴 이야기나 촬영 시 느낀 감정을 자류롭게 작성해주세요.'
                        onChange={handleOnchangeUploadFileInfo}></textarea>
                    <p className={styles.postImageLocation}>위치</p>
                    <input className={styles.inputLocation}
                        placeholder='예: 제주도특별자치도 서귀포시 성산읍'
                        onChange={handleOnchangeUploadFileInfo}></input>
                    <p className={styles.postImageTag}>태그</p>
                    <input className={styles.postImageTagInput}
                        placeholder='#에 의해 나눠집니다. 예: #가족#일본#겨울'
                        onChange={handleOnchangeUploadFileInfo}></input>
                    <div className={styles.forflexPostImage}>
                        <button className={styles.uploadImageButtonContainer}>
                            <img src={twinkle} alt='' className={styles.twinkleIcon2}></img>
                            <span className={styles.upLoadImageText}
                            onClick={()=>uploadingImage(uploadImage)}>사진 업로드하기</span>
                        </button>
                        <button className={styles.cancelButton} onClick={handleCancelButton}>취소하기</button>
                    </div>
                </div>
            </div>
        </div>
    )
}