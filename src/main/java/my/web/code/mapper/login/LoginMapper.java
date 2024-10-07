package my.web.code.mapper.login;

import my.web.code.dto.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LoginMapper {
    UserVO getLoginUser(@Param("userId") String userId);

}
