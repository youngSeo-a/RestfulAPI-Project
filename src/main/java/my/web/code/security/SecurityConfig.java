package my.web.code.security;

import lombok.RequiredArgsConstructor;
import my.web.code.handler.LoginFailurHandler;
import my.web.code.handler.LoginSuccessHandler;
import my.web.code.handler.LogoutSuccHandler;
import my.web.code.service.login.LoginUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.HttpSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity //시큐리티를 어노테이션으로 함
@RequiredArgsConstructor
public class SecurityConfig {

    //로그인 서비스 의존성 주입
    private final LoginUserService userService;
    @Bean
    public WebSecurityCustomizer configure(){
      return  (w) -> w.ignoring() //dist문자처리 뜻 filter는 모든것들을 다 보지만 antMatcher를 넣으면 보지말라는 뜻
              .antMatchers("/", "/webjars/**", "/dist/**")
              .antMatchers("/js/**","/css/**" ,"/gallery/images/**")
              .antMatchers("/login/**","/img/**","images/**", "/main","/board/jpa/**");
        }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().
                disable()
                .authorizeHttpRequests()//GET,PUT,POST,DELETE >>Method 설정
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated() //모든 요청은 로그인 하에 허락.
                .and()
                .formLogin()
                .loginPage("/login")//기본페이지말고, 사용자가 지정한 페이지경로
                .loginProcessingUrl("/loginProc") //로그인 버튼을 눌렀을 때, 실제로그인처리 하도록 하는 경로
                .successHandler(new LoginSuccessHandler()) //로그인 성공 시 처리
                .failureHandler(new LoginFailurHandler()) //로그인 실패 시 처리
                .and()
                .logout()
                .invalidateHttpSession(true) //세션 정보 삭제
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true) //로그인되면 시큐리티가 만드는 인증객체를 삭제
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) //로그아웃이 실행되는 url
                .logoutSuccessHandler(new LogoutSuccHandler()).permitAll();

        return http.build();
    }

    //Authenticationprovider 한테 우리가 만든 UserDetialService를 전달해야한다.
    //그래야 우리가 원하는 DB에서 데이터를 가져와
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//
        provider.setPasswordEncoder(this.bcryptPasswordEncoder());
//
        provider.setUserDetailsService(userService);

        return provider;
    }
    @Bean
    public BCryptPasswordEncoder bcryptPasswordEncoder(){

        return new BCryptPasswordEncoder();
    }
}
