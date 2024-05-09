package dte.controlpeople.adviceselector;

import static com.google.common.base.Predicates.alwaysTrue;
import static dte.controlpeople.advice.AuthorType.GUEST;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

import dte.controlpeople.advice.AskPeopleAdvice;
import dte.controlpeople.advice.AuthorType;

public abstract class AdviceSelector<SELF extends AdviceSelector<SELF>>
{
    private Predicate<AskPeopleAdvice> filter = alwaysTrue();

    /**
     * Sets a filter that determines what advices will be returned.
     *
     * @param filter The filter to use.
     * @return The same instance for chaining purposes.
     */
    public SELF filter(Predicate<AskPeopleAdvice> filter)
    {
        this.filter = this.filter.and(filter);
        return self();
    }

    /**
     * Excludes advices that were written by authors of the provided type.
     *
     * @return The same instance for chaining purposes.
     */
    public SELF excludingBy(AuthorType authorType)
    {
        return filter(advice -> advice.getAuthorType() != authorType);
    }

    /**
     * Filters out all advices that were not written by the provided user.
     * <p>
     * Guest advices are also excluded to avoid selecting fake ones.
     *
     * @param username The name of the user whose advices are to be returned.
     * @return The same instance for chaining purposes.
     */
    public SELF byUser(String username)
    {
        return excludingBy(GUEST)
                .filter(advice -> advice.getAuthorName().equals(username));
    }

    /**
     * Returns the found advices after filtering them if requested.
     *
     * @return The list of advices.
     */
    public List<AskPeopleAdvice> get()
    {
        return obtainAdvices().stream()
                .filter(this.filter)
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    protected SELF self()
    {
        return (SELF) this;
    }

    protected abstract List<AskPeopleAdvice> obtainAdvices();
}