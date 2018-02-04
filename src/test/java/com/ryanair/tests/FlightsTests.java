package com.ryanair.tests;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.ryanair.pages.HomePage;
import com.ryanair.pages.SearchResultPage;

public class FlightsTests {

	WebDriver driver;

	@BeforeClass
	public void beforeClass() {
		System.setProperty("webdriver.chrome.driver", "lib//driver//chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
	}

	@Test
	public void testFlights() throws InterruptedException {

		HomePage homePage = new HomePage(driver);
		
		homePage.open();
		Assert.assertEquals(driver.getTitle(),
				"Official Ryanair website | Book direct for the lowest fares | Ryanair.com");

		homePage.selectFromCountry(" Belgium")
				.selectFromAirport("Brussels Charleroi");
		Assert.assertEquals(homePage.getSelectedFromValue(), "Brussels Charleroi");

		homePage.selectToCountry(" United Kingdom")
				.selectToAirport("Manchester");
		Assert.assertEquals(homePage.getSelectedToValue(), "Manchester");

		// Mechanism to choose random dates
		LocalDate startingDate = LocalDate.of(java.time.LocalDateTime.now().getYear(),
				java.time.LocalDateTime.now().getMonthValue(), java.time.LocalDateTime.now().getDayOfMonth());
		long start = startingDate.toEpochDay();
		LocalDate endingDate = LocalDate.of(java.time.LocalDateTime.now().getYear(),
				java.time.LocalDateTime.now().getMonthValue() + 7, java.time.LocalDateTime.now().getDayOfMonth()); 
		long end = endingDate.toEpochDay();
		long randomEpochDay = ThreadLocalRandom.current().longs(start, end).findAny().getAsLong();
		long randomEpochDay2 = ThreadLocalRandom.current().longs(randomEpochDay, end).findAny().getAsLong();
		String startDate = LocalDate.ofEpochDay(randomEpochDay).getDayOfMonth() + "";
		String toDate = LocalDate.ofEpochDay(randomEpochDay2).getDayOfMonth() + "";
		String startMonth = LocalDate.ofEpochDay(randomEpochDay).getMonth().name();
		String toMonth = LocalDate.ofEpochDay(randomEpochDay2).getMonth().name();

		homePage.selectFromCalendar(startDate, startMonth)
				.selectFromCalendar(toDate, toMonth)
				.addTeen();
		SearchResultPage searchResultPage = homePage.search();
		Assert.assertTrue(searchResultPage.isFlightDisplayed());
		
		String startResultDate = searchResultPage.startResultDateText().toLowerCase();
		Assert.assertTrue(
				startResultDate.toLowerCase().contains((startDate + " " + startMonth.substring(0, 3)).toLowerCase()));

		String endResultDate = searchResultPage.endResultDateText().toLowerCase();
		Assert.assertTrue(endResultDate.toLowerCase().contains((toDate + " " + toMonth.substring(0, 3)).toLowerCase()));
		searchResultPage.printFlightDetails();

		System.out.println("Test Completed...");
	}

	@AfterClass
	public void afterTest() {
		driver.quit();
	}

}
