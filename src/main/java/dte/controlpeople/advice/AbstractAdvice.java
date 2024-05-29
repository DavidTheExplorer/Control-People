package dte.controlpeople.advice;

import dte.controlpeople.author.AskPeopleAuthor;

public abstract class AbstractAdvice implements AskPeopleAdvice
{
	private final Type type;
	private final AskPeopleAuthor author;

	protected AbstractAdvice(Type type, AskPeopleAuthor author)
	{
		this.type = type;
		this.author = author;
	}

	@Override
	public AskPeopleAuthor getAuthor()
	{
		return this.author;
	}

	@Override
	public Type getType()
	{
		return this.type;
	}
}
