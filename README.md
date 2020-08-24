ManyWho Azure Service
=====================

This service allows you to integrate your Flows with Azure Id Provider

Before running the app for first time the admin need to consent permissions executing this URL in the browser:


TTo authorize the Boomi Flow app for the whole organization the admin should give permissions following this link:

https://login.microsoftonline.com/common/oauth2/authorize?client_id={clientId}&response_type=code&redirect_uri={redirect-uri}%2Fauthorization&nonce={random-nonce}&resource=00000002-0000-0000-c000-000000000000&prompt=admin_consent

> The `redirect_uri` parameter should be URL-encoded, like `https%3A%2F%2Fservices.manywho.com%2Fapi%2Fazure%2F1%2Fcallback%2Fadmin%2Fauthorization`

#### Building 

To build the service, you will need to have Apache Ant, Maven 3 and Java 8.

Configure the Azure oauth2 client credentials using the environment variables `oauth2.clientId` and `oauth2.clientSecret`.
You can also generate a file service.property using Apache Ant:

```bash
$ ant generate-properties -Doauth2.clientId={client-id} -Doauth2.clientSecret={client-secret}
```

#### Running

The service is a Jersey JAX-RS application, that by default is run under the Grizzly2 server on port 8080 (if you use 
the packaged JAR).

##### Defaults

Running the following command will start the service listening on `0.0.0.0:8080/api/azure/1`:

```bash
$ java -jar target/azure-1.0-SNAPSHOT.jar
```

##### Custom Port

You can specify a custom port to run the service on by passing the `server.port` property when running the JAR. The
following command will start the service listening on port 9090 (`0.0.0.0:9090/api/azure/1`):

```bash
$ java -Dserver.port=9090 -jar target/azure-1.0-SNAPSHOT.jar
```

## Contributing

Contribution are welcome to the project - whether they are feature requests, improvements or bug fixes! Refer to 
[CONTRIBUTING.md](CONTRIBUTING.md) for our contribution requirements.

## License

This service is released under the [MIT License](http://opensource.org/licenses/mit-license.php).
