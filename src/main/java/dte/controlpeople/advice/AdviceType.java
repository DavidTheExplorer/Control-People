package dte.controlpeople.advice;

/**
 * Represents the the type of an {@link AskPeopleAdvice}.
 */
public enum AdviceType
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
