package dte.controlpeople.question;

import java.util.List;

import dte.controlpeople.advice.AskPeopleAdvice;

/**
 * Represents a question within the <a href="http://askpeople.co.il">AskPeople</a> website.
 */
public interface AskPeopleQuestion
{
	/**
	 * Returns the unique identifier of this question.
	 * 
	 * @return The id of this question.
	 */
	String getID();
	
	/**
	 * Returns the advices that were written for this question.
	 * Use {@link #selectAdvices()} in order to include their responses, or apply filtering instructions.
	 * 
	 * @return This question's advice list.
	 * @see #selectAdvices()
	 */
	List<AskPeopleAdvice> getAdvices();
	
	/**
	 * Selects the relevant advices of this question; This can include their responses or narrow them down for a specific use-case.
	 * 
	 * @return A customizable selector for the advices of this question.
	 */
	default AdviceSelector selectAdvices() 
	{
		return new AdviceSelector(this);
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
