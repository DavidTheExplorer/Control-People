package dte.controlpeople.advice;

import dte.controlpeople.exceptions.AskPeopleException;

public abstract class AbstractAdvice implements AskPeopleAdvice
{
	private final String commentorName;
	private final AdviceType type;
	private final boolean authorGuest;
	
	protected AbstractAdvice(String commentorName, AdviceType type, boolean authorGuest)
	{
		this.commentorName = commentorName;
		this.type = type;
		this.authorGuest = authorGuest;
	}

	@Override
	public String getCommentorName() 
	{
		return this.commentorName;
	}

	@Override
	public AdviceType getType() 
	{
		return this.type;
	}
	
	@Override
	public boolean isAuthorGuest() 
	{
		return this.authorGuest;
	}
	
	//helper method that currently only serves the abstract getResponses()
	protected void verifyType(AdviceType expectedType, String errorMessage) 
	{
		if(getType() != expectedType)
			throw new AskPeopleException(errorMessage);
	}
}
