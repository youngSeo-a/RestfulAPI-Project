//package my.web.code.controller.board;
//
//
//import lombok.RequiredArgsConstructor;
//import my.web.code.dto.Notice;
//import my.web.code.service.board.NoticeJPAService;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//public class BoardJPAController {
//    private final NoticeJPAService service;
//
//    @GetMapping("/board/jpa/list")
//    public List<Notice.Response> getBoardList() {
//        return service.getBoardList();
//    }
//}
