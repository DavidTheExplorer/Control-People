package dte.controlpeople.advice;

import dte.controlpeople.adviceselector.ResponseSelector;
import dte.controlpeople.exceptions.AskPeopleException;

public abstract class AbstractAdvice implements AskPeopleAdvice
{
	private final String commentorName;
	private final AdviceType type;
	private final AuthorType authorType;
	
	protected AbstractAdvice(String commentorName, AdviceType type, AuthorType authorType)
	{
		this.commentorName = commentorName;
		this.type = type;
		this.authorType = authorType;
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
	public AuthorType getAuthorType()
	{
		return this.authorType;
	}

	@Override
	public ResponseSelector selectResponses()
	{
		return new ResponseSelector(this);
	}

	//helper method that currently only serves the abstract getResponses()
	protected void verifyType(AdviceType expectedType, String errorMessage) 
	{
		if(getType() != expectedType)
			throw new AskPeopleException(errorMessage);
	}
}
