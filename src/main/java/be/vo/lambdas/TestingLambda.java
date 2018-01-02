package be.vo.lambdas;

import be.vo.models.SimplePOJO;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

public class TestingLambda implements RequestHandler<SimplePOJO, String>{

    public String handleRequest( SimplePOJO o, Context context ){
        Map<String, String> env = System.getenv();

        String accessKey =  env.get( "S3_ACCESS_KEY" );
        String accessSecret =  env.get( "S3_ACCESS_SECRET" );

        return ("Hello, " + o.getFirstName() + " " + o.getLastName() + "!\n"
                + "These are your env variables:" + accessKey + " " + accessSecret) ;
    }
}
