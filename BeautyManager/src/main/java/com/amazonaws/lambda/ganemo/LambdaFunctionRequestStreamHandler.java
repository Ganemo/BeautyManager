package com.amazonaws.lambda.ganemo;

import java.util.Set;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

public class LambdaFunctionRequestStreamHandler extends SpeechletRequestStreamHandler {

	public LambdaFunctionRequestStreamHandler(Speechlet speechlet, Set<String> supportedApplicationIds) {
		super(speechlet, supportedApplicationIds);
		// TODO Auto-generated constructor stub
	}

}
