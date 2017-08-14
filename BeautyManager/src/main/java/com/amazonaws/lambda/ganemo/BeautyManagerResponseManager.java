package com.amazonaws.lambda.ganemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.speechlet.dialog.directives.DialogIntent;
import com.amazon.speech.speechlet.dialog.directives.DialogSlot;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.QueryOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.internal.IteratorSupport;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;
import com.amazonaws.services.dynamodbv2.document.utils.ValueMap;

public class BeautyManagerResponseManager {

	public static DynamoDB dynamoDB;
	public static ItemCollection<QueryOutcome> possibleUsers;

	public static String processInputIntent(Intent intent) {

		switch (intent.getName()) {
		case "Startup":

			break;

		default:
			break;
		}

		return null;
	}

	public static DialogIntent buildDialogueIntent(String intentName, Map<String, Slot> slots) {
		DialogIntent dialogIntent = new DialogIntent();
		dialogIntent.setName(intentName);
		Map<String, DialogSlot> dialogSlots = new HashMap<String, DialogSlot>();

		Iterator<Entry<String, Slot>> itr = slots.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Slot> pair = itr.next();
			DialogSlot diagSlot = new DialogSlot();
			diagSlot.setName(((Slot) pair.getValue()).getName());
			dialogSlots.put((String) pair.getKey(), diagSlot);
		}

		dialogIntent.setSlots(dialogSlots);

		return dialogIntent;
	}

	public static SpeechletResponse buildDialogueDelegateResponse(Intent intent, String speechText) {
		DelegateDirective dd = new DelegateDirective();

		dd.setUpdatedIntent(BeautyManagerResponseManager.buildDialogueIntent(intent.getName(), intent.getSlots()));

		ArrayList<Directive> directiveList = new ArrayList<Directive>();
		directiveList.add(dd);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		SpeechletResponse speechletResp = new SpeechletResponse();
		// speechletResp.setOutputSpeech(speech);
		speechletResp.setDirectives(directiveList);
		speechletResp.setNullableShouldEndSession(false);
		return speechletResp;
	}

	public static boolean verifyUser(String id) {
		Table table = dynamoDB.getTable("BeautyManagementData");

		QuerySpec spec = new QuerySpec().withKeyConditionExpression("Id = :v_id")
				.withValueMap(new ValueMap().withString(":v_id", id));

		possibleUsers = table.query(spec);
		IteratorSupport<Item, QueryOutcome> itr = possibleUsers.iterator();

		return itr.hasNext();
	}

	public static SpeechletResponse getSpeechletResponse(String speechText, String cardTitle) {
		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle(cardTitle);
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return SpeechletResponse.newTellResponse(speech, card);
	}

	public static SpeechletResponse getSpeechletResponse(String speechText, String repromptText, String cardTitle) {

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle(cardTitle);
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);
		// Create the plain text output.
		PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
		speech.setText(repromptText);
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptSpeech);

		return SpeechletResponse.newAskResponse(speech, reprompt, card);
	}
}
