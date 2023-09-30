package dte.controlpeople.question;

import java.util.ArrayList;
import java.util.List;

import dte.controlpeople.advice.AskPeopleAdvice;

public class SimpleQuestion implements AskPeopleQuestion
{
	private final String id;
	private final List<AskPeopleAdvice> advices;
	
	public SimpleQuestion(String id, List<AskPeopleAdvice> advices) 
	{
		this.id = id;
		this.advices = advices;
	}
	
	@Override
	public String getID()
	{
		return this.id;
	}
	
	@Override
	public List<AskPeopleAdvice> getAdvices() 
	{
		return new ArrayList<>(this.advices);
	}
}