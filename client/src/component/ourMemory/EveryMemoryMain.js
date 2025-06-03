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
import { useRandomIndex } from "../../contexts/RandomIndexContext";


async function fetchUserposts(retries = 0, maxRetries = 3) {
    let accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/every/posts`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            }
        });
        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('Unauthorized');
            }
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        const posts = await response.json();
        return posts;
    } catch (error) {
        if (error.message === 'Unauthorized' && refreshToken && retries < maxRetries) {
            accessToken = await refreshAccessToken(refreshToken);
            if (accessToken) {
                return await fetchUserposts(retries + 1, maxRetries);
            }
        }
        console.error('Failed to get post');
        return null;
    }
}

async function refreshAccessToken(refreshToken) {
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/refresh-token`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ refreshToken })
        });
        if (!response.ok) {
            throw new Error(`Token refresh failed status: ${response.status}`);
        }
        const data = await response.json();
        return data.accessToken;
    } catch (error) {
        console.error('Error fetching token:', error);
        return null;
    }
}


async function updateLikeCount(postId, retries = 0, maxRetries = 3) {
    let accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/posts/every/${postId}/like`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({ postId })
        });
        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('Unauthorized');
            }
            throw new Error(`Failed to upload count: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        if (error.message === 'Unauthorized' && refreshToken && retries < maxRetries) {
            accessToken = await refreshAccessToken(refreshToken);
            if (accessToken) {
                return await updateLikeCount(postId, retries + 1, maxRetries);
            }
        }
        console.error('Failed to upload like');
        return null;
    }
}

