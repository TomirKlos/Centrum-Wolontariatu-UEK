package pl.krakow.uek.centrumWolontariatu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by MSI DRAGON on 2017-12-03.
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/")
    public String home(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "thymeleaf/home";
    }
}
