package be.lanssens.lambdas;


import be.lanssens.models.FormBackup;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.StringUtils;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.util.Map;

import static org.apache.commons.net.util.Base64.decodeBase64;

public class S3FileStoreLambda implements RequestHandler<FormBackup, String>{

    Logger log = Logger.getLogger( S3FileStoreLambda.class );

    //NOTE: This lambda needs atleast 512MB of memory
    public String handleRequest( FormBackup formBackup, Context context ){
        Map<String, String> env = System.getenv();

        String accessKey =  env.get( "AWS_LAMBDA_ACCESS_KEY" );
        String accessSecret =  env.get( "AWS_LAMBDA_ACCESS_SECRET" );

        if( StringUtils.isNullOrEmpty( accessKey ) || StringUtils.isNullOrEmpty( accessSecret ) )
            return "No enviroment variables found...";

        AWSCredentials credentials = new BasicAWSCredentials(accessKey, accessSecret);

        AmazonS3Client s3Client = new AmazonS3Client(credentials);
        Region region = Region.getRegion( Regions.EU_WEST_1);
        s3Client.setRegion(region);

        log.info( "AmazonS3client has been initialized..." );

        try {
            log.info( "Started uploading file..." );

            byte[] fileData =  decodeBase64(formBackup.getBase64Value().getBytes());

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(fileData.length);

            PutObjectRequest putRequest = new PutObjectRequest(formBackup.getS3BucketUrl(),
                    formBackup.getFileName(), new ByteArrayInputStream(fileData),
                    metadata);

            log.info( "PutObject created..." );

            PutObjectResult result = s3Client.putObject(putRequest);

            log.info( "PutObject posted..." );

            return "Worked";
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which " +
                    "means your request made it " +
                    "to Amazon S3, but was rejected with an error response" +
                    " for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

            return "Didn't work";
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which " +
                    "means the client encountered " +
                    "an internal error while trying to " +
                    "communicate with S3, " +
                    "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());

            return "Didn't work";
        }

    }
}
