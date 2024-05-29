package dte.controlpeople.advice;

import java.util.List;

import dte.controlpeople.adviceselector.ResponseSelector;
import dte.controlpeople.author.AskPeopleAuthor;
import dte.controlpeople.question.AskPeopleQuestion;

/**
 * Represents an advice for a question within the <a href="http://askpeople.co.il">AskPeople</a> website.
 * 
 * @see AskPeopleQuestion
 * @implSpec See {@link AbstractAdvice} for a convenient skeletal implementation.
 */
public interface AskPeopleAdvice
{
	/**
	 * Returns whether this advice is normal, or just a response to another advice.
	 *
	 * @return The type of this advice.
	 */
	Type getType();

	/**
	 * Returns the author of this advice.
	 *
	 * @return The author of this advice.
	 */
	AskPeopleAuthor getAuthor();
	
	/**
	 * Returns the responses people wrote for this advice;
	 * Use {@link #selectResponses()} to easily filter the list.
	 * 
	 * @return This advice's response list.
	 */
	List<AskPeopleAdvice> getResponses();

	/**
	 * Works like {@link #getResponses()}, but this method is used to apply common filters.
	 *
	 * @return A customizable selector for the responses of this advice.
	 */
	default ResponseSelector selectResponses()
	{
		return new ResponseSelector(this);
	}
	
	/**
	 * Returns whether this advice can be disliked. Usually returns false when the advice was already disliked.
	 * 
	 * @return Whether this advice can be disliked or not.
	 */
	boolean canBeDisliked();
	
	/**
	 * Dislikes this advice. If this advice cannot be disliked, nothing happens.
	 * 
	 * @see #canBeDisliked()
	 */
	void dislike();



	/**
	 * Represents the type of an {@link AskPeopleAdvice}.
	 */
	enum Type
	{
		/**
		 * Represents a normal advice to a question.
		 */
		ROOT,

		/**
		 * Represents an advice that was written as a response to another advice.
		 */
		RESPONSE;
	}
}