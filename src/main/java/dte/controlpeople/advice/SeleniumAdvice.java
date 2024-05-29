package dte.controlpeople.advice;

import static dte.controlpeople.advice.AskPeopleAdvice.Type.RESPONSE;
import static dte.controlpeople.advice.AskPeopleAdvice.Type.ROOT;
import static dte.controlpeople.author.AskPeopleAuthor.Type.*;
import static java.util.stream.Collectors.toList;

import java.util.List;

import dte.controlpeople.author.AskPeopleAuthor;
import dte.controlpeople.exceptions.AskPeopleException;
import dte.controlpeople.client.SeleniumClient;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SeleniumAdvice extends AbstractAdvice
{
	private final WebElement element;

	private SeleniumAdvice(WebElement element, Type type, AskPeopleAuthor author)
	{
		super(type, author);
		
		this.element = element;
	}

	public static SeleniumAdvice fromWebElement(WebElement adviceElement) 
	{
		WebElement nameContainer = adviceElement.findElement(By.xpath(".//div[@class='details']/div/h3"));

		AskPeopleAuthor.Type authorType = getAuthorType(nameContainer);
		String authorName = getAuthorName(nameContainer, authorType);
		Type type = getAdviceType(adviceElement);

		return new SeleniumAdvice(adviceElement, type, new AskPeopleAuthor(authorName, authorType));
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
		if(getType() != ROOT)
			throw new AskPeopleException("Cannot get the responses of an advice that is a response!");
		
		List<WebElement> responseElements = this.element.findElements(By.xpath(".//ul[@class='responses']/li"));
		
		return responseElements.stream()
				.map(SeleniumAdvice::fromWebElement)
				.collect(toList());
	}

	@Override
	public String toString() 
	{
		return String.format("SeleniumAdvice [author=%s, type=%s]", getAuthor().getName(), getType());
	}
	
	
	
	/*
	 * Selenium 
	 */
	private static AskPeopleAuthor.Type getAuthorType(WebElement nameContainer)
	{
		//clicking a registered user's name leads to his profile
		if(!nameContainer.findElements(By.tagName("a")).isEmpty())
			return REGISTERED_USER;

		if(!nameContainer.findElements(By.tagName("span")).isEmpty())
			return ORIGINAL_POSTER;

		return GUEST;
	}

	private static String getAuthorName(WebElement nameContainer, AskPeopleAuthor.Type authorType)
	{
		String fullName = nameContainer.getAttribute("innerText");

		switch(authorType)
		{
			case GUEST:
			case REGISTERED_USER:
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

	private static Type getAdviceType(WebElement adviceElement)
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
