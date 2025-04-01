import "./Calender"; //Calender 컴포넌트 css사용

//달력 안에 들어가 있는 날짜 컴포넌트
function DayCell({ day, isEmpty = false }) {
  //day: 날짜, isEmpty:빈 칸인지 여부 (true이면 날짜 없음)
  return (
    <div className={`day ${isEmpty ? "empty" : ""}`}>{!isEmpty && day}</div>
  );
}

export default DayCell;
