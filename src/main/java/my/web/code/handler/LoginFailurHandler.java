package my.web.code.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;

public class LoginFailurHandler implements AuthenticationFailureHandler{

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        //응답하는 타입이 html 문서이다.
        response.setContentType("text/html; charset=UTF-8");
        //document 에 문서 쓰는 객체선언
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<meta charset=\"UTF-8\">");
        out.println("<html lang='en'>");
        out.println("  <body>");
        out.println("<script>");
        out.println("alert('아이디 또는 패스워드가 틀렸습니다.');");
        out.println("<location.href='/login'>");
        out.println("</script>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}


