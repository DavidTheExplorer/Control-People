package dte.controlpeople.selenium;

import static dte.controlpeople.advice.AdviceType.RESPONSE;
import static dte.controlpeople.advice.AdviceType.ROOT;
import static org.openqa.selenium.PageLoadStrategy.EAGER;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import dte.controlpeople.advice.AdviceType;
import dte.controlpeople.exceptions.AskPeopleException;
import dte.controlpeople.question.AskPeopleQuestion;

class AskPeopleSelenium 
{
	private static final WebDriver DRIVER = new ChromeDriver(getEagerLoadingOption());
	
	static
	{
		//register a shutdown hook that quits the driver
		Runtime.getRuntime().addShutdownHook(new Thread(AskPeopleSelenium.getDriver()::quit));
	}

	//Using this option drastically improves the scraping performance, from an average of 10 seconds to get a question's advices -> to just 3.
	private static ChromeOptions getEagerLoadingOption() 
	{
		ChromeOptions options = new ChromeOptions();
		options.setPageLoadStrategy(EAGER);
		
		return options;
	}
	
	public static void navigateToQuestion(String id) 
	{
		navigateTo(AskPeopleQuestion.getURL(id));
		
		if(!doesQuestionExist())
			throw new AskPeopleException(String.format("Could not find a question with the id \"%s\"", id));
	}

	public static void navigateTo(String url) 
	{
		DRIVER.get(url);
	}

	public static WebDriver getDriver() 
	{
		return DRIVER;
	}
	
	/**
	 * Checks whether the provided {@code element} is enabled.
	 * 
	 * @param element The element to check.
	 * @return Whether the element is enabled.
	 * @apiNote Found to be working <b>only</b> with <i>like/dislike</i> buttons after clicked; It's unknown whether this method is relevant to other elements.
	 */
	public static boolean isEnabled(WebElement element) 
	{
		return !element.getAttribute("class").contains("disabled");
	}

	public static void click(WebElement element) 
	{
		try 
		{
			element.click();
		}
		catch(ElementClickInterceptedException exception) 
		{
			((JavascriptExecutor) DRIVER).executeScript("arguments[0].click();", element);
		}
	}



	/*
	 * Data extraction from question pages
	 */
	
	/**
	 * Returns whether there is a readable question in the driver's current page.
	 * 
	 * @return false if the page either represents a deleted question or never represented a question in the first place; true otherwise.
	 */
	public static boolean doesQuestionExist() 
	{
		return DRIVER.findElements(By.xpath(".//div[@id='div_deleted_question']")).isEmpty();
	}
	
	public static List<WebElement> getAdviceElements()
	{
		return DRIVER.findElements(By.xpath("//ul[@id='ul_advices']/li"));
	}



	/*
	 * Data extraction from advice elements
	 */
	public static List<WebElement> getResponseElements(WebElement adviceElement)
	{
		return adviceElement.findElements(By.xpath(".//ul[@class='responses']/li"));
	}

	public static WebElement getDislikeButton(WebElement adviceElement) 
	{
		return adviceElement.findElement(By.xpath(".//div[@class='commands']/span[contains(@id, 'against')]"));
	}

	public static String getCommentorName(WebElement adviceElement) 
	{
		WebElement nameContainer = adviceElement.findElement(By.xpath(".//div[@class='details']/div/h3"));
		boolean isGuestComment = nameContainer.findElements(By.tagName("a")).isEmpty();
		WebElement nameElement = isGuestComment ? nameContainer : nameContainer.findElement(By.tagName("a"));

		return nameElement.getAttribute("innerText");
	}

	public static AdviceType getAdviceType(WebElement adviceElement) 
	{
		WebElement parentOfParent = adviceElement.findElement(By.xpath("../.."));

		switch(parentOfParent.getTagName()) 
		{
		case "div": 
			return ROOT;
			
		case "li": 
			return RESPONSE;

		default:
			throw new RuntimeException("Cannot determine the type of the provided advice element!");
		}
	}
}
