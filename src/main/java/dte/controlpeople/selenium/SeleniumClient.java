package dte.controlpeople.selenium;

import static java.util.stream.Collectors.toList;

import java.util.List;

import dte.controlpeople.advice.AskPeopleAdvice;
import dte.controlpeople.client.AskPeopleClient;
import dte.controlpeople.question.AskPeopleQuestion;
import dte.controlpeople.question.SimpleQuestion;

public class SeleniumClient implements AskPeopleClient
{
	@Override
	public AskPeopleQuestion getQuestionByID(String id) 
	{
		//go to the question's page
		AskPeopleSelenium.navigateToQuestion(id);
		
		//scrape the question's data
		List<AskPeopleAdvice> advices = scrapeAdvices();

		return new SimpleQuestion(id, advices);
	}

	private List<AskPeopleAdvice> scrapeAdvices()
	{
		return AskPeopleSelenium.getAdviceElements().stream()
				.map(SeleniumAdvice::fromWebElement)
				.collect(toList());
	}
}