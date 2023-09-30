package dte.controlpeople.selenium;

import static dte.controlpeople.advice.AdviceType.ROOT;
import static java.util.stream.Collectors.toList;

import java.util.List;

import org.openqa.selenium.WebElement;

import dte.controlpeople.advice.AbstractAdvice;
import dte.controlpeople.advice.AdviceType;
import dte.controlpeople.advice.AskPeopleAdvice;

public class SeleniumAdvice extends AbstractAdvice
{
	private final WebElement element;

	private SeleniumAdvice(WebElement element, String commentorName, AdviceType type)
	{
		super(commentorName, type);
		
		this.element = element;
	}

	public static SeleniumAdvice fromWebElement(WebElement adviceElement) 
	{
		String commentorName = AskPeopleSelenium.getCommentorName(adviceElement);
		AdviceType type = AskPeopleSelenium.getAdviceType(adviceElement);

		return new SeleniumAdvice(adviceElement, commentorName, type);
	}

	public WebElement getElement() 
	{
		return this.element;
	}

	@Override
	public boolean canBeDisliked() 
	{
		return AskPeopleSelenium.isEnabled(AskPeopleSelenium.getDislikeButton(this.element));
	}

	@Override
	public void dislike()
	{
		AskPeopleSelenium.click(AskPeopleSelenium.getDislikeButton(this.element));
	}
	
	@Override
	public List<AskPeopleAdvice> getResponses() 
	{
		verifyType(ROOT, "Cannot get the responses of an advice that is a response!");
		
		return AskPeopleSelenium.getResponseElements(this.element).stream()
				.map(SeleniumAdvice::fromWebElement)
				.collect(toList());
	}

	@Override
	public String toString() 
	{
		return String.format("SeleniumAdvice [commentor=%s, type=%s]", getCommentorName(), getType());
	}
}
