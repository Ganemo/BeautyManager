package com.amazonaws.lambda.ganemo;

import java.util.Arrays;

import com.amazonaws.SDKGlobalConfiguration;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;

public class DynamoDBTesting {

	public static void main(String[] args) {
		SDKGlobalConfiguration.isInRegionOptimizedModeEnabled();
		AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build();
		
		 DynamoDB dynamoDB = new DynamoDB(client);
		 
		 String tableName = "BeautyRegiment";
		 
		 try {
			 CreateTableRequest request = new CreateTableRequest()
					 .withTableName(tableName)
					 .withKeySchema(Arrays.asList(new KeySchemaElement().withAttributeName("Id").withKeyType(KeyType.HASH)))
					 .withAttributeDefinitions(Arrays.asList(new AttributeDefinition().withAttributeName("Id").withAttributeType("N")))
					 .withProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits(5L).withWriteCapacityUnits(6L));
			 
			 Table table = dynamoDB.createTable(request);
			 table.waitForActive();
			 System.out.println(table.getTableName());
		 } catch (Exception e) {
			 System.err.println("Unable to create table: ");
	         System.err.println(e.getMessage());
		 }
	}

}
