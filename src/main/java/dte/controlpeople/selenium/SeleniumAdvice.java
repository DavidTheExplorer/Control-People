package dte.controlpeople.selenium;

import static dte.controlpeople.advice.AdviceType.RESPONSE;
import static dte.controlpeople.advice.AdviceType.ROOT;
import static dte.controlpeople.advice.AuthorType.*;
import static java.util.stream.Collectors.toList;

import java.util.List;

import dte.controlpeople.advice.AuthorType;
import dte.controlpeople.client.AskPeopleClient;
import dte.controlpeople.question.AskPeopleQuestion;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import dte.controlpeople.advice.AbstractAdvice;
import dte.controlpeople.advice.AdviceType;
import dte.controlpeople.advice.AskPeopleAdvice;

public class SeleniumAdvice extends AbstractAdvice
{
	private final WebElement element;

	private SeleniumAdvice(WebElement element, String commentorName, AdviceType type, AuthorType authorType)
	{
		super(commentorName, type, authorType);
		
		this.element = element;
	}

	public static SeleniumAdvice fromWebElement(WebElement adviceElement) 
	{
		WebElement nameContainer = adviceElement.findElement(By.xpath(".//div[@class='details']/div/h3"));

		AuthorType authorType = getAuthorType(nameContainer);
		String commentorName = getCommentorName(nameContainer, authorType);
		AdviceType type = getAdviceType(adviceElement);

		return new SeleniumAdvice(adviceElement, commentorName, type, authorType);
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
	private static AuthorType getAuthorType(WebElement nameContainer)
	{
		//clicking a registered user's name leads to his profile
		if(!nameContainer.findElements(By.tagName("a")).isEmpty())
			return USER;

		if(!nameContainer.findElements(By.tagName("span")).isEmpty())
			return ORIGINAL_POSTER;

		return GUEST;
	}

	private static String getCommentorName(WebElement nameContainer, AuthorType authorType)
	{
		String fullName = nameContainer.getAttribute("innerText");

		switch(authorType)
		{
			case GUEST:
			case USER:
				//reputable users don't have an age - just their name(no comma for their age)
				int endIndex = fullName.contains(",") ? fullName.indexOf(',') : fullName.length();

				return fullName.substring(0, endIndex);

			case ORIGINAL_POSTER:
				return fullName.substring(0, fullName.indexOf('(') -1);

			default:
				throw new IllegalStateException("Could not determine the type of the author!");
		}
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
