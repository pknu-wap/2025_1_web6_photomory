import styles from './Profile.Main.module.css'
import FriendManage from './Friend.Manage'
import SearchFriend from './Search.Friend'
import {useState, useContext } from 'react'
import { context } from '../App'

function ProfileMain() {
    // const {mokData}=useContext(context)


    return(
    <>
        <div className={styles.myInfoContainer}>
            <div className={styles.myDetailInfoContainer1}>
                <div className={styles.forFlexLeft}>
                    <div className={styles.image}></div>
                    <div className={styles.forFlex}>
                        <div className={styles.name}>권동욱</div>
                        <div className={styles.shortIntro}>자연 풍경 전문 사진작가</div>
                        <div className={styles.photoNum}>사진 999</div>
                    </div>
                </div>
                <button className={styles.logOutForFlexRight}>log out</button>
            </div>
            <div className={styles.myDetailInfoContainer2}>
                <div className={styles.myFieldContainer}>
                    <p className={styles.myField}>전문 분야</p>
                    <input type='text' placeholder='풍경 사진'
                    className={styles.myFieldInput}></input>
                </div>
                <div className={styles.myToolContainer}>
                    <p className={styles.myTool}>사용 장비</p>
                    <input type='text' placeholder='sony A7 IV'
                    className={styles.myToolInput}></input>
                </div>
                <div className={styles.myAreaContainer}>
                    <p className={styles.myArea}>활동 지역</p>
                    <input type='text' placeholder='서울, 강원'
                    className={styles.myAreaInput}></input>
                </div>
            </div>
            <div className={styles.myDetailInfoContainer3}>
                <p className={styles.introTop}>소개</p>
                <textarea className={styles.longIntro}
                type="text" placeholder="제가 누구냐면요.."></textarea>
            </div>
        </div>
        <p className={styles.manageFriendTop}>친구 관리</p>
        <div className={styles.manageFriendContainer}>
            <div className={styles.myFriendsListContainer}>
                <p className={styles.myFriendListTop}>내 친구 목록</p>
                {/* map() */}
                <FriendManage></FriendManage>
            </div>
            <div className={styles.searchFriendContainer}>
                <p className={styles.searchMyFriendTop}>친구 검색</p>
                <input type='text' className={styles.searchBar} placeholder='친구 검색...'></input>
                {/* map() */}
                <SearchFriend></SearchFriend>
            </div>
        </div>
    </>
    )


}

export default ProfileMain