function PhotoInfo({ albumTitle, albumPeriod, description, photoCount }) {
  return (
    <div style={{ marginBottom: "54px" }}>
      <h2
        style={{
          display: "inline-block",
          padding: "8px 16px",
          marginBottom: "16px",
          background: "#000000",
          color: "#ffffff",
          borderRadius: "8px",
        }}
      >
        #{albumTitle}
      </h2>
      <p style={{ marginBottom: "100px" }}>{description}</p>
      <h2
        style={{
          fontSize: "18px",
          fontWeight: "bold",
          lineHeight: "28px",
          letterSpacing: "0px",
          marginBottom: "16px",
        }}
      >
        촬영 정보
      </h2>
      <div
        style={{
          display: "flex",
          padding: "24px  24px 22px",
          gap: "380px",
          backgroundColor: "#ffffff",
          height: "94px",
          alignItems: "center",
          boxShadow:
            "0px 2px 4px -2px rgba(0, 0, 0, 0.1),0px 4px 6px -1px rgba(0, 0, 0, 0.1)",
          borderRadius: "8px",
        }}
      >
        <div>
          <p
            style={{
              fontSize: "14px",
              fontWeight: "normal",
              lineHeight: "20px",
              color: "#6B7280",
            }}
          >
            촬영기간
          </p>
          <p
            style={{
              fontSize: "16px",
              fontWeight: "500",
              lineHeight: "24px",

              color: "#000000",
            }}
          >
            {albumPeriod}
          </p>
        </div>
        <div>
          <p
            style={{
              fontSize: "14px",
              fontWeight: "normal",
              lineHeight: "20px",
              color: "#6B7280",
            }}
          >
            사진 매수
          </p>
          <p
            style={{
              fontSize: "16px",
              fontWeight: "500",
              lineHeight: "24px",

              color: "#000000",
            }}
          >
            {photoCount}매
          </p>
        </div>
      </div>
    </div>
  );
}

export default PhotoInfo;
