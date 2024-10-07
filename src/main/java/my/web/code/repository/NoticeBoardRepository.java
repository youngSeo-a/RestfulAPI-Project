package my.web.code.repository;

import my.web.code.entity.NoticeBoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoardEntity , Integer> {
    //Integer 가 들어간 이유는 PK(boardId)가 int 이기 떄문에 객체로 받으려면 Integer를 넣어야 하기 떄문

}
