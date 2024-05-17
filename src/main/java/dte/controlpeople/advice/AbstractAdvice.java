package dte.controlpeople.advice;

public abstract class AbstractAdvice implements AskPeopleAdvice
{
	private final String authorName;
	private final Type type;
	private final AuthorType authorType;
	
	protected AbstractAdvice(String authorName, Type type, AuthorType authorType)
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
	public Type getType()
	{
		return this.type;
	}

	@Override
	public AuthorType getAuthorType()
	{
		return this.authorType;
	}
}
