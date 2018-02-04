package com.ryanair.pages;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Function;

public class HomePage {

	private WebDriver driver;
	private String homeURL = "https://www.ryanair.com/gb/en/";

	@FindBy(how = How.XPATH, using = "(//*[@name='departureAirportName'])[1]")
	private WebElement fromElement;

	@FindBy(how = How.XPATH, using = "//*[text()=' Belgium']")
	private WebElement belgiumLink;

	@FindBy(how = How.XPATH, using = "//*[text()='Brussels Charleroi']")
	private WebElement brusselsLink;

	@FindBy(how = How.XPATH, using = "(//*[@name='destinationAirportName'])[1]")
	private WebElement toElement;

	@FindBy(how = How.XPATH, using = "//*[text()=' United Kingdom']")
	private WebElement unitedKingdomLink;

	@FindBy(how = How.XPATH, using = "//*[text()='Manchester']")
	private WebElement manchesterLink;

	@FindBy(how = How.XPATH, using = "//div[@class='dropdown-handle']")
	private WebElement addPassengersDropdown;

	@FindBy(how = How.XPATH, using = "(//*[@icon-id='glyphs.plus-circle'])[2]/..")
	private WebElement teenPlusButton;

	@FindBy(how = How.XPATH, using = "//button/span[text()=\"Let's go! \"]")
	private WebElement letsGoButton;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		PageFactory.initElements(driver, this);
	}

	public HomePage open() {
		System.out.println("Lauching Ryanair home page...");
		driver.get(homeURL);
		return this;
	}

	public HomePage selectFromCountry(final String Country) {
		System.out.println("Selecting From Country..." + Country);
		fromElement.click();
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)");
		WebElement fromCountry  = (new WebDriverWait(driver, 30)).until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {
				return d.findElement(By.xpath("//*[text()='" + Country + "']"));
			}
		});
		fromCountry.click();

		return this;

	}

	public HomePage selectFromAirport(final String Airport) {
		System.out.println("Selecting From Airport..." + Airport);
		WebElement fromAirport = (new WebDriverWait(driver, 30)).until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {
				return d.findElement(By.xpath("//*[text()='" + Airport + "']"));
			}
		});
		fromAirport.click();
		return this;
	}

	public String getSelectedFromValue() {
		return fromElement.getAttribute("value");
	}

	public HomePage selectToCountry(final String Country) {
		System.out.println("Selecting To Country:" + Country);
		((JavascriptExecutor) driver).executeScript("window.scrollBy(0,250)");
		WebElement toCountry = (new WebDriverWait(driver, 30)).until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {
				return d.findElement(By.xpath("//*[text()='" + Country + "']"));
			}
		});
		toCountry.click();
		return this;

	}

	public HomePage selectToAirport(final String Airport) {
		System.out.println("Selecting To Airport:" + Airport);
		WebElement toAirport  = (new WebDriverWait(driver, 30)).until(new ExpectedCondition<WebElement>() {
			public WebElement apply(WebDriver d) {
				return d.findElement(By.xpath("//*[text()='" + Airport + "']"));
			}
		});
		toAirport.click();
		return this;

	}

	public String getSelectedToValue() {
		new WebDriverWait(driver, 30).until(ExpectedConditions.textToBePresentInElementValue(toElement, "Manchester"));
		return toElement.getAttribute("value");
	}

	public HomePage selectFromCalendar(String StartDate, String startMonth) {

		System.out.println("Selecting... " + StartDate + "-" + startMonth);
		int i = 1;

		while (i < 11) {
			WebElement Month = driver.findElement(By.xpath("(//h1[@class='month-name'])[" + i + "]"));
			if (Month.getText().toLowerCase().contains(startMonth.toLowerCase())) {
				WebElement Calendar = driver.findElement(By.xpath("(//core-datepicker//ul[@class='days'])[" + i + "]"));
				List<WebElement> Days = Calendar
						.findElements(By.xpath("(//core-datepicker//ul[@class='days'])[" + i + "]/li"));
				for (WebElement day : Days) {
					String str = day.getAttribute("data-id").substring(0, 2);
					if ((str.substring(0, 1).equals("0") ? str.substring(1) : str).equals(StartDate)) {
						day.click();
						return this;
					}
				}
			} else {
				driver.findElement(By.xpath("//button[@class='arrow right']")).click();
				i++;
			}
		}

		return this;
	}

	public HomePage addTeen() {
		addPassengersDropdown.click();
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS).ignoring(WebDriverException.class);
		teenPlusButton = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(By.xpath("(//*[@icon-id='glyphs.plus-circle'])[2]/.."));
			}
		});
		teenPlusButton.click();
		return this;
	}

	public SearchResultPage search() {
		letsGoButton.click();
		return new SearchResultPage(driver);
	}

}