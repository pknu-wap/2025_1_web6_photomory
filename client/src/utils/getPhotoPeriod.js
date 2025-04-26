export function getPhotoPeriod(photos) {
  if (!photos || photos.length === 0) return null;

  // 날짜들을 Date 객체로 변환
  const dates = photos.map((photo) => new Date(photo.createdAt));

  // 가장 빠른 날짜 (최솟값)
  const minDate = new Date(Math.min(...dates));
  const maxDate = new Date(Math.max(...dates));

  // yyyy.mm.dd 포맷으로 변환
  const format = (date) =>
    `${date.getFullYear()}.${(date.getMonth() + 1)
      .toString()
      .padStart(2, "0")}.${date.getDate().toString().padStart(2, "0")}`; //padstart "3" → "03" 두 자리 맞춤춤

  return `${format(minDate)} ~ ${format(maxDate)}`;
}
