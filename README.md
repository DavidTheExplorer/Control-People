# Control People
This library is an abstract wrapper of the [AskPeople](https://askpeople.co.il) website, with a default Selenium implementation.\
The name is joke about the website's name, which means that everything in it can be automated.

## How to use?
Start by retrieving a client instance:
```java
AskPeopleClient client = AskPeopleClient.ofSelenium();
```

Now you can:

* Get info about a question:
```java
AskPeopleQuestion question = client.getQuestionByID("123");

LOGGER.info("{} people wrote advices for this question.", question.getAdvices().size());
```

* Loop over the advices of the question:
```java
for(AskPeopleAdvice advice : question.getAdvices()) 
{
    LOGGER.info("Advice by: {}", advice.getAuthorName());
}
```

* Rate an advice:
```java
AskPeopleAdvice advice = ...;

advice.dislike();
```

## AdviceSelector
When working with advices, it's common to have the need for just some of them.\
Every class that supplies a list of advices also offers a `#select` method that returns an `AdviceSelector`:
```java
String username = "Specific User";

List<AskPeopleAdvice> targetAdvices = question.selectAdvices()
    .byUser(username) //filter by username
    .filter(advice -> {}) //we may want our own filter
    .withResponses() //we want his responses as well
    .get();

LOGGER.info("{} wrote {} advices in the question!", username, targetAdvices.size());
```
