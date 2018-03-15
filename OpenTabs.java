package hate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class OpenTabs {
	
	static WebDriver driver;
	JavascriptExecutor jse;
	
	public static void main(String[] args) {

		ArrayList<String> tabs = null;
		String[] tests = {""
		};
		
		String path = "";
		System.setProperty("webdriver.chrome.driver", path);
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window(); // maximize the window
		driver.manage().window().maximize();
		
		for (int i = 0; i < tests.length; i++) {
			System.out.println("---------------------");
			System.out.println(tests[i]);
			
			// Prints the open tabs
			tabs = new ArrayList<String>(driver.getWindowHandles());
//			String listString = String.join(", ", tabs);
//			System.out.println(listString);
			System.out.println("There are currently: " + tabs.size() + " tabs open.");
			
			
			if(i == 0) {
				
				String username = "";
				String pass = "";
			
				driver.get("https://jira.vasco.com/browse/" + tests[i]);
				
				driver.findElement(By.id("login-form-username")).sendKeys(username);
				driver.findElement(By.id("login-form-password")).sendKeys(pass);
				driver.findElement(By.id("login-form-submit")).click();
				driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 	// synchronize the lines of code + page --
				driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);	// wait for the page to load
							
			}
			else {
				// Opens a new tab
				System.out.println("Opening a new tab:");
				((JavascriptExecutor)driver).executeScript("window.open();");
				tabs = new ArrayList<String>(driver.getWindowHandles());
				System.out.println(tabs.size());
			
				driver.switchTo().window(tabs.get(tabs.size()-1));
				driver.navigate().to("https://jira.vasco.com/browse/" + tests[i]);					
			}
		}
		
		System.out.println("Finished.");
	}
	

}
