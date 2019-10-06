package cc.eabour.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/testapi/v1")
public class HelloController {
	
	@RequestMapping("/hello")
	public String hello() {
		return "hello";
	}
}
