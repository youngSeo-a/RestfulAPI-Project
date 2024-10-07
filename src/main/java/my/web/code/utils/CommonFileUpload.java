package my.web.code.utils;

import groovyjarjarpicocli.CommandLine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j // 로그를 남기기 위한 annotation
public class CommonFileUpload {

    public static Map<String, Object> uploadFiles
            (MultipartFile file, String filePath, String fileType){

        Map<String, Object> result = null;
        try{
            //파일 객체 없다면  에러 처리
            if(file == null) {
                throw new FileNotFoundException("파일이 없음");
            }

            result = new HashMap<>();
            String originFileName = file.getOriginalFilename(); // 원본 파일 이름
            String ext = originFileName.substring(originFileName.lastIndexOf(".") +1);
            //저장되는 파일이름이 겹치지 않도록 중복이 어려운 UUID 이용하여 만든다
            //uuid 는 하이픈(-) 이 들어가기 때문에 제거한다.
            String uuId = UUID.randomUUID().toString().replaceAll("-", "");
            uuId = uuId.substring(0, 16); //길이가 길어서 16자로 자른다.
            String newFileName = uuId + "." + ext;

            String fullPath = filePath + fileType + File.separator + newFileName;
            //새로 생성할 파일 객체를 만든다
            File newFile = new File(fullPath);

            //저장할 경로가 있는지 확인해서 없으면 만들자
            if(!newFile.getParentFile().exists()){
                newFile.getParentFile().mkdirs();
            }

            //빈파일 만들기
            newFile.createNewFile();
            //빈파일에 내용 옮기기
            file.transferTo(newFile);

            result.put("newFile", newFile);
            result.put("filePath",  filePath + fileType + File.separator);

        }catch (Exception e) {
            //에러 로그 남기기
            log.error("======File Upload Error ===== : {}", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }
}
