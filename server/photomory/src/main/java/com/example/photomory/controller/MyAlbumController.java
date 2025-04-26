import com.example.photomory.service.MyAlbumService;
import com.example.photomory.dto.MyAlbumDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/my-albums")
@RequiredArgsConstructor
public class MyAlbumController {

    private final MyAlbumService myAlbumService;

    @GetMapping("/{albumId}/user/{userId}")
    public ResponseEntity<MyAlbumDetailDto> getMyAlbumDetail(
            @PathVariable Long albumId,
            @PathVariable Long userId) {

        MyAlbumDetailDto albumDetail = myAlbumService.getMyAlbum(albumId, userId);
        return ResponseEntity.ok(albumDetail);
    }
}
