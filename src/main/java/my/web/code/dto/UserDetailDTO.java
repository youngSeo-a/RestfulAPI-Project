package my.web.code.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailDTO implements UserDetails{

    private final String ROLE_PREFIX = "ROLE_";
    private String username; //실제 아이디
    private String loginUserName;//로그인 사용자 이름
    private String password;
    private String userAuth;
    private String roleName;


    //사용자가 가지는 권한
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(ROLE_PREFIX+userAuth));
        return auths;
    }

    public String getLoginUserName() {
        return loginUserName;
    }

    public void setLoginUserName(String loginUserName) {
        this.loginUserName = loginUserName;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    //시큐리티에서는 사용자 id를 의미
    @Override
    public String getUsername() {
        return this.username;
    }
    
    //계정 유효한가
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    //계정이 잠기지 않았는가
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    //비밀번호 변경기간이 지나지 않았는가 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    //사용 가능한 계정인가
    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserAuth() {
        return userAuth;
    }
}
