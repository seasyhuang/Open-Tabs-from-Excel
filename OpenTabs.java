package hate;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.collections4.functors.ChainedClosure;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class OpenTabs {
	
	static WebDriver _driver;
	JavascriptExecutor _jse;
	
	public static void main(String[] args) {

		ArrayList<String> tabs = null;
		String[] tests = {
//				"PB-9655"
		};

		String path = "";
		System.setProperty("webdriver.chrome.driver", path);
		_driver = new ChromeDriver();
		_driver.manage().deleteAllCookies();
		_driver.manage().window(); // maximize the window
		_driver.manage().window().maximize();
		
		for (int i = 0; i < tests.length; i++) {
			System.out.println("---------------------");
			System.out.println(tests[i]);
			
			// Prints the open tabs
			tabs = new ArrayList<String>(_driver.getWindowHandles());
			System.out.println("There are currently: " + tabs.size() + " tabs open.");
			
			if(i == 0) {
				// First tab: need to log in
				String username = "";
				String pass = "";
			
				_driver.get("https://jira.vasco.com/browse/" + tests[i]);
				
				_driver.findElement(By.id("login-form-username")).sendKeys(username);
				_driver.findElement(By.id("login-form-password")).sendKeys(pass);
				_driver.findElement(By.id("login-form-submit")).click();
				_driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); 	// synchronize the lines of code + page --
				_driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);	// wait for the page to load

			}
			else {
				// Opens a new tab
				System.out.println("Opening a new tab:");
				((JavascriptExecutor)_driver).executeScript("window.open();");
				tabs = new ArrayList<String>(_driver.getWindowHandles());
				System.out.println(tabs.size());
				
				_driver.switchTo().window(tabs.get(tabs.size()-1));
				_driver.navigate().to("https://jira.vasco.com/browse/" + tests[i]);					
			}
		}
		
		// Deleting labels, one tab at a time. Tried deleting the labels as 
		// the tabs were being opened, but for some reason it was really slow
		for (int i = 0; i < tabs.size(); i++) {
			System.out.println("---------------------");
			System.out.println("Switching to tab: " + i);
			
			_driver.switchTo().window(tabs.get(i));
			openLabels();
//			editLabels();
			addLabel("ClassicUI");
			saveLabels();
			
//			List<WebElement> _labelList = getLabelListOpen();
//			if(!labelExists(_labelList, "NewUI")) { 
//				addLabel("NewUI");
//			}
		}
		/*
 */		
				
		System.out.println("---------------------");
		System.out.println("Finished.");
	}
	
//	public static void excel() throws Exception {
//		File src = new File("P:\\Documents\\Excel_Data\\Tests.xlsx");				// apache POI
//		FileInputStream fis = new FileInputStream(src);								// pass src into fis 
//		XSSFWorkbook wb = new XSSFWorkbook(fis);									// this line loads the workbook (XML SpreadSheet Format)
//		XSSFSheet sheet1 = wb.getSheetAt(0);										// accesses sheet 1 on excel
//	}
	
	public static List<WebElement> getLabelListClosed() {
		
		By _selector = By.cssSelector("[class=\"labels\"]");
		By _labelSelector = By.cssSelector(".labels li");
		WebElement labelElem = _driver.findElement(_selector);
		
		List<WebElement> labelList = labelElem.findElements(_labelSelector);
		labelList.remove(labelList.size()-1);
		
        return labelList;
	}
	
	public static List<WebElement> getLabelListOpen() {
		
		By _selector = By.cssSelector("[class='items']");
		By _labelSelector = By.cssSelector(".items li");
		WebElement labelElem = _driver.findElement(_selector);
		
		List<WebElement> labelList = labelElem.findElements(_labelSelector);
//		labelList.remove(labelList.size()-1);
		
        return labelList;
	}
	
	public static List<WebElement> getLabelListDelete() {
		
		By _selector = By.cssSelector("[class='items']");
		By _deleteSelector = By.cssSelector(".items li em[class='item-delete']");

		WebElement deleteElem = _driver.findElement(_selector);
		
		List<WebElement> deleteList = deleteElem.findElements(_deleteSelector);
//		deleteList.remove(deleteList.size()-1);
		
        return deleteList;
	}
	
	public static List<WebElement> getAllLabels() {
        return getLabelListClosed()
                .stream()
                .collect(Collectors.toList());
	}
	
	public static int getNumLabels(List<WebElement> labelList) {
		return labelList.size();
	}
	
	public static void openLabels() {
		
		WebElement labels = _driver.findElement(By.cssSelector("[id=\"wrap-labels\"] > strong[class=\"name\"]"));
		int width = (int) Math.round((labels.getSize().getWidth())/2.00);
		int offset = width + 4;						// should work because offsets are from center of the element

		Actions builder = new Actions(_driver);
		Action action = builder
				.moveToElement(labels)
				.moveByOffset(offset, 0)
				.click()
				.build();
		action.perform();
	}
	
	public static void editLabels() {
		
		System.out.println("---------------------");
		System.out.println("Editing labels.");
		
		WebDriverWait wait = new WebDriverWait(_driver, 5);
		// this is only for loading the labels
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(
				By.cssSelector("[class='labels-wrap value editable-field active']")));
		
		List<WebElement> labelList = getLabelListOpen();
		List<WebElement> deleteList = getLabelListDelete();
		removeLabels(labelList, deleteList);
		
