import React, { useState } from "react";
import dayjs from "dayjs";
import "./Calender.css";

function Calender() {
  const [currentDate, setCurrentDate] = useState(dayjs()); //앱이 처음 실행될 때 기준이 되는 날짜를 dayjs()로 설정 (즉, 오늘 날짜)
  console.log(currentDate);
  const startOfMonth = currentDate.startOf("month"); //현재 월의 1일을 구함
  const startDay = startOfMonth.day(); // 이번 달의 1일이 무슨 요일인지 확인
  const daysInMonth = currentDate.daysInMonth(); //이번 달이 며칠까지 있는지 확인
  console.log("startOfMonth:\n", startOfMonth);
  console.log("startDay:\n", startDay);
  console.log("dayInmonth:\n", daysInMonth);

  //<-, ->버튼으로 월 이동
  const prevMonth = () => setCurrentDate(currentDate.subtract(1, "month"));
  const nextMonth = () => setCurrentDate(currentDate.add(1, "month"));

  const generateCalendar = () => {
    const days = [];

    for (let i = 0; i < startDay; i++) {
      days.push(<div className="day empty" key={`empty-${i}`}></div>);
    }

    for (let d = 1; d <= daysInMonth; d++) {
      days.push(
        <div className="day" key={d}>
          {d}
        </div>
      );
    }

    return days;
  };

  return (
    <div className="calendar">
      <div className="header">
        <button onClick={prevMonth}>⬅</button>
        <h2>{currentDate.format("YYYY년 M월")}</h2>
        <button onClick={nextMonth}>➡</button>
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
