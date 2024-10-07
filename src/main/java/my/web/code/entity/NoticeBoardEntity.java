package my.web.code.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

//@Data를 쓰면 toString으로 불러올때 에러 뜰수도 있어서 안부름
@Entity(name = "notice_board") //테이블 네임을 넣어주면 됨
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NoticeBoardEntity {
    @Id() //기본키
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private int boardId;
    private String boardTitle;
    private String boardContents;
    private int boardCount;
    private String boardWriter;
    private LocalDateTime regDate; //LocalDateTime은 자바 8부터 시간을 정확하게 표시할때 씀
    private LocalDateTime updateDate;


}
