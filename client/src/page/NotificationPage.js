import Footer from "../component/common/Footer";
import Header from "../component/common/Header";
import MemoryNotificationBox from "../component/notification/MemoryNotificationBox";
import GeneralNotificationBox from "../component/notification/GeneralNotificationBox";
function NotificationPage() {
  return (
    <>
      <Header />
      <div
        style={{
          width: "1056px",
          height: "1068px",
          borderRadius: "8px",
          background: "#ffffff",
          margin: "64px auto",
          boxShadow:
            "0px 4px 6px -4px rgba(0, 0, 0, 0.1),0px 10px 15px -3px rgba(0, 0, 0, 0.1)",
        }}
      >
        <MemoryNotificationBox />
        <GeneralNotificationBox />
      </div>
      <Footer />
    </>
  );
}

export default NotificationPage;
