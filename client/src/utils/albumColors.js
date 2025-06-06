// 앨범 색상 팔레트 정의 (배경용)
export const albumColors = [
  "#DBEAFE", // 연한 파랑
  "#F3E8FF", // 연한 보라
  "#FCE7F3", // 연한 핑크
  "#FEF9C3", // 연한 노랑
  "#D1FAE5", // 연한 민트
  "#FFE4E6", // 연한 코랄 (핑크 + 레드 계열, 따뜻한 느낌)
  "#F0F9FF", // 연한 블루그레이 (차분하고 깨끗한 느낌)
];

// 진한 색상 (점용)
export const albumDotColors = [
  "#3B82F6", // 파랑
  "#8B5CF6", // 보라
  "#EC4899", // 핑크
  "#EAB308", // 노랑
  "#10B981", // 민트
  "#F43F5E", // 진한 코랄레드 (FFE4E6과 매칭)
  "#0EA5E9", // 진한 블루스카이 (F0F9FF과 매칭)
];

// 앨범명 => 색상 매핑 함수
export function getAlbumColorMap(titles) {
  const map = {};
  titles.forEach((title, i) => {
    map[title] = albumColors[i % albumColors.length];
  });
  return map;
}

// 앨범명 점 => 진한 색
export function getAlbumDotColorsMap(titles) {
  const map = {};
  titles.forEach((title, i) => {
    map[title] = albumDotColors[i % albumDotColors.length];
  });
  return map;
}
