package my.web.code.controller.main;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping("/main")
    public String mainView(Model model){
        model.addAttribute("param1","casca"); model.addAttribute("param2","vgtr");

        return "views/main";
    }

//    @PostMapping("/main")
//    public String mainView(Model model, String param1, String param2){
//        System.out.println(param1);
//        System.out.println(param2);
//        model.addAttribute("param1",param1); model.addAttribute("param2",param2);
//
//        return "views/main";
//    }

}
