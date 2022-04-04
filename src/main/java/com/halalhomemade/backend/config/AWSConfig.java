package com.halalhomemade.backend.config;
 
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
 
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
 
@Configuration
public class AWSConfig {
 
    // Access key id will be read from the application.properties file during the application intialization.
    @Value("${aws.access_key_id}")
    private String accessKeyId;
    // Secret access key will be read from the application.properties file during the application intialization.
    @Value("${aws.secret_access_key}")
    private String secretAccessKey;
    // Region will be read from the application.properties file  during the application intialization.
    @Value("${aws.region}")
    private String region;
 
    @Bean
    public S3Client getAWSS3Cient() {
        final AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
        // Get AmazonS3 client and return the s3Client object.
        return S3Client.builder()
        		.region(Region.of(region))
        		.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
    
    @Bean
    public CognitoIdentityProviderClient getAWSCognitoClient() {
    	final AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    	return CognitoIdentityProviderClient.builder()
    			.region(Region.of(region))
    			.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    }
    
    @Bean
    public SnsClient getAWSSnsClient() {
    	final AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    	return SnsClient.builder()
    			.region(Region.of(region))
    			.credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();
    } 
}