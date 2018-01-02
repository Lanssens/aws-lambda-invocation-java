package be.lanssens;

import be.lanssens.models.FormBackup;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambdaAsyncClient;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class LambdaInvoker{

    public LambdaInvoker() throws JsonProcessingException{
        Map<String, String> env = System.getenv();

        String accessKey =  env.get( "AWS_LAMBDA_ACCESS_KEY" );
        String accessSecret =  env.get( "AWS_LAMBDA_ACCESS_SECRET" );
        String bucketUrl =  env.get( "AWS_LAMBDA_S3_BUCKET_URL" );
        String lambdaName =  env.get( "AWS_LAMBDA_FUNCTION_NAME" );

        if( StringUtils.isNullOrEmpty( accessKey ) || StringUtils.isNullOrEmpty( accessSecret ) || StringUtils.isNullOrEmpty( bucketUrl ) || StringUtils.isNullOrEmpty( lambdaName ) )
            System.out.println("System variables not found");

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);

        AWSLambdaAsyncClient client = new AWSLambdaAsyncClient(credentials);
        Region region = Region.getRegion( Regions.EU_WEST_1);
        client.withRegion( region );

        FormBackup formBackup = new FormBackup();
        formBackup.setBase64Value( "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" );
        formBackup.setFileName( "test.png" );
        formBackup.setS3BucketUrl( bucketUrl);

        ObjectMapper mapper = new ObjectMapper();
        String payload = mapper.writeValueAsString(formBackup);

        InvokeRequest request = new InvokeRequest();
        request.setInvocationType("Event");
        request.withFunctionName(lambdaName).withPayload(payload);
        InvokeResult invoke = client.invoke(request);

        System.out.println( "done" );
    }

    public static void main(String[] args) throws JsonProcessingException{
        LambdaInvoker lambdaInvoker = new LambdaInvoker();
    }
}
