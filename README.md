# pipelines

This repository is part of the FREME project ([official project home page](http://www.freme-project.eu/), [GitHub](https://github.com/freme-project)).
It contains code of the Pipelining service, which allows to make a chain of requests to other services in one request.

A detailed description of the API, together with an online test form can be found at <http://api.freme-project.eu/doc/>.

This service runs in a FREME distribution (recommended way); here are the [installation instructions](https://github.com/freme-project/technical-discussion/wiki/Developers-getting-started-guide).

However, the instructions below allow it to run as a standalone service.

## Prerequisites
You need

* Java >= 7 (from release 0.4 on: >= 8)
* Maven 3
* Git

## Add maven repository credentials
See [this instruction](https://github.com/freme-project/technical-discussion/wiki/Developers-getting-started-guide#add-maven-repository-credentials)
as explained in the Developer's getting started guide.

## Building
This step assumes you have the repository cloned, and we assume the directory name is `pipelines`.

	cd pipelines
	mvn clean install

This creates 2 jars in the `target` folder:

* pipelines-*version*.jar
* pipelines-*version*-jar-with-dependencies.jar (use this one to run)

## Starting the service

Again, the recommended way is [starting the Broker](https://github.com/freme-project/technical-discussion/wiki/Developers-getting-started-guide),
as the behaviour is slightly different. **Use this way only for debugging purposes!**

From the command line:

	java -jar target/pipelines-<version>-jar-with-dependencies.jar

From your favourite IDE:

The Main class is `eu.freme.eservices.pipelines.Run`. No further arguments required.

This starts a server listening on port 8080. You can send POST requests to
<http://localhost:8080/pipelining/chain>.

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

A test page for online testing with more examples can be found [here](http://api.freme-project.eu/doc/0.3/pipelining/post_pipelining_chain).

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

## License

Copyright 2015 Deutsches Forschungszentrum für Künstliche Intelligenz

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

This project uses 3rd party tools. You can find the list of 3rd party tools including their authors and licenses [here](LICENSE-3RD-PARTY).
