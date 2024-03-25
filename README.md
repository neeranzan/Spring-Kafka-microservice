This is a Proof of Concept (PoC) sample consumer for our pub-sub model.

## KUBERNETES DEPLOYMENT

> **_NOTE:_**  `pom_containerize.xml pom file should be used as standard pom file for kubernetes deployment`

####  Build image and helm packaging

- Make sure docker daemon is running
- If you are running Minikube, first you need to point your shell to minikube's docker-daemon before executing the build command.
  To do that execute : `eval $(minikube docker-env) ` in your terminal.

Run build command:

> **_NOTE:_** you can skip this and follow deploy section below if you want to build & deploy at the same time.

```
mvn clean install -DskipTests
```
This will create helm package under `target/helm` directory and create image to local docker repo.

#### Deploying

Please ensure the following before deploying:

- k8 cluster is running on your local machine and `kubectl` and `helm` clients are configured (and available in PATH) to talk to same cluster.
- Dependency image - Init container  is available in local docker repo (configured in `pom.xml`). Execute `docker images` command in your terminal to confirm
  or else pull the image from remote repo.

Run deploy command:
```
mvn clean deploy -DskipTests -Dvault.token=<your_vault_token> -Dconsul.token=<your_consul_token>
```
> **_NOTE:_**  This micro will run in port 8082. In order to access from you local machine,  you will have to do port forwarding to the service running inside the cluster.
If you are deploying in minikube you can open new terminal and execute: `minikube tunnel`. Alternatively, you can use `kubectl port-forward` command  to map to different port.

To confirm app is running you can do: `curl -X  GET http://localhost:<port_number>`

## Local Deployment:

Before running the app, please set environment variables as follows:
```
APPLICATION_NAME=consumer
CONSUL_KV_PREFIX=<consul_kv_prefix>
SPRING_CONSUL_HTTP_ADDR=https://consul.com/
SPRING_CONSUL_HTTP_PORT=443
SPRING_CONSUL_HTTP_SCHEME=https
SPRING_PROFILES_ACTIVE=development
VAULT_ADDR=https://vault.com/
VAULT_KV_BACKEND=kv
VAULT_KV_CONTEXT=<vault_kv_context>

VAULT_TOKEN=<your_vault_token>
CONSUL_HTTP_TOKEN=<your_consule_token>
```
Now, you can simply run it as a java application. The main class is `ConsumerApplication.java`.

This application integrates with Confluent Kafka and Amazon Aurora Postgres database.
To publish  message to kafka, you can execute
```
 curl -X POST -H "Content-Type: application/json" \
--data '{"merchantName": "Test Merchant","status":"true"}' \  
http://localhost:8082/api/v1/kafka/publish/profile
```

