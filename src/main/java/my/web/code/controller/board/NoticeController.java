package my.web.code.controller.board;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import my.web.code.dto.Notice;
import my.web.code.service.board.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;


@Controller
@RequiredArgsConstructor //생성자를 통해서 필수값만 파라메터로 받는다.
@RequestMapping("/notice")
public class NoticeController {
	
	private final NoticeService service; //생성자를 통해 DI
	
	@GetMapping("/list")
	public ModelAndView getBoardList(@RequestParam(name="nowPage", defaultValue = "0") int nowPage) {
		//보여줄 화면과 전송할 데이터를 담는 객체 
		ModelAndView view = new ModelAndView();
		try {
			
			//파라메터 객체생성 
			Map<String, Object> param = new HashMap<>();
			param.put("nowPage", nowPage);
			//서비스로부터 결과 객체를 받는다 
			Notice.DataResponse response = service.getBoardList(param);
			//전송할 데이터를 key , value 로 담는다. 
			view.addObject("data", response);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		view.setViewName("views/board/boardList"); //이동할 화면경로 
		
		return view;
	}
	
	@GetMapping("/detail")
	public ModelAndView getBoard(@RequestParam(name="boardId") int boardId,
			                     @CookieValue(name = "notice", defaultValue = "") String cookieValue,
			                     @RequestParam(name="nowPage", defaultValue = "0") int nowPage,
			                     HttpServletResponse response) {
		
		ModelAndView view = new ModelAndView();
		view.addObject("nowPage", nowPage);
		try {
			
			Notice.Response data = service.getBoard(boardId, cookieValue, response);
			view.addObject("data", data);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		view.setViewName("views/board/boardDetail");
		return view;
	}
	
	@GetMapping("/detail/update")
	public ModelAndView getBoardUpdate(@RequestParam(name="boardId") int boardId,
			                     @CookieValue(name = "notice", defaultValue = "") String cookieValue,
			                     @RequestParam(name="nowPage", defaultValue = "0") int nowPage,
			                     HttpServletResponse response) {
		
		ModelAndView view = new ModelAndView();
		view.addObject("nowPage", nowPage);
		try {
			
			Notice.Response data = service.getBoard(boardId, cookieValue, response);
			view.addObject("data", data);
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		view.setViewName("views/board/boardUpdate");
		return view;
		
	}

	@PostMapping("/detail/update")
	public ModelAndView updateBoard(@ModelAttribute Notice.Update updateVO) {
		ModelAndView view = new ModelAndView();
		int resultCode = 0;
		
		try {
			resultCode = service.UpdateNotice(updateVO);
		}catch (Exception e) {
			e.printStackTrace();
			resultCode = 500;
		}
		//화면경로가 아니라 url 경로로 리다이렉트 한다.
		view.setViewName("redirect:/notice/list");
		return view;
	}
	
	
	@GetMapping("/new")
	public ModelAndView createNotice() {
		ModelAndView view = new ModelAndView();
		view.setViewName("views/board/boardWrite");
		return view;
	}
	
}
