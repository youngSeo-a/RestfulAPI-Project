
package my.web.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GenerateSecuredPassword {

    public static void main(String[] args) {
        SpringApplication.run(GenerateSecuredPassword.class, args);

        //암호화 객체 생성
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "1234";

        //암호화
        String securedPassword = passwordEncoder.encode(password);
        System.out.println("encode passwd : "+ securedPassword);
        //패스워드 비교(원본, 암호화된 패스워드)
        System.out.println("matched : "+ passwordEncoder.matches(password , securedPassword));

    }
}
