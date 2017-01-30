# pipelines

**This repository is deprecated. It has moved to the [basic services repository](https://github.com/freme-project/basic-services/tree/master/controllers/pipelines).**

This repository is part of the FREME project ([official project home page](http://www.freme-project.eu/), [GitHub](https://github.com/freme-project)).
It contains code of the Pipelining service, which allows to make a chain of requests to other services in one request.

A detailed description of the API, together with an online test form can be found at <http://api.freme-project.eu/doc/>.

This service runs in a FREME distribution; here are the [installation instructions](https://github.com/freme-project/technical-discussion/wiki/Developers-getting-started-guide).

However, you can build it as a library to use in your own code.

## Prerequisites
You need

* Java >= 8
* Maven 3
* Git

## Add maven repository credentials
See [this instruction](https://github.com/freme-project/technical-discussion/wiki/Developers-getting-started-guide#add-maven-repository-credentials)
as explained in the Developer's getting started guide.

## Building
This step assumes you have the repository cloned, and we assume the directory name is `pipelines`.

	cd pipelines
	mvn clean install

This creates a jar `pipelines-*version*.jar` in the `target` folder:

## Starting the service

This service is part of the [Broker](https://github.com/freme-project/technical-discussion/wiki/Developers-getting-started-guide).

## Sending requests

### Test via the command line
This assumes you have the program `curl` available.

Create a file `example.json` with the following contents:

```
[
{
  "method": "POST",
  "endpoint": "http://api.freme-project.eu/current/e-entity/dbpedia-spotlight/documents",
  "parameters": {
    "language": "en"
  },
  "headers": {
    "content-type": "text/plain",
    "accept": "text/turtle"
  },
  "body": "Ghent is one of the most beautiful cities in Belgium."
},
{
  "method": "POST",
  "endpoint": "http://api.freme-project.eu/current/e-link/documents/",
  "parameters": {
    "templateid": "3"
  },
  "headers": {
    "content-type": "text/turtle",
    "accept": "text/turtle"
  }
}
]
```

This will be the body of the request to the pipelining service. It represents a
request to the online e-Entity service and then that output is used for the next
request, which is an e-Link request.

Send the request to the service:

	curl -X POST --header "Content-Type: application/json" -d @example.json http://localhost:8080/pipelining/chain

A test page for online testing with more examples can be found [here](http://api.freme-project.eu/doc/current/pipelining/post_pipelining_chain).

### Creating request bodies

There are classes in the repository that ease making a valid request body.

#### RequestFactory

The `eu.freme.eservices.pipelines.requests.RequestFactory` creates some predefined requests.

Usage:

	import eu.freme.eservices.pipelines.requests.RequestFactory;
	import eu.freme.eservices.pipelines.requests.SerializedRequest;
	...
	
		// somewhere in your code you want to create the example above.
		
		// first, define the text to enrich and create an e-Entity request.
		String text = "Ghent is one of the most beautiful cities in Belgium."
		SerializedRequest entityRequest = RequestFactory.createEntitySpotlight(text, "en");
		
		// then, create an e-Link request, that uses template 3 (add geo coordinates)
		SerializedRequest linkRequest = RequestFactory.createLink("3");
		
		// now create the JSON representation of the pipeline of requests:
		String jsonBody = RequestFactory.toJson(entityRequest, linkRequest);

#### RequestBuilder

The `eu.freme.eservices.pipelines.requests.RequestBuilder` allows to create custom requests.

Let's make the same request as in the example of the requestFactory.

Usage:

	import eu.freme.eservices.pipelines.requests.RequestBuilder;
	import eu.freme.eservices.pipelines.requests.RequestFactory;
	import eu.freme.eservices.pipelines.requests.SerializedRequest;
	import eu.freme.eservices.pipelines.requests.ServiceConstants;
	import eu.freme.conversion.rdf.RDFConstants;
	...
	
		// somewhere in your code you want to create the example above.
		
		// first, define the text to enrich and create an e-Entity request.
		String text = "Ghent is one of the most beautiful cities in Belgium."
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_ENTITY_SPOTLIGHT.getUri());
		SerializedRequest entityRequest = builder
				.informat(RDFConstants.RDFSerialization.PLAINTEXT)
				.parameter("language", "en")
				.body(text).build();
		
		// then, create an e-Link request, that uses template 3 (add geo coordinates)
		RequestBuilder builder = new RequestBuilder(ServiceConstants.E_LINK.getUri());
		SerializedRequest linkRequest = builder
				.parameter("templateid", "3")
				.build();
		
		// now create the JSON representation of the pipeline of requests:
		String jsonBody = RequestFactory.toJson(entityRequest, linkRequest);
