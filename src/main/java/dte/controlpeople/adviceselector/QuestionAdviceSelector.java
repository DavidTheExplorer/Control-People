package dte.controlpeople.adviceselector;

import dte.controlpeople.advice.AskPeopleAdvice;
import dte.controlpeople.question.AskPeopleQuestion;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class QuestionAdviceSelector extends AdviceSelector<QuestionAdviceSelector>
{
    private final AskPeopleQuestion question;
    private boolean includeResponses = false;

    public QuestionAdviceSelector(AskPeopleQuestion question)
    {
        this.question = question;
    }

    /**
     * Includes all responses.
     * Pay attention that any filtering instructions are applied <b>only after</b> the responses are added.
     *
     * @return The same instance for chaining purposes.
     */
    public QuestionAdviceSelector withResponses()
    {
        this.includeResponses = true;
        return self();
    }

    @Override
    protected List<AskPeopleAdvice> obtainAdvices()
    {
        List<AskPeopleAdvice> advices = new ArrayList<>(this.question.getAdvices());

        if(this.includeResponses)
            advices.addAll(getResponses(advices));

        return advices;
    }

    private List<AskPeopleAdvice> getResponses(List<AskPeopleAdvice> advices)
    {
        return advices.stream()
                .flatMap(advice -> advice.getResponses().stream())
                .collect(toList());
    }
}
