import Container from "./component/Container";
import Calender from "./component/Calender";

function App() {
  return (
    <div
      style={{
        margin: "0 auto",
        position: "relative",
        width: "1440px",
        height: "2942px",
        opacity: "1",
      }}
    >
      <nav
        style={{
          width: "100%",
          height: "80px",
          backgroundColor: "#f2f2f2",
          display: "flex",
          alignItems: "center",
          paddingLeft: "20px",
          marginBottom: "28px",
        }}
      >
        임시 메뉴 바
      </nav>
      <Container>
        <Calender />
      </Container>
    </div>
  );
}

export default App;
