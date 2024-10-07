package my.web.code.service.gallery;

import lombok.RequiredArgsConstructor;
import my.web.code.dto.FileDTO;
import my.web.code.dto.Gallery;
import my.web.code.mapper.gallery.GalleryMapper;
import my.web.code.utils.CommonFileUpload;
import my.web.code.utils.ImageResizeUtil;
import my.web.code.utils.PagingVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor

public class GalleryService {

    @Value("${server.stored.file.path}")
    private String filePath;

    //mapper 의존성 주입
    private final GalleryMapper mapper;

    //페이징객체 의존성 주입
    //@Component를 써서 스프링한테 객체를 받아올수 있음.
    private final PagingVO paging;

    public Map<String, Object> createGallery(Gallery.RequestDTO requestDTO) throws  Exception {
        Map<String, Object> resultMap = new HashMap<>();

        //갤러리 데이터 저장
        int result = mapper.insertGallery(requestDTO);

        if(result < 0) {
            throw new Exception("갤러리 데이터 저장 오류");
        }

        //파일 저장
        Map<String, Object> uploadedMap
                = CommonFileUpload.uploadFiles(requestDTO.getFile(), filePath, "gallery");

        if(uploadedMap != null) {
            //썸네일 만들기
            File newFile = (File)uploadedMap.get("newFile");
            String filePath = uploadedMap.get("filePath").toString();
            String thumbPath = filePath + "thumb" + File.separator;
            //썸네일 만들기
            String thumbFilName
                    = ImageResizeUtil.makeThumbnailFile(300, 200, newFile, thumbPath);

            //만들어진 파일정보 저장
            FileDTO fileDTO =
                    FileDTO.builder()
                            .parentId(requestDTO.getGallId())
                            .fileOriginName(requestDTO.getFile().getOriginalFilename())
                            .fileStoredName(newFile.getName())
                            .filePath(filePath)
                            .fileThumbName(thumbFilName)
                            .fileThumbPath(thumbPath)
                            .fileType("G")
                            .build();


            result = mapper.insertFile(fileDTO);

            if(result < 0){
                throw new Exception(" 파일 저장 오류");
            }

            //아무 문제 없이 성공하면 200
            resultMap.put("resultCode", 200);
        }
        return resultMap;
    }

    public Map<String, Object> updateGallery(Gallery.RequestDTO requestDTO) throws  Exception {
        Map<String, Object> resultMap = new HashMap<>();

        //기존 데이터 가져오기
        Gallery.GalleryVO galleryInfo = mapper.getGalleryInfo(requestDTO.getGallId());

        //새로운 데이터로 기존데이터에서 변경
        galleryInfo.setGallTitle(requestDTO.getGallTitle());
        galleryInfo.setGallWriter(requestDTO.getGallWriter());

        //갤러리 데이터 수정
        int result = mapper.updateGallery(galleryInfo);

        if(result < 0) {
            throw new Exception("갤러리 데이터 수정 오류");
        }
        if(!requestDTO.getFile().isEmpty()){

            //실제 파일을 지웁니다.
            this.deleteImage(galleryInfo.getFile());

            //파일 저장
            Map<String, Object> uploadedMap
                    = CommonFileUpload.uploadFiles(requestDTO.getFile(), filePath, "gallery");

            if(uploadedMap != null) {
                //썸네일 만들기
                File newFile = (File)uploadedMap.get("newFile");
                String filePath = uploadedMap.get("filePath").toString();
                String thumbPath = filePath + "thumb" + File.separator;
                //썸네일 만들기
                String thumbFilName
                        = ImageResizeUtil.makeThumbnailFile(300, 200, newFile, thumbPath);

                //만들어진 파일정보 저장
                FileDTO fileDTO =
                        FileDTO.builder()
                                .fileId(galleryInfo.getFile().getFileId())
                                .parentId(galleryInfo.getGallId())
                                .fileOriginName(requestDTO.getFile().getOriginalFilename())
                                .fileStoredName(newFile.getName())
                                .filePath(filePath)
                                .fileThumbName(thumbFilName)
                                .fileThumbPath(thumbPath)
                                .fileType("G")
                                .build();


                result = mapper.updateFile(fileDTO);

                if(result < 0) {
                    throw new Exception(" 파일 수정 오류");
                }
            }
        }
        //아무 문제 없이 성공하면 200
        resultMap.put("resultCode", 200);

        return resultMap;
    }

    public Gallery.Response getGalleryList(Map<String, Object> param) throws Exception{
        int nowPage = Integer.parseInt(param.get("nowPage").toString());
        int totalSize = mapper.getGalleryListTotal();

        List<Gallery.GalleryVO> galleryList = new ArrayList<>();
        //페이지 처리를 위한 기본값 세팅
        paging.setPageData(nowPage, totalSize);
        //페이지의 시작과 끝 세팅
        param.put("start", paging.getStart());
        param.put("end", paging.getEnd());

        if (totalSize > 0){
            galleryList = mapper.getGalleryList(param);
        }
        //빌더패턴을 이용한 객체 선언
        //객체를 선언하고 사용하는게 용이하다.
        return Gallery.Response
                .builder()
                .galleryList(galleryList)
                .nowPage(nowPage)
                .totalSize(totalSize)
                .pageHtml(paging.pageHTML())
                .build();
    }

    public int deleteGallery(Map<String, Object> param) throws  Exception {

        String[] delTargets = param.get("delTargets").toString().split(",");
        param.put("delTargetIds", delTargets);

        //삭제하기 위해 파일 목록 불러오기
        List<FileDTO> delFiles = mapper.getFileInfo(param);
        int resultCode = 0;

        for(int i = 0; i < delFiles.size(); i++){
            FileDTO fileDTO = delFiles.get(i);
            resultCode = mapper.deleteGalleryFile(fileDTO.getParentId());

            //파일정보를 정상 삭제 했는지 확인
            if (resultCode > 0){
                //파일정보를 정상 삭제 했는지 확인
                resultCode = mapper.deleteGallery(fileDTO.getParentId());
                //갤러리 정보 삭제가 잘못되었다면 에러 발생
                if(resultCode < 1){
                    throw new Exception("delete fail: " + fileDTO.getParentId());
                }
            }else{
                //파일 삭제가 제대로 안되었다면 에러 발생
                throw new Exception("delete fail: " + fileDTO.getParentId());
            }
            deleteImage(fileDTO);
        }
        return resultCode;
    }


    private void deleteImage(FileDTO fileDTO) throws Exception{
        String path = fileDTO.getFilePath() + fileDTO.getFileStoredName();
        String thumbPath = fileDTO.getFileThumbPath() + fileDTO.getFileThumbName();

        //원본파일을 경로를 통해 파일 객체로 만든다.
        File file = new File(path);
        //썸네일을 경로를 통해 파일 객체로 만든다.
        File thumbFile = new File(thumbPath);

        if(file.exists()){
            file.delete();
        }

        if(thumbFile.exists()){
            thumbFile.delete();
        }
    }
}
