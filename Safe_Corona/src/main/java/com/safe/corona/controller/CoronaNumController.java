package com.safe.corona.controller;

import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoronaNumController {

	private static final String DRIVER_PATH = "C:\\Workspaces\\Workspace_Python\\chromedriver.exe";
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

	public static WebDriver driver;
	public static WebElement element;

	public static String coronaNum() throws InterruptedException {

		Logger logger = LoggerFactory.getLogger(CoronaNumController.class);

		// Property 설정하기.
		System.setProperty(WEB_DRIVER_ID, DRIVER_PATH);

		ChromeOptions options = new ChromeOptions();

		// 크롬이 켜지는 것을 보기 위해
		// 보려면 주석처리 해도됨.
		options.addArguments("headless");

		driver = new ChromeDriver(options);

		driver.get(
				"https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=0&ie=utf8&query=%EC%BD%94%EB%A1%9C%EB%82%98+%ED%99%95%EC%A7%84%EC%9E%90");

		String KR_Contury = driver.findElement(By.cssSelector(".status_today .info_02 .info_num")).getText();
		logger.info("국내 확진자 수");
		logger.info(KR_Contury);

		String Other_Contury = driver.findElement(By.cssSelector(".status_today .info_03 .info_num")).getText();
		logger.info("해외 확진자 수");
		logger.info(Other_Contury);
		Thread.sleep(3000);
		logger.info("\n");
		logger.info("차트 누르기 " + driver.findElements(By.cssSelector("li[role=tab]")).get(7).findElement(By.tagName("a"))
				.findElement(By.tagName("span")).getText());
		logger.info("\n");
		driver.findElements(By.cssSelector("li[role=tab]")).get(7).findElement(By.tagName("a")).click();
		Thread.sleep(1000);
		// List<WebElement> chartlist =
		// driver.findElement(By.className("_x_axis_and_series_label")).findElements(By.className("column"));

		String Yd_Kr = driver.findElement(By.className("-bar_data")).findElements(By.cssSelector("div")).get(8)
				.getAttribute("data-local");
		String Yd_Other = driver.findElement(By.className("-bar_data")).findElements(By.cssSelector("div")).get(8)
				.getAttribute("data-oversea");
		String Yd_Total = driver.findElement(By.className("-bar_data")).findElements(By.cssSelector("div")).get(8)
				.getAttribute("data-total");

		logger.info("어제 국내 확진자 : " + Yd_Kr);
		logger.info("어제 해외 확진자 : " + Yd_Other);
		logger.info("어제 총 확진자 : " + Yd_Total);
		logger.info("\n");
		int KR_Compared = Integer.parseInt(KR_Contury) - Integer.parseInt(Yd_Kr);
		int Other_Compared = Integer.parseInt(Other_Contury) - Integer.parseInt(Yd_Other);
		int Total_Compared = Integer.parseInt(KR_Contury) + Integer.parseInt(Other_Contury)
				- Integer.parseInt(Yd_Total);
		
		logger.info("전날대비 국내 확진자 : " + KR_Compared);
		logger.info("전날대비 해외 확진자 : " + Other_Compared);
		logger.info("전날대비 총 확진자 : " + Total_Compared);
		
		String KR_PM = (KR_Compared > 0) ? "▲"+KR_Compared : (KR_Compared < 0) ? "▼"+Math.abs(KR_Compared) : "-";
		String Other_PM = (Other_Compared > 0) ? "▲"+Other_Compared : (Other_Compared < 0) ? "▼"+Math.abs(Other_Compared) : "-";
		String Total_PM = (Total_Compared > 0) ? "▲"+Total_Compared : (Total_Compared < 0) ? "▼"+Math.abs(Total_Compared) : "-";

		Calendar cal = Calendar.getInstance();
		String result = "";
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH) + 1;
		int day = cal.get(Calendar.DATE);

		if (day - 1 == 0) {
			month -= -1;
			if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				day = 31;
			} else if (month == 4 || month == 6 || month == 9 || month == 11) {
				day = 30;
			} else if (month == 2) {
				if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
					day = 29;
				} else {
					day = 28;
				}
			} else {
				// 1월 1일일 경우
				// month가 0이 될거임
				// 그때는 작년으로 돌아간다
				year -= -1;
				month = 12;
				day = 31;
			}

			result = String.format("[ %d년 %d월 %d일 ], 국내발생 : %s명 (%s), 해외발생 : %s명 (%s), 총합 : %d명 (%s)", year, month,
					day, KR_Contury, KR_PM, Other_Contury, Other_PM,
					Integer.parseInt(KR_Contury) + Integer.parseInt(Other_Contury), Total_PM);
			logger.info(result);
		} else {
			result = String.format("[ %d년 %d월 %d일 ], 국내발생 : %s명 (%s), 해외발생 : %s명 (%s), 총합 : %d명 (%s)", year, month,
					day-1, KR_Contury, KR_PM, Other_Contury, Other_PM,
					Integer.parseInt(KR_Contury) + Integer.parseInt(Other_Contury), Total_PM);
			logger.info(result);
		}

		return result;
	}

}
