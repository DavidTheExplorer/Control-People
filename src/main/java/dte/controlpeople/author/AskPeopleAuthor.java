package dte.controlpeople.author;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents the author of a {@link dte.controlpeople.question.AskPeopleQuestion Question} or an {@link dte.controlpeople.advice.AskPeopleAdvice Advice}.
 */
public class AskPeopleAuthor
{
    private final String name;
    private final int age;
    private final Type type;

    private static final Pattern AGE_PATTERN = Pattern.compile("(בן|בת) \\d+");

    public AskPeopleAuthor(String name, int age, Type type)
    {
        this.name = name;
        this.age = age;
        this.type = type;
    }

    public String getName()
    {
        return this.name;
    }

    public int getAge()
    {
        return this.age;
    }

    public Type getType()
    {
        return this.type;
    }

    //ugly here, but will be here temporarily
    public static int extractAge(String fullName)
    {
        Matcher matcher = AGE_PATTERN.matcher(fullName);
        matcher.find();
        String[] sexAndAge = fullName.substring(matcher.start(), matcher.end()).split(" ");

        return Integer.parseInt(sexAndAge[1]);
    }



    public enum Type
    {
        REGISTERED_USER,
        GUEST,
        ORIGINAL_POSTER;
    }
}