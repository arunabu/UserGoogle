package com.example.usergoogle;

import com.amazonaws.mobileconnectors.lambdainvoker.LambdaFunction;
public interface MyInterface {

    /**
     * Invoke the Lambda function "AndroidBackendLambdaFunction".
     * The function name is the method name.
     */
    @LambdaFunction
    ResponseClass AndroidBackendLambdaFunction(RequestClass request);

    @LambdaFunction
    ResponseClass AddFunction(RequestClass request);


    @LambdaFunction
    ResponseClass updateFunction(RequestClass request);



    @LambdaFunction
    ResponseClass DeleteFunction(RequestClass request);



    @LambdaFunction
    ResponseClass GetItemFunction(RequestClass request);
}
