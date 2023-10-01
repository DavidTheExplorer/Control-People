package dte.controlpeople.client;

import dte.controlpeople.exceptions.AskPeopleException;
import dte.controlpeople.question.AskPeopleQuestion;
import dte.controlpeople.selenium.SeleniumClient;

/**
 * This interface is responsible of creating objects related to the <a href="http://askpeople.co.il">AskPeople</a> website; 
 * Essentially functions as the main entry of the library.
 */
public interface AskPeopleClient
{
	/**
	 * Returns the {@code AskPeopleQuestion} the website associates with the provided {@code id}.
	 * 
	 * @param id The unique identifier of the question to return.
	 * @return The question found.
	 * @throws AskPeopleException if there is no such question.
	 */
	AskPeopleQuestion getQuestionByID(String id);
	
	
	/**
	 * Creates a client whose functionality depends on the {@code Selenium} library.
	 * 
	 * @return A selenium client.
	 */
	public static AskPeopleClient ofSelenium() 
	{
		return new SeleniumClient();
	}
}
