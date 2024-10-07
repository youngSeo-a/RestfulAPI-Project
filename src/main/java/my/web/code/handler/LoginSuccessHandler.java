package my.web.code.handler;


import my.web.code.dto.UserDetailDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
* 
* */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    //http 요청 기억 객체
    private RequestCache requestCache = new HttpSessionRequestCache();
    //되돌려주기 정책
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public void SetRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {


        //기본 경로 설정
        setDefaultTargetUrl("/main");
        //인증된 객체를 시큐리티에서 가져오기
        //사용자가 어떤 객체에 UserDetails 를 상속하여 사용자 객체를 만들지 시스템은 모르기 떄문에
        //유저 객체를 저장할 때는 Object 타입으로 저장한다.
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //우리는 알기때문에 유저객체를 사용한 것에 맞게 형변환 한다.
       UserDetailDTO user = (UserDetailDTO)principal;

        //http 세션에 별도 저장 - 저장하는 이유는 타임리프가 시큐리티 객체 접근에 오류가 좀 있어서
        //세션에서 사용자 정보를 꺼내는게 편해서 저장하는것... 원래는 안해도 되는 옵션 기능
        request.getSession().setAttribute("userInfo", user);

        //저장된 이전 리퀘스트 객체 가져오기
        SavedRequest savedRequest = requestCache.getRequest(request, response);
        //이전에 보던 페이지가 있을 경우
        if (savedRequest != null) {
            String targetURI = savedRequest.getRedirectUrl();
            redirectStrategy.sendRedirect(request, response, targetURI);
        }else{
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
