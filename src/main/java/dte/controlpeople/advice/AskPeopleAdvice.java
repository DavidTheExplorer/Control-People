package dte.controlpeople.advice;

import static java.util.stream.Collectors.toList;

import java.util.List;

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
	 * Returns the name of the person(or guest) who sent this advice. 
	 * 
	 * @return The name of this advice's commentor.
	 * @apiNote This will one day return a proper {@code AskPeopleUser}.
	 */
	String getCommentorName();
	
	/**
	 * Returns whether this advice is normal, or just a response to another advice.
	 *
	 * @return The type of this advice.
	 */
	AdviceType getType();
	
	/**
	 * Returns the response list of this advice.
	 * 
	 * @return The response list of this advice.
	 */
	List<AskPeopleAdvice> getResponses();
	
	/**
	 * Returns the type of this advice's author.
	 * 
	 * @return the type of this advice's author.
	 */
	AuthorType getAuthorType();
	
	/**
	 * Returns whether the dislike button of this advice is enabled(could be various reasons why not).
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
	 * Returns the responses to this advice sent by the commentor identified by their {@code name}.
	 * 
	 * @param commentorName The commentor whose responses are desired.
	 * @return The filtered response list.
	 */
	default List<AskPeopleAdvice> getResponsesBy(String commentorName) 
	{
		return getResponses().stream()
				.filter(response -> response.getCommentorName().equals(commentorName))
				.collect(toList());
	}
}