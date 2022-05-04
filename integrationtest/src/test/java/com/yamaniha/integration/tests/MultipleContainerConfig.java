package com.yamaniha.integration.tests;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.testcontainers.containers.localstack.LocalStackContainer.Service.*;

public class MultipleContainerConfig {

    public static final String LOCALSTACK_IMAGE = "localstack/localstack:0.11.3";
    public static final String APP_IMAGE = "app:0.0.3";
    public static final int APP_PORT = 9080;

    private final LocalStackContainer localstack;
    private final GenericContainer microservice;


    public MultipleContainerConfig() {

        final Network network = Network.newNetwork();



        final DockerImageName localstackImage = DockerImageName.parse(LOCALSTACK_IMAGE);
        localstack = new LocalStackContainer(localstackImage)
                .withNetwork(network)
                .withNetworkAliases("localstack")
                .withServices(S3, SQS, DYNAMODB);

        localstack.start();


        final DockerImageName appImage = DockerImageName.parse(APP_IMAGE);
        microservice = new GenericContainer(appImage)
                .withNetwork(network)
                .withEnv("PORT", String.format("%d",APP_PORT))
                .withEnv("DYNAMO-ENDPOINT","http://localstack:4569")
                .withEnv("AWS-ACCESS-KEY","123")
                .withEnv("AWS-SECRET-KEY","123")
                .withEnv("AWS-REGION","us-east-1")
                .withExposedPorts(APP_PORT);

        microservice.setPortBindings(List.of(APP_PORT +":" + APP_PORT));
        microservice.waitingFor(Wait.forHttp("/actuator"));

    }

    public LocalStackContainer getLocalstack() {
        return localstack;
    }

    public GenericContainer getMicroservice() {
        return microservice;
    }

    public void start() {


        microservice.start();

    }

    public void stop() {

        microservice.stop();
        localstack.stop();

    }

}
