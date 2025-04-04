import React, { useState } from "react";
import {
  getAlbumColorMap,
  getAlbumDotColorsMap,
} from "../utils/albumColors.js";
import dayjs from "dayjs";
import "./Calender.css";
import DayCell from "./DayCell.js";
import AlbumTitleColorList from "./AlbumTitleColorList.js";

// 달력 컴포넌트
function Calender({ groupAlbums, selectedGroupId, albumTitlesByGroup }) {
  const [currentDate, setCurrentDate] = useState(dayjs()); //앱이 처음 실행될 때 기준이 되는 날짜를 dayjs()로 설정 (즉, 오늘 날짜)
  const startOfMonth = currentDate.startOf("month"); //현재 월의 1일을 구함
  const startDay = startOfMonth.day(); // 이번 달의 1일이 무슨 요일인지 확인
  const daysInMonth = currentDate.daysInMonth(); //이번 달이 며칠까지 있는지 확인

  //선택된 그룹의 id로 선택 그룹의 앨범명 배열
  const selectedAlbumTitles = albumTitlesByGroup[selectedGroupId] || []; //선택된 그룹의 앨범명들

  // 앨범명 => 색상 매핑 객체 생성
  const albumColorsMap = getAlbumColorMap(selectedAlbumTitles);
  // 앨범명 점 =>색상 매핑 객체 생성
  const albumDotColorsMap = getAlbumDotColorsMap(selectedAlbumTitles);

  //<-, ->버튼으로 월 이동
  const prevMonth = () => setCurrentDate(currentDate.subtract(1, "month"));
  const nextMonth = () => setCurrentDate(currentDate.add(1, "month"));

  //달력에 날짜를 동적으로 그려주는 함수
  const generateCalendar = () => {
    const days = [];

    // 이번 달 1일의 요일만큼 빈 셀 추가
    for (let i = 0; i < startDay; i++) {
      days.push(<DayCell key={`empty-${i}`} isEmpty={true} />);
    }

    // 이번 달 실제 날짜 채우기기
    for (let d = 1; d <= daysInMonth; d++) {
      const matchingPhotos = []; //각 날짜에 해당하는 사진 데이터를 모으는 배열열

      //각 앨범을 돌며 해당 날짜에 맞는 사진 찾아내기
      groupAlbums.forEach((album) => {
        album.photos.forEach((photo) => {
          const createdDate = new Date(photo.createdAt);
          const photoYear = createdDate.getFullYear();
          const photoMonth = createdDate.getMonth(); // 0부터 시작
          const photoDate = createdDate.getDate();

          //현재 보고 있는 currentDate와 비교해서 현재 연도 + 월 + 날짜가 일치하는지 확인
          if (
            photoYear === currentDate.year() &&
            photoMonth === currentDate.month() &&
            photoDate === d
          ) {
            matchingPhotos.push({
              title: photo.title, //사진 이름
              albumTitle: album.title, //앨범명
              imgUrl: photo.imgUrl, //사진 imgurl
            });
          }
        });
      });
      //해당 날짜에 맞는 사진정보를 갖고 있는 날짜 셀
      days.push(
        <DayCell
          key={d}
          day={d}
          photos={matchingPhotos}
          albumColorsMap={albumColorsMap}
          albumDotColorsMap={albumDotColorsMap}
        />
      );
    }

    return days;
  };

  return (
    <div className="calendar">
      <div className="calenderHeader">
        <h3>촬영 캘린더</h3>
        <span className="moveMonth">
          <button onClick={prevMonth}>⬅</button>
          <h2>{currentDate.format("YYYY년 M월")}</h2>
          <button onClick={nextMonth}>➡</button>
        </span>
      </div>

      <div className="weekdays">
        {["일", "월", "화", "수", "목", "금", "토"].map((d) => (
          <div key={d} className="weekday">
            {d}
          </div>
        ))}
      </div>
      {/* 현재 선택된 그룹의 앨범 제목 목록 하단에 출력 */}
      <div className="days">{generateCalendar()}</div>
      <AlbumTitleColorList
        selectedAlbumTitles={selectedAlbumTitles}
        albumColorsMap={albumColorsMap}
        albumDotColorsMap={albumDotColorsMap}
      />
    </div>
  );
}

export default Calender;
