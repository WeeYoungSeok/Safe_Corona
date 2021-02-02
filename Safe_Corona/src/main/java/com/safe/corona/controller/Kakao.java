package com.safe.corona.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Kakao {

	private static final String DRIVER_PATH = "C:\\Workspaces\\Workspace_Python\\chromedriver.exe";
	private static final String WEB_DRIVER_ID = "webdriver.chrome.driver";

	public static WebDriver driver;
	public static WebElement element;
	
	private static String kakao_id = "아이디";
	private static String kakao_pw = "비밀번호";
	
	
	public static void sendMessage(String msg) throws InterruptedException {
		Logger logger = LoggerFactory.getLogger(Kakao.class);
		
		// 확진자 수 메세지를 만든것을 ,로 잘라서 배열에 담음
		String[] msgs = msg.split(",");
		
		System.setProperty(WEB_DRIVER_ID, DRIVER_PATH);

		ChromeOptions options = new ChromeOptions();

		// 크롬이 켜지는 것을 보기 위해
		// 보려면 주석처리 해도됨.
		// options.addArguments("headless");

		driver = new ChromeDriver(options);
		
		// 자신의 카카오 채널을 들어가기 위해 주소를 받아옴
		driver.get("https://center-pf.kakao.com/_EbbeK/chats");
		
		// options.addArguments("headless");
		
		// 주소가 켜지면 로그인을 해야함
		// id입력하는 곳을 클릭해야지만 input에 값이 들어감
		// 카카오에서 이렇게 만듬
		driver.findElement(By.cssSelector("label[for=id_email_2]")).click();
		driver.findElement(By.cssSelector("input[validator=email_or_phone_or_kakaoid")).sendKeys(kakao_id);
		
		driver.findElement(By.cssSelector("label[for=id_password_3]")).click();
		driver.findElement(By.cssSelector("input[validator=password")).sendKeys(kakao_pw);
		
		driver.findElement(By.className("btn_confirm")).click();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		
		// 서버가 켜지는 동안
		// 브라우저가 스위치가 될경우
		// 서버가 꺼지기 전까지는 스위치된 브라우저를 인식하려고한다.
		// 고로 이 메소드가 실행될때 처음 열린 페이지로 돌아와야한다.
		// 이것을 변수에 담아줘야지 고정이 됨
		
		String parent_browser = driver.getWindowHandle();
		
		driver.switchTo().window(parent_browser);
		
		
		// 채팅을 보낼 목록이 여러개 이므로
		// 리스트에 담아서 가져온다.
		List<WebElement> elements = driver.findElements(By.className("item_info"));
		

		// 카카오 채널에서 메세지를 보낼경우 
		// 그 사람이 제일 최상위로 올라온다.
		// 고로 목록만큼 돌면서 가장 마지막번지에 있는 사람에게만 메세지를 보내면된다.
		// 근데 이렇게하면 클릭하고 보내고 클릭하고 보내고를 해야하는데
		// 셀레니움 특성상 로드가 다 된 뒤에 찾아야하므로 시간적으로 오래걸린다.
		
		// 그렇게해서 미리 다 클릭해버리고 메세지를 보내면 된다.
		for (int i = 0; i < elements.size(); i++) {
			// 한명 클릭하고 
			element = elements.get(i);
			Thread.sleep(500);
			element.click();
		}
		
		// 다 클릭한 뒤에 마지막으로 클릭된 사람이
		// 로드되길 기다린다.
		
		Thread.sleep(3000);
		
		int cnt = 0;
		// cnt를 0으로 한 이유는
		
		// 여기서 열린 창을 모조리 가져와서 forEach문을 통해
		// 하나씩 가져와서 메세지를 보내줄건데
		// 처음으로 가져와지는 브라우저는 채팅목록 브라우저다
		// 그래서 cnt를 선언해서 처음으로 나온 창에서는 아무행동을 안해주기위해
		// 선언했다 (왜 굳이 forEach냐? driver.getWindowHandles 리턴이 set이기때문)
		for (String winHandle : driver.getWindowHandles()) {
			if (cnt > 0) {
				// 자식으로 바꾸어준다.
				driver.switchTo().window(winHandle);
				
				// 채팅칠 수 있는 텍스트에어리어를 찾아준다.
				element = driver.findElement(By.tagName("textarea"));
				
				// 값들을 입력해준다
				element.sendKeys(msgs[0]);
				// 줄바꿈
				element.sendKeys(Keys.LEFT_SHIFT, Keys.ENTER);
				element.sendKeys(msgs[1]);
				element.sendKeys(Keys.LEFT_SHIFT, Keys.ENTER);
				element.sendKeys(msgs[2]);
				element.sendKeys(Keys.LEFT_SHIFT, Keys.ENTER);
				element.sendKeys(msgs[3]);
				element.sendKeys(Keys.ENTER);
			} else {
				// 처음 브라우저는
				// 채팅목록이므로 처음 브라우저는 넘겨버린다.
				cnt++;
			}
		}
		logger.info("카카오채널 메세지 전송 성공");
	
	}
}
