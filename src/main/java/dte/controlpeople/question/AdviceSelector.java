package dte.controlpeople.question;

import static com.google.common.base.Predicates.alwaysTrue;
import static com.google.common.base.Predicates.not;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import dte.controlpeople.advice.AskPeopleAdvice;

public class AdviceSelector
{
	private final AskPeopleQuestion question;
	private Predicate<AskPeopleAdvice> filter = alwaysTrue();
	private boolean includeResponses = false;
	
	private static final Pattern GUEST_NAME_PATTERN = Pattern.compile(".+אורחת?");
	
	//there's no point to make this public
	AdviceSelector(AskPeopleQuestion question) 
	{
		this.question = question;
	}
	
	/**
	 * Filters out all advices that were not written by the provided {@code commentor}.
	 * <p>
	 * Guest advices are also excluded to avoid selecting fake ones.
	 * 
	 * @param commentor The commentor whose advices are to be returned.
	 * @return The same instance for chaining purposes.
	 */
	public AdviceSelector byUser(String commentor) 
	{
		return excludingGuests()
				.filter(advice -> advice.getCommentorName().equals(commentor));
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
	
	public AdviceSelector excludingGuests() 
	{
		return filter(not(advice -> GUEST_NAME_PATTERN.matcher(advice.getCommentorName()).matches()));
	}
	
	/**
	 * Sets a filter that determines what advices will be returned.
	 * 
	 * @param filter The filter to use.
	 * @return The same instance for chaining purposes.
	 */
	public AdviceSelector filter(Predicate<AskPeopleAdvice> filter) 
	{
		this.filter = this.filter.and(filter);
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
	
	/**
	 * Returns a {@link Stream} of the found advices, this method is a shortcut that's essentially equivalent to writing:
	 * <pre>
	 * {@code 
	 * Stream<AskPeopleAdvice> stream = selector.get().stream();
	 * }</pre>
	 * 
	 * @return A stream of selected advices.
	 */
	public Stream<AskPeopleAdvice> stream()
	{
		return get().stream();
	}

	private List<AskPeopleAdvice> getResponses(List<AskPeopleAdvice> advices)
	{
		return advices.stream()
				.flatMap(advice -> advice.getResponses().stream())
				.collect(toList());
	}
}