package dte.controlpeople.adviceselector;

import static com.google.common.base.Predicates.alwaysTrue;
import static dte.controlpeople.advice.AskPeopleAdvice.AuthorType.GUEST;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;

import dte.controlpeople.advice.AskPeopleAdvice;
import dte.controlpeople.advice.AskPeopleAdvice.AuthorType;

/**
 * This class describes a process of selecting specific {@link AskPeopleAdvice}s from a source.
 * For example: Selecting the responses to an advice.
 * <p>
 * The design is of a recursive builder - Concrete selectors can add their own filters.
 *
 * @param <SELF> The class of the implementation class.
 */
public abstract class AdviceSelector<SELF extends AdviceSelector<SELF>>
{
    private Predicate<AskPeopleAdvice> filter = alwaysTrue();

    /**
     * Adds a filter that determines which advices will be returned.
     *
     * @param filter The filter to apply.
     * @apiNote This does not override the current filter, but adds the provided {@code filter} on top of it.
     * @return The same instance for chaining purposes.
     */
    public SELF filter(Predicate<AskPeopleAdvice> filter)
    {
        this.filter = this.filter.and(filter);
        return self();
    }

    /**
     * Excludes advices that were written by authors from the provided type.
     *
     * @return The same instance for chaining purposes.
     */
    public SELF excludingBy(AuthorType authorType)
    {
        return filter(advice -> advice.getAuthorType() != authorType);
    }

    /**
     * Selects only advices that were written by the provided user.
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
     * Obtains and filters a list of advices from the source, and returns them.
     *
     * @return The selected advices.
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

    /**
     * Returns the list of advices which {@link #get()} will use as base.
     *
     * @return The original list of advices.
     */
    protected abstract List<AskPeopleAdvice> obtainAdvices();
}