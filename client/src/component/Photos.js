import CommentBox from "./CommentBox";
import dayjs from "dayjs";

function Photos({ album }) {
  return (
    <>
      {album.photos.map((photo) => (
        <div key={photo.id} style={{ display: "flex", gap: "24px" }}>
          <img
            src={photo.imgUrl}
            alt={photo.title}
            style={{ borderRadius: "8px", width: "516px", height: "400px" }}
          />
          <div>
            <h3>{photo.title}</h3>
            <p>{<p>{dayjs(photo.createdAt).format("YYYY/MM/DD")}</p>}</p>
            <CommentBox />
          </div>
        </div>
      ))}
    </>
  );
}

export default Photos;
