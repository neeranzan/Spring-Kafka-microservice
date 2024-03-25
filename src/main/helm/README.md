
# Building

## Local

###  Build image and helm packaging 

 - Make sure docker daemon is running
 - If you are running Minikube, first you need to point your shell to minikube's docker-daemon before executing the build command.
   To do that execute : `eval $(minikube docker-env) ` in your terminal.

Run build command:

> **_NOTE:_** you can skip this and follow deploy section below if you want to build & deploy at the same time.

```
mvn clean install -DskipTests
```
This will create helm package under `target/helm` directory and create image to local docker repo.

## DEV

# Deploying

##  Local:

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

## DEV

Run deploy command:
```
mvn clean deploy -Ddeployment.type=snapshot
```


