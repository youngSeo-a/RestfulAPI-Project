package my.web.code.controller.gallery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
@RequestMapping("/gall" )
public class GalleryController {
    @GetMapping("/list")
        public ModelAndView listView(){
            ModelAndView view = new ModelAndView();
            view.setViewName("views/gall/galleryList");
            return view;
        }
}
