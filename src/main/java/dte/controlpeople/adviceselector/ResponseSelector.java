package dte.controlpeople.adviceselector;

import dte.controlpeople.advice.AskPeopleAdvice;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link AdviceSelector} that selects the responses to an {@link AskPeopleAdvice}.
 */
public class ResponseSelector extends AdviceSelector<ResponseSelector>
{
    private final AskPeopleAdvice advice;

    public ResponseSelector(AskPeopleAdvice advice)
    {
        this.advice = advice;
    }

    @Override
    protected List<AskPeopleAdvice> obtainAdvices()
    {
        return new ArrayList<>(this.advice.getResponses());
    }
}
