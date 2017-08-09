package com.amazonaws.lambda.ganemo;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;

public class DynamoDBTesting {

	public static void main(String[] args) {
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();

		DynamoDB dynamoDB = new DynamoDB(client);

		String tableName = "Movies";

		try {
			/*
			 * CreateTableRequest request = new CreateTableRequest()
			 * 		.withTableName(tableName)
					.withKeySchema(Arrays.asList(new KeySchemaElement()
						.withAttributeName("Id")
						.withKeyType(KeyType.HASH)))
					.withAttributeDefinitions(Arrays.asList(new AttributeDefinition()
						.withAttributeName("Id")
						.withAttributeType("N")))
					.withProvisionedThroughput(new ProvisionedThroughput()
						.withReadCapacityUnits(5L)
						.withWriteCapacityUnits(6L));
			 * 
			 * Table table = dynamoDB.createTable(request);
			 */
			/*
			 * Table table = dynamoDB.createTable(tableName, Arrays.asList(
			 * 		new KeySchemaElement("year", KeyType.HASH), // Partition // key
			 * 		new KeySchemaElement("title", KeyType.RANGE)), // Sort key
			 * Arrays.asList(
			 * 		new AttributeDefinition("year", ScalarAttributeType.N),
			 * 		new AttributeDefinition("title", ScalarAttributeType.S)),
			 * 		new ProvisionedThroughput(10L, 10L));
			 */

			Table table = dynamoDB.getTable("Movies");

			table.waitForActive();
			System.out.println(table.getTableName() + "Created and ACTIVE");

			/*Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("plot", "Nothing happens at all.");
			infoMap.put("rating", 0);

			System.out.println("Adding a new item...");
			PutItemOutcome outcome = table.putItem(
					new Item().withPrimaryKey("year", 1999, "title", "TheMovieOfMovies").withMap("info", infoMap));

			System.out.println("PutItem succeeded:\n" + outcome.getPutItemResult());
			*/
			System.out.println("Attempting to read the item...");
            Item getoutcome = table.getItem(new GetItemSpec().withPrimaryKey("year", 1999, "title", "TheMovieOfMovies"));
            
            System.out.println("GetItem succeeded: " + (String)getoutcome.asMap().get("title"));
		} catch (Exception e) {
			System.err.println("Unable to create table: ");
			System.err.println(e.getMessage());
		}
	}

}
