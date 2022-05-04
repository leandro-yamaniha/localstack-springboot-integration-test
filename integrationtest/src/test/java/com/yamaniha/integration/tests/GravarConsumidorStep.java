package com.yamaniha.integration.tests;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testcontainers.containers.localstack.LocalStackContainer;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB;

public class GravarConsumidorStep {

    private static MultipleContainerConfig config;

    @Given("microservico e aws iniciados")
    public void microservicoEAwsIniciados() throws InterruptedException {
        config = new MultipleContainerConfig();
        config.start();
        criartabelaDynamoDB(config.getLocalstack());
    }

    @When("realizar um cadastro de consumidor")
    public void realizarUmCadastroDeConsumidor() throws IOException, InterruptedException, URISyntaxException {

        final String requestBody  = "{\"company_name\": \"Empresa Inglesa LTDA\",\"company_document_number\": \"6598752300011\",\"phone_number\": \"12345678\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9080/v1/costumer"))
                .header("Content-Type", "application/json")
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());

    }

    @Then("quero consultar os dados do consumidor")
    public void queroConsultarOsDadosDoConsumidor() throws IOException, InterruptedException, URISyntaxException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:9080/v1/costumer/all"))
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build();

        HttpResponse<String> response = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build()
                .send(request, HttpResponse.BodyHandlers.ofString());


        assertEquals(200, response.statusCode());
    }

    private void criartabelaDynamoDB(LocalStackContainer localstack) throws InterruptedException {
        AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard()
                .withEndpointConfiguration(localstack.getEndpointConfiguration(DYNAMODB))
                .withCredentials(localstack.getDefaultCredentialsProvider())
                .build();
        DynamoDB dynamoDB = new DynamoDB(client);

        List<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
        attributeDefinitions.add(new AttributeDefinition().withAttributeName("id").withAttributeType("S"));

        List<KeySchemaElement> keySchema = new ArrayList<KeySchemaElement>();
        keySchema.add(new KeySchemaElement().withAttributeName("id").withKeyType(KeyType.HASH));

        CreateTableRequest request = new CreateTableRequest()
                .withTableName("Costumer")
                .withKeySchema(keySchema)
                .withAttributeDefinitions(attributeDefinitions)
                .withProvisionedThroughput(new ProvisionedThroughput()
                        .withReadCapacityUnits(5L)
                        .withWriteCapacityUnits(5L));

        Table table = dynamoDB.createTable(request);

        table.waitForActive();


    }
}
