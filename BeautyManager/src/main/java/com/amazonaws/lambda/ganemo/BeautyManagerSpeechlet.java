/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package com.amazonaws.lambda.ganemo;

import java.awt.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.Directive;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.IntentRequest.DialogState;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.speechlet.dialog.directives.DelegateDirective;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

/**
 * This sample shows how to create a simple speechlet for handling speechlet
 * requests.
 */
public class BeautyManagerSpeechlet implements Speechlet {
	private static final Logger log = LoggerFactory.getLogger(BeautyManagerSpeechlet.class);

	private String clientId;

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any initialization logic goes here
		clientId = session.getUser().getUserId();
		BeautyManagerResponseManager.dynamoDB = new DynamoDB(
				AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_EAST_1).build());
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		return getWelcomeResponse();
	}

	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		Intent intent = request.getIntent();
		String intentName = (intent != null) ? intent.getName() : null;

		if (intentName.equals("StartUp")) {
			if (request.getDialogState() == DialogState.STARTED) {
				return BeautyManagerResponseManager.buildDialogueDelegateResponse(intent, "This Shouldn't Fire");
			} else if (request.getDialogState() == DialogState.COMPLETED) {
				return getVerifyUserResponse(intent);
			} else {
				DelegateDirective dd = new DelegateDirective();

				ArrayList<Directive> directiveList = new ArrayList<Directive>();
				directiveList.add(dd);

				SpeechletResponse speechletResp = new SpeechletResponse();
				speechletResp.setDirectives(directiveList);
				speechletResp.setShouldEndSession(false);
				return speechletResp;
			}
		}

		// Get Dialog State
		DialogState dialogueState = request.getDialogState();

		return getVerifyUserResponse(intent);
	}

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());
		// any cleanup logic goes here
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} with a welcome message.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechText = "Welcome to Beauty Manager for Alexa. You can schedule your Beauty Regimens through me!";

		// Create the Simple card content.
		SimpleCard card = new SimpleCard();
		card.setTitle("Welcome to Beauty Manager!");
		card.setContent(speechText);

		// Create the plain text output.
		PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
		speech.setText(speechText);

		return SpeechletResponse.newTellResponse(speech, card);
	}

	/**
	 * Creates and returns a {@code SpeechletResponse} trying to verify the
	 * user.
	 * 
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getVerifyUserResponse(Intent intent) {

		String name = intent.getSlot("Name").getValue();
		
		String speechResponse = "Hello " + name + "!";

		if (BeautyManagerResponseManager.verifyUser(clientId)) {
			speechResponse += " What would you like to do?";
		} else {
			speechResponse += "It doesn't look like we've met before. I'll remember you. What would you like to do?";
		}

		return BeautyManagerResponseManager.getSpeechletResponse(speechResponse, speechResponse, "Hello!");
	}

}
