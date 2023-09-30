# Control People
This is rather a joke about the website's name, which means that everything in the website can be automated.

## AskPeople Wrapper
This project is an abstract wrapper to the [AskPeople](https://www.askpeople.co.il/) website, The primary interfaces are: 
- `AskPeopleClient` - The entry point of the wrapper.
- `AskPeopleQuestion` - Supplies advice objects with various functionalities: reading the content, liking, etc.
- `AskPeopleAdvice` - Exactly what the name suggests.

The usage of the library begins with a `AskPeopleClient` object, which currently can be retreived by:
```java
AskPeopleClient client = AskPeopleClient.ofSelenium();
```
This interface provides objects that represent components in the website, such as `AskPeopleAdvice`s.

## How to use?
The following examples assume you have a client object named `client`:

* Get a question by the ID in the website:
```java
AskPeopleQuestion question = client.getQuestionByID("123");
```

* Loop over the advices of the question:
```java
for(AskPeopleAdvice advice : question.getAdvices()) 
{
    LOGGER.info("Advice by: {}", advice.getCommentorName());
}
```

* Dislike or Like an advice:
```java
AskPeopleAdvice advice = ...;

//it's not always possible rate advices, for example when they're already rated
if(advice.canBeDisliked())
  advice.dislike();
```

## Workflow Examples
* <ins>Under **your** responsibility</ins>, this library can be used to do malicious stuff:
```java
List<AskPeopleAdvice> targetAdvices = question.selectAdvices() //returns a customizable list, in contrast to getAdvices()
    .byUser("Hated User") //select only the advices of a specific user
    .withResponses() //includes the responses to the user's advices
    .filter(AskPeopleAdvice::canBeDisliked) //apply a custom predicate
    .get();

for(AskPeopleAdvice advice : targetAdvices) 
{
    advice.dislike();
			
    //sleep for a random time
    TimeUnit.SECONDS.sleep(1 + ThreadLocalRandom.current().nextInt(3));
}
```
