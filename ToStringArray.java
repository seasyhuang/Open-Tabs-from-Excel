package hate;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class ToStringArray {
	
	static WebDriver driver;
	public static void main(String[] args) {
		
		String input = "APP-1734\r\n" + 
				"APP-1735\r\n" + 
				"APP-1736\r\n" + 
				"APP-1737\r\n" + 
				"APP-1738\r\n" + 
				"APP-1739\r\n" + 
				"APP-1740\r\n" + 
				"APP-1741\r\n" + 
				"APP-1742";
		
		String excel = convert(input);
		System.out.println(excel);
		
		List<String> temp = Arrays.asList(excel.split(","));
		System.out.println(temp);
		
		Function<String,String> addQuotes = s -> "\n\"" + s + "\"";

		String result = temp.stream()
		  .map(addQuotes)
		  .collect(Collectors.joining(", "));
		
		System.out.println("---------------");
		
		System.out.println(result);
		
	}
	
	public static String convert(String input) {
		
		String path = "";
		System.setProperty("webdriver.chrome.driver", path);
		driver = new ChromeDriver();
		driver.manage().deleteAllCookies();
		driver.manage().window(); // maximize the window
		driver.manage().window().maximize();
		driver.get("https://delim.co/#");
		
		WebElement in = driver.findElement(By.cssSelector("[ng-model=\"settings.docsv_string\"]"));
		in.click();
		in.sendKeys(input);
		
		driver.findElement(By.cssSelector("[ng-click=\"addCommas(settings)\"]")).click();
		WebElement take = driver.findElement(By.cssSelector("[name=\"undo_csv\"]"));
		take.click();
		
		String output = take.getText();
		driver.close();
		return output;
	}

}
