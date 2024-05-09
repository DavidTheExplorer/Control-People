package dte.controlpeople.advice;

import dte.controlpeople.exceptions.AskPeopleException;

public abstract class AbstractAdvice implements AskPeopleAdvice
{
	private final String authorName;
	private final AdviceType type;
	private final AuthorType authorType;
	
	protected AbstractAdvice(String authorName, AdviceType type, AuthorType authorType)
	{
		this.authorName = authorName;
		this.type = type;
		this.authorType = authorType;
	}

	@Override
	public String getAuthorName()
	{
		return this.authorName;
	}

	@Override
	public AdviceType getType() 
	{
		return this.type;
	}

	@Override
	public AuthorType getAuthorType()
	{
		return this.authorType;
	}

	//helper method that currently only serves the abstract getResponses()
	protected void verifyType(AdviceType expectedType, String errorMessage) 
	{
		if(getType() != expectedType)
			throw new AskPeopleException(errorMessage);
	}
}
