package my.web.code.controller.board;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;
import my.web.code.dto.FileDTO;
import my.web.code.dto.Notice;
import my.web.code.service.board.NoticeService;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Slf4j
public class NoticeAPIController {

	private final NoticeService service;
	private final HttpSession session;
	//http://localhost:9090/api/v1/notice/1
	@DeleteMapping("/notice/{boardId}")
	public ResponseEntity<Map<String, Object>> deleteBoard(@PathVariable("boardId") int boardId){
	
		Map<String, Object> resultMap = null;
		try {
			
			resultMap = service.deleteNotice(boardId);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@PostMapping("/notice")
	public ResponseEntity<Map<String, Object>> createNotice(
			                                          @ModelAttribute Notice.Request requestVO) {
		Map<String, Object> resultMap = null;
		try {
			
			requestVO.setBoardWriter("admin");
			resultMap = service.createNotice(requestVO);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}

	@GetMapping("/file/{boardId}/{fileType}")
	public ResponseEntity<UrlResource> downFile(@PathVariable("boardId") int boardId, @PathVariable("fileType") String fileType){

		Map<String, Object> param = new HashMap<String, Object>();
		param.put("boardId", boardId);
		param.put("fileType",fileType);

		//다운로드 할 리소스(파일)를 담을 객체
		UrlResource res = null;
		HttpHeaders headers = new HttpHeaders();
		try {
			FileDTO fileDTO = service.getFileInfo(param);
			String fullPath = fileDTO.getFilePath() +fileDTO.getFileStoredName();

			//파일 객체 생성
			File file = new File(fullPath);
			if (!file.exists()) {
				throw new FileNotFoundException(fullPath + "경로에 파일이 존재하지 않습니다.");
			}


			//존재 하면 전송
			//mimeType >> 파일의 타입
			String mimeType = Files.probeContentType(Paths.get(file.getAbsolutePath()));

			if (mimeType == null) {
				//기본 이진 파일로 설정
				mimeType = "application/octet-stream";
			}
			//경로로 부터 파일을 전송받아 객체에 넣기
			res = new UrlResource(file.toURI());

			//경로오류 방지 위한 방법 >>
			//한글의 경우 암호화(바이트코드화)해서 데이터를 보내야함 근데 + 기호는 경로에서 인식을 잘못함.
			//바이트 코드로 변환
			String encodedFile = URLEncoder.encode(fileDTO.getFileOriginName(), "UTF-8").replace("+" , "%20");


			// http 헤더세팅
			// 파일 다운로드 양식으로 받을 수 있또록
			headers.set("Content-Disposition", "attachment; filename" +encodedFile + ";filename*=UTF-8" +encodedFile);
			headers.setCacheControl("no-cache");

			//다운로드할 파일의 타입
			headers.setContentType(MediaType.parseMediaType(mimeType));

		}catch (Exception e) {
			log.error("Error file Download: {}" , e.getMessage()==null ?"" : e.getMessage());
			e.printStackTrace();
		}
		return new ResponseEntity<>(res, headers, HttpStatus.OK);
	}
	
}
