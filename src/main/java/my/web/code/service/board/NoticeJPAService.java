package my.web.code.service.board;

import lombok.RequiredArgsConstructor;
import my.web.code.dto.Notice;
import my.web.code.entity.NoticeBoardEntity;
import my.web.code.repository.NoticeBoardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeJPAService {
    private  final NoticeBoardRepository repository;

    public List<Notice.Response> getBoardList(){
        //JPA를 이용하여 테이블 전체 데이터 조회
        List<NoticeBoardEntity> list = repository.findAll(); //findAll()은 객체를 다 가져 오겠다.!!

        //Steam(jdk1.8 에서 collection 에 추가된 기능)
        //list.stream().map(obj->{return Notice.Response.of(obj);}).toString();
        List<Notice.Response> boardList =
        list.stream().map(Notice.Response::of) // entity => response 객체로 변환해서 리턴
                .collect(Collectors.toList()); //모아서 다시 list로 출력
        return  boardList;
    }

}
