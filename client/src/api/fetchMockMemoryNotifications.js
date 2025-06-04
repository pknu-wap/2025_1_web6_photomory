// 가짜 메모리 알림 데이터 반환 함수
export async function fetchMockMemoryNotifications() {
  return [
    {
      type: "REMIND",
      title: "봄 소풍의 추억",
      photoUrl: "https://picsum.photos/seed/photo1/600",
      date: "2025-05-20",
      message: "8일 전 오늘",
    },
    {
      type: "REMIND",
      title: "첫 여행지에서",
      photoUrl: "https://picsum.photos/seed/photo2/600",
      date: "2025-05-22",
      message: "6일 전 오늘",
    },
    {
      type: "REMIND",
      title: "카페에서의 한 컷",
      photoUrl: "https://picsum.photos/seed/photo3/600",
      date: "2025-05-23",
      message: "5일 전 오늘",
    },
    {
      type: "REMIND",
      title: "해 질 녘 바닷가",
      photoUrl: "https://picsum.photos/seed/photo4/600",
      date: "2025-05-24",
      message: "4일 전 오늘",
    },
    {
      type: "REMIND",
      title: "산책하는 모습",
      photoUrl: "https://picsum.photos/seed/photo5/600",
      date: "2025-05-25",
      message: "3일 전 오늘",
    },
    {
      type: "REMIND",
      title: "우리의 첫 셀카",
      photoUrl: "https://picsum.photos/seed/photo6/600",
      date: "2025-05-26",
      message: "2일 전 오늘",
    },
  ];
}
