package my.web.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class Gallery {

    @Data
    public static class RequestDTO{
        private int gallId;
        private String gallTitle;
        //클라이언트에서 오는 파일이 저장되는 객체
        private MultipartFile file;
        private String gallWriter;
    }

    @Data
    public static class GalleryVO {
        private int gallId;
        private String gallTitle;
        private String gallWriter;
        private int gallCount;
        private String updateDate;
        private FileDTO file; //객체로 있음

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public  static class Response{
        List<GalleryVO> galleryList;
        int nowPage;
        int totalSize;
        String pageHtml;
    }
}

