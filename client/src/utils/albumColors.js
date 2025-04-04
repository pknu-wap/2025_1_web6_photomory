// 앨범 색상 팔레트 정의
export const albumColors = [
  "#DBEAFE", // 연한 파랑
  "#F3E8FF", // 연한 보라
  "#FCE7F3", // 연한 핑크
  "#FEF9C3", // 연한 노랑
  "#D1FAE5", // 연한 민트
];

// 앨범명 => 색상 매핑 함수
export function getAlbumColorMap(titles) {
  const map = {};
  titles.forEach((title, i) => {
    map[title] = albumColors[i % albumColors.length];
  });
  return map;
}
