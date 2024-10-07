package my.web.code.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class FileDTO {

        private int fileId;
        private int parentId;
        private String fileOriginName;
        private String fileStoredName;
        private String fileThumbName;
        private String filePath;
        private String fileThumbPath;
        private String fileType;

}
