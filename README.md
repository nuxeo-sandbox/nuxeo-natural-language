# nuxeo-natural-language

**WORK IN PROGRESS - WORK IN PROGRESS - WORK IN PROGRESS**

_This "WORK IN PROGRESS" includes this README :-)_


## About
This plugin provides a service allowing for Natural Language Processing (NLP). To cite Google Natural Language API documentation:

> [...] reveals the structure and meaning of text [...]

NLP extracts information from the text and allows for:

* Language detection
* Sentiment analysis: Determines a writer's attitude as positive, negative, or neutral
* Entity analysis (people, organizations, places, events, ...)
* Syntax analysis: Extracts tokens (usually each word) and gives information about it (a verb, a noun, ...)

The plug-in is built as a service, and it is possible to contribute _providers_ that performs the analysis. In this first implementation, the [Google Natural Language API](https://cloud.google.com/natural-language) is used as provider.

The API also allows to extract all information, or only the features needed (for example, extract only sentiment, or only entities, or both, ...)

## Usage
 
### Limitations
The service acceptes to process a Document (it will extract its blob), a Blob or directly a string. When passing Document/Blob, the plugin extracts the full, raw, text (with no formatting, tags, ...). When using a string directly, please notice the service Accepts only plain text (not HTML)

### Checking/Saving the Result of the Analyzis
The plug-in does not automatically extract the information (no _listener_ is involved). Nuxeo developers using the plug-in will have to build the custom schemas they need to store the information. This is because the amount of data returned can be huge, not every document has to be analyzed, each application has different needs, ... For example, the syntax analysis returns information about almost every word in the text.

A typical way to extract the information from a Studio project is the following:

* Create the schema(s) you need to store the information (if information has to be stored)
* Add an Event Handler, typically for `Document Created` and `Document Modified`. We recommend using an _asynchronous_ handler, since an external service is called, and depending of the amount of data, the service can take time to return the result.
* In the handler, call the one of the operations provided by the plugin (`Services.NaturalLanguageOnDocumentOp`, ...), using JavaScript automation (it will be easier to parse and handle the resuls). Call it with the features you need, parse and handle the result.

### Example of JS Automation Using an Operaiton

```
// input is a File containing a valid analyzable file (pdf, Word, ...)
function run(input, params) {
  
  var response, sentences, i, max, entities;
  
  Services.NaturalLanguageDocumentOp(
    input, {
      'features': ['DOCUMENT_SENTIMENT', 'ENTITIES'], //'SYNTAX'
      'outputVariable': "response" /*,
      'provider': ,
      'xpath': */
    });
  
  response = ctx.response;
  Console.log("Language: " + response.getLanguage());
  Console.log("Sentiment Score: " + response.getSentimentScore());
  Console.log("Sentiment Magnitude: " + response.getSentimentMagnitude());
  
  Console.log("===============> sentences");
  sentences = response.getSentences();
  if(sentences !== null) {
    max = sentences.length;
    for(i = 0; i < max; ++i) {
      Console.log("sentences #" + (i + 1) + ":\n" + sentences[i]);
    }
  }
  
  Console.log("===============> ENTITIES");
  entities = response.getEntities();
  if(entities !== null) {
    max = entities.length;
    for(i = 0; i < max; ++i) {
      Console.log("Entity #" + (i + 1) + ":\n" + entities[i].toString());
    }
  }
  
  return input;

}
```



## Google Natural Language API: Authenticating to the Service

- Enable the Natural Language API from the google developer console
- As of August 2017, billing must be activated in your google account in order to use the Natural Language API
- Get a credential file from the Google Developer Console
- Store it in in you Dev/Test/Prod Nuxeo server
- Set the `org.nuxeo.natural.language.google.credential` (in nuxeo.conf) parameter to the full path to this credentials file.

#### Algorithm Used when Connecting to the Service
 1. If the `org.nuxeo.natural.language.google.credential` configuraiton parameter is set, use it
 2. Else, read the `GOOGLE_APPLICATION_CREDENTIALS` Environment Variable (this is common Google variable, set to access its misc. APIs)
 3. If none of the previous returned a value, the call will fail

 
## Build
#### Requirements
Build requires the following software:
- git
- maven

```
git clone https://github.com/nuxeo-sandbox/nuxeo-natural-language
cd nuxeo-natural-language
mvn clean install
```
Notice that for unit test, credentials are set differenty (see below or in the source code). To compile without running the tests:

```
mvn clean install -DskipTests=true
#to compile the test without running them:
mvn test-compile
```

## Deploy

As of this version, only Google is supported as provider:

- Enable the Natural Language API from the google developer console
- As of August 2017, billing must be activated in your google account in order to use the Natural Language API
- Get a credential file from the Google Developer Console
- Store it in in you Dev/Test/Prod Nuxeo server
- Edit nuxeo.conf and add the path to the credentials file
```
org.nuxeo.natural.language.google.credential=PATH_TO_JSON_CREDENTIAL_FILE
```
  - Alternatively, you can set the `GOOGLE_APPLICATION_CREDENTIALS` environmenent variable
 
- Install the marketplace package
 
# Resources (Documentation and other links)
TBD

[Google Natural Language API](https://cloud.google.com/natural-language)
 
# License
[Apache License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)
 
# About Nuxeo
The [Nuxeo Platform](http://www.nuxeo.com/products/content-management-platform/) is an open source customizable and extensible content management platform for building business applications. It provides the foundation for developing [document management](http://www.nuxeo.com/solutions/document-management/), [digital asset management](http://www.nuxeo.com/solutions/digital-asset-management/), [case management application](http://www.nuxeo.com/solutions/case-management/) and [knowledge management](http://www.nuxeo.com/solutions/advanced-knowledge-base/). You can easily add features using ready-to-use addons or by extending the platform using its extension point system.
 
The Nuxeo Platform is developed and supported by Nuxeo, with contributions from the community.
 
Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with
SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Netflix, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris.
More information is available at [www.nuxeo.com](http://www.nuxeo.com).