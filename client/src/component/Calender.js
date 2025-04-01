import React, { useState } from "react";
import dayjs from "dayjs";
import "./Calender.css";
import DayCell from "./DayCell.js";

// 달력 컴포넌트
function Calender() {
  const [currentDate, setCurrentDate] = useState(dayjs()); //앱이 처음 실행될 때 기준이 되는 날짜를 dayjs()로 설정 (즉, 오늘 날짜)
  const startOfMonth = currentDate.startOf("month"); //현재 월의 1일을 구함
  const startDay = startOfMonth.day(); // 이번 달의 1일이 무슨 요일인지 확인
  const daysInMonth = currentDate.daysInMonth(); //이번 달이 며칠까지 있는지 확인

  //<-, ->버튼으로 월 이동
  const prevMonth = () => setCurrentDate(currentDate.subtract(1, "month"));
  const nextMonth = () => setCurrentDate(currentDate.add(1, "month"));

  //달력에 날짜를 동적으로 그려주는 함수
  const generateCalendar = () => {
    const days = [];

    // 이번 달 1일이 무슨 요일인지에 따라 앞에 빈칸 채움
    for (let i = 0; i < startDay; i++) {
      days.push(<DayCell key={`empty-${i}`} isEmpty={true} />);
    }

    //실제 날짜(1일 ~ 말일) 만큼 날짜 셀 추가
    for (let d = 1; d <= daysInMonth; d++) {
      days.push(<DayCell key={d} day={d} />);
    }

    return days; // 이 배열을 렌더링에 사용
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

      <div className="days">{generateCalendar()}</div>
    </div>
  );
}

export default Calender;
