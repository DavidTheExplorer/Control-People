package dte.controlpeople.advice;

public abstract class AbstractAdvice implements AskPeopleAdvice
{
	private final String commentorName;
	private final AdviceType type;
	
	protected AbstractAdvice(String commentorName, AdviceType type)
	{
		this.commentorName = commentorName;
		this.type = type;
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
	
	//helper method that currently only serves the abstract getResponses()
	protected void verifyType(AdviceType expectedType, String errorMessage) 
	{
		if(getType() != expectedType)
			throw new UnsupportedOperationException(errorMessage);
	}
}
