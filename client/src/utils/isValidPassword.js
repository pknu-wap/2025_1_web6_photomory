function isValidPassword(password) {
  //비밀번호 문자열에 있는 숫자 추출한 배열
  const allDigits = password.match(/\d/g) || [];
  //비밀번호 문자열에 있는 특수 문자 추출한 배열
  const allSpecials = password.match(/[!@#$%^&*(),.?":{}|<>_\-\\[\]]/g) || [];

  //acc객체를 이용한한 누적계산을 통한 문자별 등장 횟수 계산

  //숫자 누적 횟수 계산
  const digitCount = allDigits.reduce((acc, char) => {
    acc[char] = (acc[char] || 0) + 1;
    return acc;
  }, {});

  //특수문자 누적횟수 계산
  const specialCount = allSpecials.reduce((acc, char) => {
    acc[char] = (acc[char] || 0) + 1;
    return acc;
  }, {});

  //객체의 값들만 배열로 추출하여 1번만 등장한 숫자 종류 수 계산
  const uniqueDigits = Object.values(digitCount).filter(
    (count) => count === 1
  ).length;

  //객체의 값들만 배열로 추출하여 1번만 등장한 특수문자 종류 수 계산
  const uniqueSpecials = Object.values(specialCount).filter(
    (count) => count === 1
  ).length;

  // 숫자/특수문자를 제거한 나머지가 전부 영어인지 확인
  const otherChars = password
    .replace(/\d/g, "")
    .replace(/[!@#$%^&*(),.?":{}|<>_\-\\[\]]/g, "");

  const isOnlyAlphabet = /^[a-zA-Z]*$/.test(otherChars);

  return (
    password.length >= 12 &&
    uniqueDigits >= 4 &&
    uniqueSpecials >= 2 &&
    isOnlyAlphabet
  );
}

export default isValidPassword;
