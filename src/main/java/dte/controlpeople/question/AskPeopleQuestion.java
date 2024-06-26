package dte.controlpeople.question;

import java.util.ArrayList;
import java.util.List;

import dte.controlpeople.advice.AskPeopleAdvice;
import dte.controlpeople.adviceselector.QuestionAdviceSelector;

/**
 * Represents a question within the <a href="http://askpeople.co.il">AskPeople</a> website.
 */
public class AskPeopleQuestion
{
	private final String id, content;
	private final List<AskPeopleAdvice> advices;
	
	public AskPeopleQuestion(String id, String content, List<AskPeopleAdvice> advices)
	{
		this.id = id;
		this.content = content;
		this.advices = advices;
	}
	
	/**
	 * Returns the unique identifier of this question.
	 *
	 * @return The id of this question.
	 */
	public String getID() 
	{
		return this.id;
	}

	/**
	 * Returns the content of this question.
	 *
	 * @return The content of this question.
	 */
	public String getContent()
	{
		return this.content;
	}

	/**
	 * Returns the advices people wrote for this question.
	 * Use {@link #selectAdvices()} to include the responses or easily filter the list.
	 * 
	 * @return This question's advice list.
	 */
	public List<AskPeopleAdvice> getAdvices()
	{
		return new ArrayList<>(this.advices);
	}
	
	/**
	 * Works like {@link #getAdvices()}, but this method is used to apply common filters or include the (filtered) advices' responses.
	 *
	 * @return A customizable selector for the advices of this question.
	 */
	public QuestionAdviceSelector selectAdvices()
	{
		return new QuestionAdviceSelector(this);
	}


	
	/**
	 * Given a question's ID, this method builds the corresponding URL that can be visited by the browser.
	 * 
	 * @param questionID A question's ID.
	 * @return The url of the question.
	 */
	public static String getURL(String questionID) 
	{
		return String.format("https://www.askp.co.il/question/%s", questionID);
	}
}
