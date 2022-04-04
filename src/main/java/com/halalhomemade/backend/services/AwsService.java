package com.halalhomemade.backend.services;

import com.halalhomemade.backend.repositories.UserRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.MessageAttributeValue;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AwsService extends AbstractService {

	@Value("${aws.s3.bucket}")
    private String bucketName;
	
	@Autowired private CognitoIdentityProviderClient cognitoClient;
	@Autowired private S3Client s3Client;
	@Autowired private SnsClient snsClient;
	@Autowired private ApplicationEventPublisher eventPublisher;
	@Autowired private UserRepository userRepository;
  
	///------ SNS Service ------///
	public void pubTextSMS(String phoneNumber, String message) {
        try {
        	Map<String, MessageAttributeValue> smsAttributes =
        	        new HashMap<String, MessageAttributeValue>();
        	
        	smsAttributes.put("AWS.SNS.SMS.SenderID", MessageAttributeValue.builder().stringValue("Halal Homemade").build());
        	
            PublishRequest request = PublishRequest.builder()
                .message(message)
                .phoneNumber(phoneNumber)
                .build();

            PublishResponse result = snsClient.publish(request);
            
            System.out.println(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());

        } catch (SnsException e) {
	        System.err.println(e.awsErrorDetails().errorMessage());
	        System.exit(1);
        }

    }
	
	///------ S3 Service ------///
	public byte[] getObjectBytes (String keyName) {
        try {
            // create a GetObjectRequest instance
            GetObjectRequest objectRequest = GetObjectRequest
                    .builder()
                    .key(keyName)
                    .bucket(bucketName)
                    .build();

            // get the byte[] from this AWS S3 object
            ResponseBytes<GetObjectResponse> objectBytes = s3Client.getObjectAsBytes(objectRequest);
            byte[] data = objectBytes.asByteArray();
            return data;

        } catch (S3Exception e) {
            System.err.println(e.awsErrorDetails().errorMessage());
            System.exit(1);
        }
        return null;
    }

    public String putObject(byte[] data, String objectKey) {
    	try {
            PutObjectResponse response = s3Client.putObject(PutObjectRequest.builder()
            			.bucket(bucketName)
                        .key(objectKey)
                        .build(),
                    RequestBody.fromBytes(data));
            GetUrlRequest urlRequest = GetUrlRequest.builder()
            		.bucket(bucketName)
            		.key(objectKey)
            		.build();
            return s3Client.utilities().getUrl(urlRequest).toString();
        } catch (S3Exception e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return "";
    }
}
