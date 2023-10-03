package dte.controlpeople.selenium;

import static dte.controlpeople.advice.AdviceType.RESPONSE;
import static dte.controlpeople.advice.AdviceType.ROOT;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dte.controlpeople.advice.AbstractAdvice;
import dte.controlpeople.advice.AdviceType;
import dte.controlpeople.advice.AskPeopleAdvice;

public class SeleniumAdvice extends AbstractAdvice
{
	private final WebElement element;

	private SeleniumAdvice(WebElement element, String commentorName, AdviceType type, boolean authorGuest)
	{
		super(commentorName, type, authorGuest);
		
		this.element = element;
	}

	public static SeleniumAdvice fromWebElement(WebElement adviceElement) 
	{
		WebElement nameContainer = adviceElement.findElement(By.xpath(".//div[@class='details']/div/h3"));

		boolean authorGuest = nameContainer.findElements(By.tagName("a")).isEmpty(); //clicking a registered user's name leads to his profile
		String commentorName = getCommentorName(nameContainer, authorGuest);
		AdviceType type = getAdviceType(adviceElement);

		return new SeleniumAdvice(adviceElement, commentorName, type, authorGuest);
	}

	public WebElement getElement() 
	{
		return this.element;
	}

	@Override
	public boolean canBeDisliked() 
	{
		return isEnabled(getDislikeButton(this.element));
	}

	@Override
	public void dislike()
	{
		SeleniumClient.click(getDislikeButton(this.element));
	}
	
	@Override
	public List<AskPeopleAdvice> getResponses() 
	{
		verifyType(ROOT, "Cannot get the responses of an advice that is a response!");
		
		List<WebElement> responseElements = this.element.findElements(By.xpath(".//ul[@class='responses']/li"));
		
		return responseElements.stream()
				.map(SeleniumAdvice::fromWebElement)
				.collect(toList());
	}

	@Override
	public String toString() 
	{
		return String.format("SeleniumAdvice [commentor=%s, type=%s]", getCommentorName(), getType());
	}
	
	
	
	/*
	 * Selenium 
	 */
	private static String getCommentorName(WebElement adviceElement, boolean authorGuest)
	{
		WebElement nameElement = authorGuest ? adviceElement : adviceElement.findElement(By.tagName("a"));
		String nameAndAge = nameElement.getAttribute("innerText");

		//return just the name part
		return nameAndAge.substring(0, nameAndAge.indexOf(','));
	}

	private static WebElement getDislikeButton(WebElement adviceElement) 
	{
		return adviceElement.findElement(By.xpath(".//div[@class='commands']/span[contains(@id, 'against')]"));
	}

	private static AdviceType getAdviceType(WebElement adviceElement) 
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
	
	//this may apply to other elements, but idk so it's private
	private static boolean isEnabled(WebElement ratingElement) 
	{
		return !ratingElement.getAttribute("class").contains("disabled");
	}
}
