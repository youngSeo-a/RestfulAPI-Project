
package my.web.code.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import javax.servlet.MultipartConfigElement;
import java.io.File;

//Servlet 관련 설정
@Configuration
public class WebServletConfig implements WebMvcConfigurer {


    @Value("${server.stored.file.path}")
    private String filePath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

      String galleryRealPath = filePath + File.separator+ "gallery" + File.separator;
        //외부경로를 스프링 내부경로로 바꿔서 등록한다.
        //그럼 외부경로를 우회하여 만들어진 내부경로를 통해 이미지 접근 가능
        registry.addResourceHandler("/gallery/images/**") //우리가 사용할 경로
                .addResourceLocations("file:///" + galleryRealPath) //사용경로와 매칭되는 진짜 경로
                .setCachePeriod(0)
                .resourceChain(true)
                .addResolver(new PathResourceResolver());

    }

    /*
     인코딩 설정
     Bean 등록
     */
    @Bean
    public CharacterEncodingFilter characterEncodingFilter(){
        CharacterEncodingFilter filter = new CharacterEncodingFilter();
        filter.setEncoding("UTF-8");
        filter.setForceEncoding(true); //사용자가 정한 인코딩을 강제적용
        return filter;
    }



    /*
     파일 업로드 설정
     파일크기제한
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(){
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.of(100, DataUnit.MEGABYTES));
        factory.setMaxRequestSize(DataSize.of(100, DataUnit.MEGABYTES));

        return factory.createMultipartConfig();
    }

    @Bean
    public MultipartResolver multipartResolver(){
        return new StandardServletMultipartResolver();
    }

}
