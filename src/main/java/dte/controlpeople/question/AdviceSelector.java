package dte.controlpeople.question;

import static com.google.common.base.Predicates.alwaysTrue;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

import dte.controlpeople.advice.AskPeopleAdvice;

public class AdviceSelector
{
	private final AskPeopleQuestion question;
	private Predicate<AskPeopleAdvice> filter = alwaysTrue();
	private boolean includeResponses = false;
	
	//there's no point to make this public
	AdviceSelector(AskPeopleQuestion question) 
	{
		this.question = question;
	}
	
	/**
	 * Filters out all advices that were not written by the provided {@code commentor}.
	 * 
	 * @param commentor The commentor whose advices are to be returned.
	 * @return The same instance for chaining purposes.
	 */
	public AdviceSelector byUser(String commentor) 
	{
		return filter(advice -> advice.getCommentorName().contains(commentor));
	}
	
	/**
	 * Includes all responses.
	 * Pay attention that any filtering instructions are applied <b>only after</b> the responses are added.
	 * 
	 * @return The same instance for chaining purposes.
	 */
	public AdviceSelector withResponses() 
	{
		this.includeResponses = true;
		return this;
	}
	
	/**
	 * Sets a filter that determines what advices will be returned.
	 * 
	 * @param filter The filter to use.
	 * @return The same instance for chaining purposes.
	 */
	public AdviceSelector filter(Predicate<AskPeopleAdvice> filter) 
	{
		this.filter = filter;
		return this;
	}
	
	/**
	 * Returns the found advices after filtering them if requested.
	 * 
	 * @return The list of advices.
	 */
	public List<AskPeopleAdvice> get()
	{
		List<AskPeopleAdvice> advices = this.question.getAdvices();
		
		//include the responses if requested
		if(this.includeResponses) 
			advices.addAll(getResponses(advices));
		
		return advices.stream()
				.filter(this.filter)
				.collect(toList());
	}

	private List<AskPeopleAdvice> getResponses(List<AskPeopleAdvice> advices)
	{
		return advices.stream()
				.flatMap(advice -> advice.getResponses().stream())
				.collect(toList());
	}
}