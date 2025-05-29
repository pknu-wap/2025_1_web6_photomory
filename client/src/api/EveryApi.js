async function fetchUserposts(retries=0, maxRetries=3) {
    let accessToken = localStorage.getItem('accessToken')
    const refreshToken = localStorage.getItem('refreshToken')
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
    } catch (error){ //엑세스 없음 리프토큰으로 엑세스 가져오고 엑세스로 다시 돌림. 이 과정을 최대 3번만 가능하게 함.
        if (error.message === 'Unauthorized' && refreshToken && retries<maxRetries) { //리프토큰 없으면 요청 안 되게게
            accessToken = await refreshAccessToken(refreshToken);
            if (accessToken) {
                const result = await fetchUserposts(retries+1, maxRetries);
                return result
            }
        }
        console.log('Failed to get post')
        return null
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
        if (data.accessToken) {
            localStorage.setItem('accessToken', data.accessToken);
        }
        return data.accessToken;
    }
    catch (error) {
        console.error('Error fetching token:', error);
        return null;
    }
}