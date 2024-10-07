package my.web.code.utils;

import ch.qos.logback.core.pattern.FormatInfo;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.UUID;

public class ImageResizeUtil {
    public static String makeThumbnailFile(int width, int height, File originFile, String thumbPath){

        String thumbFileName = "";
        //파일 읽어오기 위한 스트림 선언
        FileInputStream in = null;
        BufferedInputStream bf = null;

        try{
            //파라메터로 넘어온 객체는 정상인지 꼭 확인
            if(originFile != null) {
                //원본파일 이름 가져오기
                String originFileName = originFile.getName();
                //확장자 얻기
                String ext = originFileName.substring(originFileName.lastIndexOf(".") +1);
                //저장되는 파일이름이 겹치지 않도록 중복이 어려운 UUID 이용하여 만든다
                //uuid 는 하이픈(-) 이 들어가기 때문에 제거한다.
                String uuId = UUID.randomUUID().toString().replaceAll("-", "");
                uuId = uuId.substring(0, 16); //길이가 길어서 16자로 자른다.
                //저장할 파일 이름 완성
                thumbFileName = uuId +"."+ ext;

                in = new FileInputStream(originFile);
                bf = new BufferedInputStream(in);

                //원본파일을 리사이즈 하기위해 메모리에 복사
                BufferedImage originImg = ImageIO.read(bf);
                //사이즈 축소할 객체 선언
                MultiStepRescaleOp scalImage = new MultiStepRescaleOp(width, height);
                //마스킹 처리 (화면 선명하게하거나...흐리게하거나...그런거)
                //그냥 브드러운 이미지 처리
                scalImage.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
                //리사이즈 이미지 생성
                BufferedImage resizedImage = scalImage.filter(originImg, null);
                //썸네일 저장할 경로를 만든다
                String fullPath = thumbPath + thumbFileName;
                //(빈)파일객체 만들기
                File newThumbFile = new File(fullPath);

                //파일 경로가 존재하지 않으면 만든다.
                if( !newThumbFile.getParentFile().exists()) {
                    newThumbFile.getParentFile().mkdirs(); //파일경로 생성
                }

                //메모리에 있는 리사이즈된 이미지를 실제 파일로 생성
                boolean isWrite = ImageIO.write(resizedImage, ext, newThumbFile);

                if(!isWrite){
                    throw new Exception("Image resize is Error");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();

        }finally {
            try{
                if( bf != null){
                    bf.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (Exception e2){
                e2.printStackTrace();;
            }
        }

        return thumbFileName;
    }
}
