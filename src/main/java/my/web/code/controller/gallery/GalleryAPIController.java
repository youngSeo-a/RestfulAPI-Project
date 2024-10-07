package my.web.code.controller.gallery;

import lombok.RequiredArgsConstructor;
import my.web.code.dto.Gallery;
import my.web.code.service.gallery.GalleryService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class GalleryAPIController {

    //서비스 의존성 주입
    private final GalleryService service;

    @GetMapping("/gall")
    public Gallery.Response galleryList(@RequestParam (name="nowPage", defaultValue = "0") int nowPage){
        Gallery.Response response = null;
        Map<String, Object> param = new HashMap<>();
        param.put("nowPage", nowPage);
        try {
            response = service.getGalleryList(param);
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }

    @PostMapping("/gall")
    public Map<String, Object> createGallery(Gallery.RequestDTO requestDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        requestDTO.setGallWriter("Admin");
        try{
            resultMap = service.createGallery(requestDTO);

        }catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
        }
        return resultMap;
    }

    @PutMapping("/gall")
    public Map<String, Object> updateGallery(Gallery.RequestDTO requestDTO) {
        Map<String, Object> resultMap = new HashMap<>();
        requestDTO.setGallWriter("Admin");
        try{
            resultMap = service.updateGallery(requestDTO);

        }catch (Exception e) {
            e.printStackTrace();
            resultMap.put("resultCode", 500);
        }
        return resultMap;

    }

    @DeleteMapping("/gall")
    public  Map<String, Object> deleteGallery(@RequestParam("deleteTarget") String deleteTarget){
        Map<String, Object> resultMap = new HashMap<String, Object>();

        try {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("delTargets", deleteTarget);

            int result = service.deleteGallery(param);

            if (result > 0){
                resultMap.put("resultCode", 200);
            }else{
                resultMap.put("resultCode", 500);
            }
        }catch (Exception e){
            e.printStackTrace();
            resultMap.put("resultCode", 500);
        }
        return resultMap;
    }
}
