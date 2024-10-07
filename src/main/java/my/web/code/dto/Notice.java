package my.web.code.dto;

import java.util.List;

import lombok.Data;
import my.web.code.entity.NoticeBoardEntity;
import org.springframework.web.multipart.MultipartFile;

public class Notice {

	
	@Data
	public static class Response{
		
		private int boardId;
		private String boardTitle;
		private String boardContents;
		private String boardWriter;
		private int boardCount;
		private int fileId;
		private String fileOriginName;
		private String regDate;
		private String updateDate;
		
		//테이블에서 객체로 데이터 이동
		public static Response of (NoticeBoardEntity entity){
			Response res = new Response();
			res.setBoardId(entity.getBoardId());
			res.setBoardTitle(entity.getBoardTitle());
			res.setBoardCount(entity.getBoardCount());
			res.setBoardId(entity.getBoardId());
			res.setBoardContents(entity.getBoardContents());
			res.setBoardWriter(entity.getBoardWriter());

			return res;
		}
	}
	
	
	@Data
	public static class Update{
		private int boardId;
		private int nowPage;
		private String boardTitle;
		private String boardContents;
		private MultipartFile file;
	}
	
	
	@Data
	public static class Request{
		private int boardId;
		private String boardTitle;
		private String boardContents;
		private String boardWriter;
		private MultipartFile file;

	}
	
	
	@Data
	public static class DataResponse{
		private int total;
		private List<Response> boardList;
		private String pageHTML;
		private int pageNum;
	}
}
