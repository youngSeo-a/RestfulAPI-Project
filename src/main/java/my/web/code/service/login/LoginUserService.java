package my.web.code.service.login;

import lombok.RequiredArgsConstructor;
import my.web.code.dto.UserDetailDTO;
import my.web.code.dto.UserVO;
import my.web.code.mapper.login.LoginMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginUserService implements UserDetailsService {

    private final LoginMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO vo = mapper.getLoginUser(username);
        return UserDetailDTO
                .builder()
                .username(vo.getUserId())
                .password(vo.getUserPasswd())
                .loginUserName(vo.getUserName())
                .userAuth(vo.getUserRole())
                .roleName(vo.getRoleName())
                .build();
    }
}
