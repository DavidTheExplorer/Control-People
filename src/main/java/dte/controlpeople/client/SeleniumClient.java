package dte.controlpeople.client;

import static java.util.stream.Collectors.toList;
import static org.openqa.selenium.PageLoadStrategy.EAGER;

import java.util.List;

import dte.controlpeople.advice.SeleniumAdvice;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import dte.controlpeople.advice.AskPeopleAdvice;
import dte.controlpeople.exceptions.AskPeopleException;
import dte.controlpeople.question.AskPeopleQuestion;

public class SeleniumClient implements AskPeopleClient
{
	private static final WebDriver DRIVER = new ChromeDriver(getEagerLoadingOption());

	static
	{
		//quit the driver on shutdown
		Runtime.getRuntime().addShutdownHook(new Thread(DRIVER::quit));
	}

	@Override
	public AskPeopleQuestion getQuestionByID(String id) 
	{
		//go to the question's page
		DRIVER.get(AskPeopleQuestion.getURL(id));

		if(!doesQuestionExist())
			throw new AskPeopleException(String.format("Could not find a question with the id \"%s\"", id));

		//scrape the question's data
		String content = scrapeCurrentQuestionContent();
		List<AskPeopleAdvice> advices = scrapeAdvices();

		return new AskPeopleQuestion(id, content, advices);
	}

	private static ChromeOptions getEagerLoadingOption() 
	{
		ChromeOptions options = new ChromeOptions();

		//Using this option drastically improves the scraping performance, from an average of 10 seconds to get a question's advices -> to just 3.
		options.setPageLoadStrategy(EAGER);

		return options;
	}
	
	private static List<AskPeopleAdvice> scrapeAdvices()
	{
		List<WebElement> adviceElements = DRIVER.findElements(By.xpath("//ul[@id='ul_advices']/li"));

		return adviceElements.stream()
				.map(SeleniumAdvice::fromWebElement)
				.collect(toList());
	}

	private static boolean doesQuestionExist() 
	{
		//for some reason, this div appears both when the question is deleted, but also when the ID doesn't exist
		return DRIVER.findElements(By.xpath(".//div[@id='div_deleted_question']")).isEmpty();
	}

	private static String scrapeCurrentQuestionContent()
	{
		WebElement content = DRIVER.findElement(By.xpath(".//div[@class='question_content']/p"));

		//return the text if it doesn't have a "continue" button
		if(content.isDisplayed())
			return content.getText();

		//click the "continue" button
		WebElement continueButton = DRIVER.findElement(By.xpath(".//div[@id='question_content_short']/p/span[@class='show_full_question']"));
		click(continueButton);

		//now the content contains the full text - so we can return it
		return content.getText();
	}
	
	public static void click(WebElement element) 
	{
		try 
		{
			element.click();
		}
		catch(ElementClickInterceptedException exception) 
		{
			((JavascriptExecutor) SeleniumClient.DRIVER).executeScript("arguments[0].click();", element);
		}
	}
}