async function updateComment(postId, comment, retries = 0, maxRetries = 3) {
    let accessToken = localStorage.getItem('accessToken');
    const refreshToken = localStorage.getItem('refreshToken');
    try {
        const response = await fetch(`${process.env.REACT_APP_API_URL}/api/every/comments`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${accessToken}`
            },
            body: JSON.stringify({ postId, userId: comment.userId, commentText: comment.commentText })
        });
        if (!response.ok) {
            if (response.status === 401) {
                throw new Error('Unauthorized');
            }
            throw new Error(`Failed to upload comment: ${response.status}`);
        }
        return await response.json();
    } catch (error) {
        if (error.message === 'Unauthorized' && refreshToken && retries < maxRetries) {
            accessToken = await refreshAccessToken(refreshToken);
            if (accessToken) {
                return await updateComment(postId, comment, retries + 1, maxRetries);
            }
        }
        console.error('Failed to upload comment');
        return null;
    }
}

export default function EveryMemoryMain() {
    const [posts, setPosts] = useState([]);
    const [error, setError] = useState();
    const [randomTagText, setRandomTagText] = useState();
    const [randomPosts, setRandomPosts] = useState([]);
    const [isImageModalOpen, setIsImageModalOpen] = useState(false);
    const [isCommentModalOpen, setIsCommentModalOpen] = useState(false);
    const [selectedPostForModal, setSelectedPostForModal] = useState(null);
    const [uploadFiles, setUploadFiles] = useState([]);
    const [uploadFileInfo, setUploadFileInfo] = useState({
        postText: '',
        postDescription: '',
        location: '',
        tagsJson: ''
    });
    const { randomIndex, updateRandomIndex } = useRandomIndex();

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
            if (file.size > 5 * 1024 ** 2) {
                alert('파일 크기는 5MB를 초과할 수 없습니다.');
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
        setUploadFiles(selectedFiles);
    };

    const uploadingImage = async (retries = 0, maxRetries = 3) => {
        const refreshToken = localStorage.getItem('refreshToken');
        let accessToken = localStorage.getItem('accessToken');

        try {
            if (!uploadFiles || uploadFiles.length === 0) {
                throw new Error('이미지를 선택해주세요.');
            }
            if (!accessToken) {
                throw new Error('로그인이 필요합니다.');
            }

            const formData = new FormData();
            for (const file of uploadFiles) {
                formData.append('photo', file);
            }
            formData.append('postText', uploadFileInfo.postText || '');
            formData.append('postDescription', uploadFileInfo.postDescription || '');
            formData.append('location', uploadFileInfo.location || '');
            formData.append('tagsJson', uploadFileInfo.tagsJson || '');

            // FormData 내용 확인용 디버깅
            for (let [key, value] of formData.entries()) {
                if (value instanceof File) {
                    console.log(`${key}: ${value.name}, ${value.size} bytes, type: ${value.type}`);
                } else {
                    console.log(`${key}: ${value}`);
                }
            }

            const response = await fetch(`${process.env.REACT_APP_API_URL}/api/every/posts`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
                body: formData
            });
            
            if (!response.ok) {
                if (response.status === 401) {
                    throw new Error('Unauthorized');
                }
                const errorData = await response.json();
                throw new Error(`이미지 업로드 실패: ${response.status} - ${errorData.message || 'Bad Request'}`);
            }

            const result = await response.json();
            alert('이미지가 성공적으로 업로드되었습니다.');
            setUploadFiles([]); // 업로드 성공 시 파일 초기화
            setUploadFileInfo({ postText: '', postDescription: '', location: '', tagsJson: '' }); // 입력 초기화
            return result;
        } catch (error) {
            console.error('Upload error:', error);
            if (error.message === 'Unauthorized' && refreshToken && retries < maxRetries) {
                accessToken = await refreshAccessToken(refreshToken);
                if (accessToken) {
                    return await uploadingImage(retries + 1, maxRetries);
                }
            }
            alert(error.message || '이미지 업로드에 실패했습니다.');
            return null;
        }
    };

    const getTimeUntilNextSaturday = () => {
        const now = new Date();
        const nextSaturday = new Date(now);
        nextSaturday.setDate(now.getDate() + (6 + 7 - now.getDay()) % 7);
        nextSaturday.setHours(23, 59, 59, 999);
        return nextSaturday.getTime() - now.getTime();
    };

    useEffect(() => {
        let intervalId = null;

        const updateRandomIndexValue = () => {
            if (posts && posts.length > 0) {
                const allTags = posts.reduce((tags, post) => {
                    if (post && post.tags && Array.isArray(post.tags)) {
                        return [...tags, ...post.tags];
                    }
                    return tags;
                }, []);
                
                const uniqueTags = [...new Set(allTags)];
                if (uniqueTags.length > 0) {
                    const newRandomIndex = Math.floor(Math.random() * uniqueTags.length);
                    updateRandomIndex(newRandomIndex);
                    // localStorage에 현재 인덱스와 업데이트 시간 저장
                    localStorage.setItem('randomIndex', newRandomIndex);
                    localStorage.setItem('lastUpdateTime', new Date().getTime());
                }
            }
        };

        // localStorage에서 저장된 값 확인
        const savedIndex = localStorage.getItem('randomIndex');
        const lastUpdateTime = localStorage.getItem('lastUpdateTime');
        const now = new Date().getTime();
        const oneWeek = 7 * 24 * 60 * 60 * 1000; // 1주일을 밀리초로

        if (savedIndex && lastUpdateTime) {
            // 마지막 업데이트로부터 1주일이 지났는지 확인
            if (now - parseInt(lastUpdateTime) >= oneWeek) {
                updateRandomIndexValue();
            } else {
                // 1주일이 지나지 않았다면 저장된 인덱스 사용
                updateRandomIndex(parseInt(savedIndex));
            }
        } else if (posts && posts.length > 0) {
            // 저장된 값이 없는 경우 초기 설정
            const allTags = posts.reduce((tags, post) => {
                if (post && post.tags && Array.isArray(post.tags)) {
                    return [...tags, ...post.tags];
                }
                return tags;
            }, []);
            
            const uniqueTags = [...new Set(allTags)];
            if (uniqueTags.length > 0) {
                const newRandomIndex = Math.floor(Math.random() * uniqueTags.length);
                updateRandomIndex(newRandomIndex);
                localStorage.setItem('randomIndex', newRandomIndex);
                localStorage.setItem('lastUpdateTime', now);
            }
        }

        // 다음 토요일까지의 시간 계산
        const timeUntilNextSaturday = getTimeUntilNextSaturday();
        
        // 타이머 설정
        const timer = setTimeout(() => {
            updateRandomIndexValue();
            // 이후 매주 토요일마다 갱신
            intervalId = setInterval(updateRandomIndexValue, 7 * 24 * 60 * 60 * 1000);
        }, timeUntilNextSaturday);

        return () => {
            clearTimeout(timer);
            if (intervalId) clearInterval(intervalId);
        };
    }, [posts, updateRandomIndex]);

    useEffect(() => {
        if (posts && posts.length > 0) {
            const allTags = posts.reduce((tags, post) => {
                if (post && post.tags && Array.isArray(post.tags)) {
                    return [...tags, ...post.tags];
                }
                return tags;
            }, []);
            const uniqueTags = [...new Set(allTags)];
            if (uniqueTags.length > 0 && randomIndex !== null) {
                setRandomTagText(uniqueTags[randomIndex]);
                const filteredPosts = posts.filter((post) =>
                    post && post.tags && Array.isArray(post.tags) &&
                    post.tags.includes(uniqueTags[randomIndex])
                );
                setRandomPosts(filteredPosts);
            }
        }
    }, [posts, randomIndex]);

    const fetchPosts = async () => {
        try {
            const posts = await fetchUserposts();
            if (posts && Array.isArray(posts)) {
                const sortedPosts = [...posts].sort((a, b) => b.likesCount - a.likesCount);
                setPosts(sortedPosts);
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
    }, []);

    const handleLikeNum = async (postId) => {
        const rollBackPosts = [...posts];
        try {
            setPosts((prevPosts) =>
                prevPosts.map((post) => post.postId === postId
                    ? post.isLikeCountUp === false
                        ? { ...post, likesCount: post.likesCount + 1, isLikeCountUp: true }
                        : { ...post, likesCount: post.likesCount - 1, isLikeCountUp: false }
                    : post).sort((a, b) => b.likesCount - a.likesCount)
            );
            await updateLikeCount(postId);
        } catch (error) {
            console.error('Error uploading like count', error);
            setPosts(rollBackPosts);
        }
    };

    const handleCommentNum = async (modalPost, comment) => {
        const rollBackPosts = [...posts];
        try {
            setPosts((prevPosts) =>
                prevPosts.map((post) => post.postId === modalPost.postId
                    ? {
                        ...post,
                        commentsCount: post.commentsCount + 1,
                        comments: [...post.comments, comment]
                    }
                    : post)
            );
            await updateComment(modalPost.postId, comment);
        } catch (error) {
            console.error('Error uploading comment', error);
            setPosts(rollBackPosts);
        }
    };

    const weeklyPosts = randomPosts.slice(0, 3);
    const weeklyPostIds = useMemo(() => new Set(weeklyPosts.map((weeklyPost) => weeklyPost.postId)), [weeklyPosts]);
    const dailyPosts = useMemo(() => posts.filter((post) => !weeklyPostIds.has(post.postId)), [posts, weeklyPostIds]);

    const [nextPage, setNextPage] = useState([0, 1, 2, 3, 4, 5]);
    const onClickNextPage = (value) => {
        const page = [
            [0, 1, 2, 3, 4, 5],
            [6, 7, 8, 9, 10, 11],
            [12, 13, 14, 15, 16, 17],
            [18, 19, 20, 21, 22, 23]
        ];
        if (value === '<') {
            if (nextPage[0] === 0) {
                setNextPage(nextPage.map((num) => num + 18));
            } else {
                setNextPage(nextPage.map((num) => num - 6));
            }
        } else if (value === '>') {
            if (nextPage[0] === 18) {
                setNextPage(nextPage.map((num) => num - 18));
            } else {
                setNextPage(nextPage.map((num) => num + 6));
            }
        } else {
            setNextPage(page[value]);
        }
    };

    const handleImageClick = (post) => {
        setIsImageModalOpen(true);
        setSelectedPostForModal(post);
    };

    const handleCommentClickForModal = () => {
        setIsCommentModalOpen(true);
    };

    const handleCloseImageModal = () => {
        setIsImageModalOpen(false);
    };

    const handleCloseCommentModal = () => {
        setIsCommentModalOpen(false);
    };

    const fileInputRef = useRef(null);
    const handleContainerClick = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click();
        }
    };

    const handleOnchangeUploadFileInfo = (e) => {
        const { name, value } = e.target;
        setUploadFileInfo((prev) => ({ ...prev, [name]: value }));
    };

    return (
        <div>
            <div className={styles.mainContainer}>
                {error && <p className={styles.error}>{error}</p>}
                <p className={styles.weeklyTag}>
                    <img src={camera} alt='' className={styles.weeklyTagCamera}></img>
                    <span className={styles.weeklyTagText}>
                        주간 인기 {randomTagText ? randomTagText : "'Unknown'"} 사진 갤러리
                    </span>
                </p>
                <div className={styles.forFlexTagBox}>
                    <div className={styles.tagBox}>
                        <img src={landscape} alt='' className={styles.tagBoxLandscape}></img>
                        <span className={styles.tagBoxText}>#{randomTagText ? randomTagText : 'Unknown'}</span>
                    </div>
                </div>
                <div className={styles.forFlexweeklyTag1}>
                    <WeeklyPopularTag
                        post={[weeklyPosts[0]]}
                        handleLikeNum={handleLikeNum}
                        handleCommentClickForModal={handleCommentClickForModal}
                        handleImageClick={handleImageClick}
                    />
                    <WeeklyPopularTag

                        post={[weeklyPosts[1]]}
                        handleLikeNum={handleLikeNum}
                        handleCommentClickForModal={handleCommentClickForModal}
                        handleImageClick={handleImageClick}
                    />
                    <WeeklyPopularTag
                        post={[weeklyPosts[2]]}
                        handleLikeNum={handleLikeNum}
                        handleCommentClickForModal={handleCommentClickForModal}
                        handleImageClick={handleImageClick}
                    />
                </div>
                <div className={styles.todayTagTopContainer}>
                    <img src={twinkle} alt='' className={styles.twinkleIcon}></img>
                    <span className={styles.todayTag}>오늘의 인기 사진</span>
                </div>
                <div className={styles.todayTagAllContainer}>
                    <div className={styles.forModalContainer}
                        onClick={() => {
                            handleTagClick(dailyPosts[nextPage[0]]);
                        }}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[0]]]}
                            handleLikeNum={handleLikeNum}
                            handleCommentClickForModal={handleCommentClickForModal}
                            handleImageClick={() => { handleImageClick(dailyPosts[nextPage[0]]) }}
                        />
                    </div>
                    <div className={styles.forModalContainer}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[1]]]}
                            handleLikeNum={handleLikeNum}
                            handleCommentClickForModal={handleCommentClickForModal}
                            handleImageClick={() => { handleImageClick(dailyPosts[nextPage[1]]) }}
                        />
                    </div>
                    <div className={styles.forModalContainer}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[2]]]}
                            handleLikeNum={handleLikeNum}
                            handleCommentClickForModal={handleCommentClickForModal}
                            handleImageClick={() => { handleImageClick(dailyPosts[nextPage[2]]) }}
                        />
                    </div>
                    <div className={styles.forModalContainer}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[3]]]}
                            handleLikeNum={handleLikeNum}
                            handleCommentClickForModal={handleCommentClickForModal}
                            handleImageClick={() => { handleImageClick(dailyPosts[nextPage[3]]) }}
                        />
                    </div>
                    <div className={styles.forModalContainer}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[4]]]}
                            handleLikeNum={handleLikeNum}
                            handleCommentClickForModal={handleCommentClickForModal}
                            handleImageClick={() => { handleImageClick(dailyPosts[nextPage[4]]) }}
                        />
                    </div>
                    <div className={styles.forModalContainer}>
                        <DailyPopularTag
                            post={[dailyPosts[nextPage[5]]]}
                            handleLikeNum={handleLikeNum}
                            handleCommentClickForModal={handleCommentClickForModal}
                            handleImageClick={() => { handleImageClick(dailyPosts[nextPage[5]]) }}
                        />
                    </div>
                </div>
                <div className={styles.forFlexButton}>
                    <img alt='' src={leftButton} className={styles.leftButton}
                        onClick={() => onClickNextPage('<')} />
                    <img alt='' src={num1} className={styles.num1Icon}
                        onClick={() => onClickNextPage(0)} />
                    <img alt='' src={num2} className={styles.num2Icon}
                        onClick={() => onClickNextPage(1)} />
                    <img alt='' src={num3} className={styles.num3Icon}
                        onClick={() => onClickNextPage(2)} />
                    <img alt='' src={num4} className={styles.num4Icon}
                        onClick={() => onClickNextPage(3)} />
                    <img alt='' src={rightButton} className={styles.rightButton}
                        onClick={() => onClickNextPage('>')} />
                </div>
                <div className={styles.postImageContainerOutter}>
                    <img src={camera} alt='' className={styles.postImageIconOutter}></img>
                    <span className={styles.postImageTextOutter}>#사진 올리기</span>
                </div>
                <div className={styles.postImageContainerInner}>
                    <img src={camera} alt='' className={styles.postImageIconInner}></img>
                    <span className={styles.postImageTextInner}>새로운 풍경 사진 업로드</span>
                    <div className={styles.postImageToolContainer}>
                        {uploadFiles.length > 0 ?
                            <div className={styles.postImageToolContainer2}>
                                {uploadFiles.map((file, index) => (
                                    <img key={index} className={styles.uploadedImage} src={URL.createObjectURL(file)} alt='' />
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
                    <input
                        name="postText"
                        className={styles.inputTitle}
                        placeholder='예: 제주도 성산일출봉의 아름다운 일출'
                        value={uploadFileInfo.postText}
                        onChange={handleOnchangeUploadFileInfo}
                    />
                    <p className={styles.postImageExplain}>설명</p>
                    <textarea
                        name="postDescription"
                        className={styles.inputExplain}
                        placeholder='사진에 담긴 이야기나 촬영 시 느낀 감정을 자유롭게 작성해주세요.'
                        value={uploadFileInfo.postDescription}
                        onChange={handleOnchangeUploadFileInfo}
                    />
                    <p className={styles.postImageLocation}>위치</p>
                    <input
                        name="location"
                        className={styles.inputLocation}
                        placeholder='예: 제주도특별자치도 서귀포시 성산읍'
                        value={uploadFileInfo.location}
                        onChange={handleOnchangeUploadFileInfo}
                    />
                    <p className={styles.postImageTag}>태그</p>
                    <input
                        name="tagsJson"
                        className={styles.postImageTagInput}
                        placeholder='#에 의해 나눠집니다. 예: #가족#일본#겨울'
                        value={uploadFileInfo.tagsJson}
                        onChange={handleOnchangeUploadFileInfo}
                    />
                    <div className={styles.forflexPostImage}>
                        <button className={styles.uploadImageButtonContainer} onClick={uploadingImage}>
                            <img src={twinkle} alt='' className={styles.twinkleIcon2}></img>
                            <span className={styles.upLoadImageText}>사진 업로드하기</span>
                        </button>
                        <button className={styles.cancelButton} onClick={() => {
                            setUploadFiles([]);
                            setUploadFileInfo({ postText: '', postDescription: '', location: '', tagsJson: '' });
                        }}>취소하기</button>
                    </div>
                </div>
            </div>
            <DailyPopularTagModal
                isOpen={isImageModalOpen}
                onClose={handleCloseImageModal}
                post={selectedPostForModal ? [selectedPostForModal] : []}
            />
            <CommentModal
                isOpen={isCommentModalOpen}
                onClose={handleCloseCommentModal}
                post={selectedPostForModal ? [selectedPostForModal] : []}
                handleCommentNum={(commentText) => {
                    if (selectedPostForModal && selectedPostForModal.postId) {
                        handleCommentNum(selectedPostForModal, {
                            userId: selectedPostForModal.comments.userId,
                            userName: selectedPostForModal.comments.userName,
                            userPhotourl: selectedPostForModal.comments.userPhotourl,
                            commentText: commentText
                        });
                    }
                }}
            />
        </div>
    );
}