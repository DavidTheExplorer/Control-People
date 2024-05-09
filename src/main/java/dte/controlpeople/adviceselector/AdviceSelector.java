package dte.controlpeople.adviceselector;

import static com.google.common.base.Predicates.alwaysTrue;
import static dte.controlpeople.advice.AuthorType.GUEST;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
     * Filters out all advices that were not written by the provided {@code commentor}.
     * <p>
     * Guest advices are also excluded to avoid selecting fake ones.
     *
     * @param commentor The commentor whose advices are to be returned.
     * @return The same instance for chaining purposes.
     */
    public SELF byUser(String commentor)
    {
        return excludingBy(GUEST)
                .filter(advice -> advice.getCommentorName().equals(commentor));
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

    @SuppressWarnings("unchecked")
    protected SELF self()
    {
        return (SELF) this;
    }

    protected abstract List<AskPeopleAdvice> obtainAdvices();
}