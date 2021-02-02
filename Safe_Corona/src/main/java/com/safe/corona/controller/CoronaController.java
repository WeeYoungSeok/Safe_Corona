package com.safe.corona.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CoronaController {

	@RequestMapping("/")
	@Scheduled(cron="0 10 10 * * *")
	public String index() throws InterruptedException {
		String msg = CoronaNumController.coronaNum();
		Kakao.sendMessage(msg);
		return "index";
	}

}
