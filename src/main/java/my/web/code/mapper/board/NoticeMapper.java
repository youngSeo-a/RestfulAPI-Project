package my.web.code.mapper.board;

import java.util.List;
import java.util.Map;

import my.web.code.dto.FileDTO;
import my.web.code.dto.Notice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface NoticeMapper {

	//전체 리스트 개수 가져오기 
	int getBoardTotalCount() throws Exception;
	//한페이지 보여줄 리스트 가져오기 
	List<Notice.Response> getBoardList(Map<String, Object> param) throws Exception;
	//게시글 정보 가져오기
	Notice.Response getBoard(@Param("boardId") int boardId) throws Exception;
	//게시글 카운트 증가
	int updateBoardCount(@Param("boardId") int boardId) throws Exception;
	//게시글 삭제
	int deleteNotice(@Param("boardId") int boardId) throws Exception;
	//게시글 수정
	int updateNotice(Notice.Update updateVO) throws Exception;
	//게시글 저장
	int createNotice(Notice.Request requestVO) throws Exception;
	//파일 저장
	int insertFile(FileDTO fileDTO) throws Exception;
	//파일 정보
	FileDTO getFileInfo(Map<String, Object> param) throws Exception;
	//파일 삭제
	int deleteBoardFile(@Param("boardId") int boardId) throws Exception;
}
