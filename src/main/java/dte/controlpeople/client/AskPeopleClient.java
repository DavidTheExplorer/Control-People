package dte.controlpeople.client;

import dte.controlpeople.exceptions.AskPeopleException;
import dte.controlpeople.question.AskPeopleQuestion;
import dte.controlpeople.selenium.SeleniumClient;

/**
 * Provides objects that represent things in the <a href="http://askpeople.co.il">AskPeople</a> website.
 */
public interface AskPeopleClient
{
	/**
	 * Returns the question identified by the provided {@code id}.
	 * 
	 * @param id The unique identifier of the question.
	 * @return The question.
	 * @throws AskPeopleException if there is no such question.
	 */
	AskPeopleQuestion getQuestionByID(String id);



	/**
	 * Creates a client that utilizes the {@code Selenium} library.
	 * 
	 * @return A selenium client.
	 */
	static AskPeopleClient ofSelenium()
	{
		return new SeleniumClient();
	}
}
