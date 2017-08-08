package com.amazonaws.lambda.ganemo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Object, String>, Speechlet {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);

        context.getFunctionName();
        
        
        // TODO: implement your handler
        return "Hello from Lambda! " + context.getIdentity();
    }

}
