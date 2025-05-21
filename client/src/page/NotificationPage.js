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
