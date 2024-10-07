package my.web.code.service.board;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import my.web.code.dto.FileDTO;
import my.web.code.dto.Notice;
import my.web.code.mapper.board.NoticeMapper;
import my.web.code.utils.CommonFileUpload;
import my.web.code.utils.PagingVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NoticeService {

	@Value("${server.stored.file.path}")
	private String filePath;

	//생성자를 통해서 의존성 주입
	private final NoticeMapper mapper;
	private final PagingVO paging;

	public Notice.DataResponse getBoardList(Map<String, Object> param) throws Exception {
		// 결과 데이터를 저장할 객체 선언  
		Notice.DataResponse response = new Notice.DataResponse();
		//파라메터에서 현재페이지 꺼내기 
		int nowPage = (int) param.get("nowPage");
		//전체 게시글 수 가져오기 
		int total = mapper.getBoardTotalCount();
		//페이지 처리를 위해 데이터 입력
		this.paging.setPageData(nowPage, total);
		//페이지에 해당하는 게시글 가져오는 객체 선언
		List<Notice.Response> list = new ArrayList<>();

		//개수가 존재할 때만 리스트 호출한다.
		if (total > 0) {
			//페이징처리를 위한 파라메터 입력
			param.put("start", paging.getStart());
			param.put("end", paging.getEnd());
			list = mapper.getBoardList(param);
		}
		//결과 객체에 데이터 삽입
		response.setTotal(total);
		response.setBoardList(list);
		response.setPageHTML(this.paging.pageHTML());
		response.setPageNum(nowPage);

		return response;
	}

	//게시글 정보 가져오기 
	public Notice.Response getBoard(int boardId, String cookieValue,
									HttpServletResponse response) throws Exception {
		//jdk 11이후 생김 
		String boardNum = "no" + boardId;

		if (!cookieValue.contains(boardNum)) {
			//게시글 카운트 증가 
			mapper.updateBoardCount(boardId);
			//현재 글번호를 이어서 붙인다.
			cookieValue += "_" + boardNum;
			Cookie coo = new Cookie("notice", cookieValue);
			coo.setMaxAge(60 * 60 * 6); //6시간 유효기간.
			response.addCookie(coo);
		}

		return mapper.getBoard(boardId);
	}


	public Map<String, Object> deleteNotice(int boardId) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		//지우기 전에 기존데이터를 가져온다.
		Notice.Response data = mapper.getBoard(boardId);
		int result = mapper.deleteNotice(boardId);
		if (result > 0) {
			Map<String, Object> param = new HashMap<>();
			param.put("boardId", data.getBoardId());
			param.put("fileType", "B");

			FileDTO file = mapper.getFileInfo(param);
			//파일 객체가 존재 한다면 진행
			if (file != null) {
				String path = file.getFilePath() + file.getFileStoredName();
				File oldFile = new File(path);
				//실제 파일이 존재하면 삭제
				if (oldFile.exists()) {
					oldFile.delete();
				}
				//실제 디비에서 삭제
				result = mapper.deleteBoardFile(boardId);
			}

			if (result > 0) {
				resultMap.put("resultCode", 200);
			} else {
				resultMap.put("resultCode", 500);
			}
		}else{
			resultMap.put("resultCode", 500);
		}
		return resultMap;
	}
	public int UpdateNotice(Notice.Update updateVO) throws Exception {

		int resultCode = 0;
		int result = mapper.updateNotice(updateVO);
		if (result < 1) {
			throw new Exception("게시글 수정 오류");
		}
		//수정할 파일이 있다.
		if (updateVO.getFile() != null && !updateVO.getFile().isEmpty()) {
			//기존 파일 삭제
			Map<String, Object> param = new HashMap<>();
			param.put("boardId", updateVO.getBoardId());
			param.put("fileType", "B");

			FileDTO file = mapper.getFileInfo(param);

			if(file !=null){
				String path = file.getFilePath() +file.getFileStoredName();
				File oldFile = new File(path);
				//실제 파일이 존재하면 삭제
				if(oldFile.exists()){
					oldFile.delete();
				}
				mapper.deleteBoardFile(updateVO.getBoardId());
			}
			//새로운 파일 저장
			FileDTO fileDTO = this.uploadBoardFile(updateVO.getFile(), updateVO.getBoardId());
			//새로운 파일 등록후 DB저장
			if (fileDTO !=null){
				result = mapper.insertFile(fileDTO);
			}
		}
		if (result > 0 ){
			resultCode = 200;
		}else {
			resultCode = 500;
		}
		return resultCode;
	}


	public Map<String, Object> createNotice(Notice.Request requestVO) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		//mapper에서 설정하여 insert 후 생성된 boardId를 requestVO 에 담아 준다.
		int result = mapper.createNotice(requestVO);

		if (result < 1) {
			throw new Exception("게시글 저장 오류");
		}
		//업로드된 파일 정보를 받아온다
		FileDTO fileDTO = this.uploadBoardFile(requestVO.getFile(), requestVO.getBoardId());
		if (fileDTO != null){
			result = mapper.insertFile(fileDTO);
		}
		if (result > 0) {
			resultMap.put("resultCode", 200);
		} else {
			resultMap.put("resultCode", 500);
		}
		return resultMap;
	}

	public FileDTO getFileInfo(Map<String, Object> param) throws Exception {
		return mapper.getFileInfo(param);
	}


	//파일을 저장하는 로직
	private FileDTO uploadBoardFile(MultipartFile file, int boardId) {
		FileDTO fileDTO = null;
		try {//파일 저장
			Map<String, Object> uploadedMap = CommonFileUpload.uploadFiles(file, filePath, "board");

			if (uploadedMap != null) {

				File newFile = (File) uploadedMap.get("newFile");
				String filePath = uploadedMap.get("filePath").toString();

				fileDTO = FileDTO
						.builder()
						.parentId(boardId)
						.fileOriginName(file.getOriginalFilename())
						.fileStoredName(newFile.getName())
						.filePath(filePath)
						.fileThumbName("")
						.fileThumbPath("")
						.fileType("B")
						.build();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileDTO;
	}


	//물리적인 파일 삭제
	private void deleteFile(FileDTO fileDTO) throws Exception {
		String path = fileDTO.getFilePath() + fileDTO.getFileStoredName();

		//원본파일을 경로를 통해 파일 객체로 만든다.
		File file = new File(path);

		if (file.exists()) {
			file.delete();
		}
	}
}
