package io.github.zufarm.library.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/people")
public class PersonController {
	
	@GetMapping()
	public String showPeople() {
        
        return "people/all";
    }
}