//		labelList = getLabelListOpen();				// updating the lists
//		deleteList = getLabelListDelete();
//		replaceLabels(labelList, deleteList);
		
		saveLabels();
	}
	
	public static void removeLabels(List<WebElement> labelList, List<WebElement> deleteList) {
		System.out.println("--- Removing labels.");

		for (int i = 0; i < labelList.size(); i++) {
			WebElement label = labelList.get(i);
			
			if (	(label.getText().trim().equalsIgnoreCase("tier0")) || 
					(label.getText().toLowerCase().startsWith("11.")) ||
					(label.getText().toLowerCase().startsWith("reg"))

					){
				
				WebElement delete = deleteList.get(i);
				System.out.println("*** Removing: " + label.getText());
				delete.click();
				
			}
			else { System.out.println(label.getText()); }
		}
	}
	
	public static void replaceLabels(List<WebElement> labelList, List<WebElement> deleteList) {
		System.out.println("----- Replacing labels.");

		// If there is more than one 10.-something, don't add more than one "10" label
		Boolean count = false;
		for (int i = 0; i < labelList.size(); i++) {
			WebElement label = labelList.get(i);
			if (label.getText().equalsIgnoreCase("10")) { 
				count = true;
				break; } 
		}
		
		for (int i = 0; i < labelList.size(); i++) {
			WebElement label = labelList.get(i);
			
			if (	(label.getText().toLowerCase().startsWith("10."))	){
				if (count == false) {
					WebElement delete = deleteList.get(i);
					System.out.println("***** Replacing: " + label.getText());
					delete.click();
					
					addLabel("10");
					count = true;
				}
				else {
					WebElement delete = deleteList.get(i);
					System.out.println("*** Removing: " + label.getText());
					delete.click();
				}
			}
			else { System.out.println(label.getText()); }
		}
	}
	
	public static void addLabel(String labelName) {
		
		By labelFieldSelector = By.cssSelector("[role=\"combobox\"]");
		
		// Wait for load
		WebDriverWait wait = new WebDriverWait(_driver, 5);
		WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(labelFieldSelector));
		
		WebElement labelField = _driver.findElement(labelFieldSelector);
		labelField.sendKeys(labelName);
		try {
			Thread.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		labelField.sendKeys(Keys.ENTER);
	}
	
	public static Boolean labelExists(List<WebElement> labelList, String labelName) {
		
		Boolean exists = false;
		for (int i = 0; i < labelList.size(); i++) {
			WebElement label = labelList.get(i);
			if (label.getText().equalsIgnoreCase(labelName)) { 
				exists = true;
				return exists; } 
		}
		return exists;
	}
	
	public static void saveLabels() {
		_driver.findElement(By.cssSelector("button[type=\"submit\"]")).click();
	}
}
