package my.web.code.mapper.gallery;


import my.web.code.dto.FileDTO;
import my.web.code.dto.Gallery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GalleryMapper {

    //갤러리 정보 저장
    int insertGallery(Gallery.RequestDTO requestDTO) throws Exception;
    //파일 저장
    int insertFile(FileDTO fileDTO) throws Exception;

    //전체 리스트 개수 가져오기
    int getGalleryListTotal() throws Exception;

    //갤러리 리스트 가져오기
    List<Gallery.GalleryVO> getGalleryList(Map<String, Object> param) throws Exception;

    //파일정보들 가져오기 //파일 정보 가져오는 이유??
    List<FileDTO> getFileInfo(Map<String, Object> param) throws Exception;

    //갤러리 지우기
    int deleteGallery(@Param("gallId") int gallId) throws Exception;

    //파일 지우기
    int deleteGalleryFile(@Param("gallId") int gallId) throws Exception;

    //갤러리 정보 한건 가져오기
    Gallery.GalleryVO getGalleryInfo(@Param("gallId") int gallId) throws Exception;
    //갤러리 정보 수정
    int updateGallery(Gallery.GalleryVO vo) throws Exception;
    //파일정보 수정
    int updateFile(FileDTO dto) throws Exception;

}
