package dte.controlpeople.author;

/**
 * Represents the author of a {@link dte.controlpeople.question.AskPeopleQuestion Question} or an {@link dte.controlpeople.advice.AskPeopleAdvice Advice}.
 */
public class AskPeopleAuthor
{
    private final String name;
    private final Type type;

    public AskPeopleAuthor(String name, Type type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public Type getType()
    {
        return this.type;
    }



    public enum Type
    {
        REGISTERED_USER,
        GUEST,
        ORIGINAL_POSTER;
    }
}