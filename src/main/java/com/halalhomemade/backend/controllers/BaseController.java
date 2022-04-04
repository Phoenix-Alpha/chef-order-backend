package com.halalhomemade.backend.controllers;

import com.halalhomemade.backend.services.UserService;
import com.halalhomemade.backend.services.qrcode.FileObject;
import com.halalhomemade.backend.utils.UrlUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public abstract class BaseController {

	@Autowired protected UserService userService;

	protected boolean isSuperAdmin() {

		return SecurityContextHolder.getContext()
							        .getAuthentication()
							        .getAuthorities()
							        .contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN"));
	}

	protected boolean isChef() {

		return SecurityContextHolder.getContext()
									.getAuthentication()
									.getAuthorities()
									.stream()
							        .filter(authority -> authority.equals(new SimpleGrantedAuthority("ROLE_CHEF")))
							        .findFirst()
							        .isPresent();
	}
  
	protected Long getLoggedInUserId() throws Exception {
		return userService.getUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
	}

	protected boolean isAPIAllowedToAccess(Long userId) throws Exception {
		return getLoggedInUserId().equals(userId);
	}
	
//	protected String getCognitoUsername() {
//		return SecurityContextHolder.getContext().getAuthentication().getName();
//	}
	
	protected String getEmail() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}
	
	protected ResponseEntity<StreamingResponseBody> prepareStreamingResponse(FileObject fileObject) {
	    StreamingResponseBody responseBody =
	        outputStream -> {
	          int numberOfBytesToWrite;
	          byte[] data = new byte[1024];
	          while ((numberOfBytesToWrite = fileObject.getFileStream().read(data, 0, data.length))
	              != -1) {
	            outputStream.write(data, 0, numberOfBytesToWrite);
	          }
	          fileObject.getFileStream().close();
	        };

	        
	        Map<String, List<String>> headerMap = Stream.of(new Object[][] {
    			{ 
    				HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, 
    				Arrays.asList(HttpHeaders.CONTENT_DISPOSITION) 
    			}, 
    			{ 
    				HttpHeaders.CONTENT_DISPOSITION,
    				Arrays.asList(StringUtils.join("attachment; filename=\"", UrlUtils.encodeFilename(fileObject.getFileName()), "\"")) 
    			},
    		}).collect(Collectors.toMap(data -> (String) data[0], data -> (List<String>) data[1]));
	        
	        return ResponseEntity.ok()
	        		.contentType(MediaType.APPLICATION_OCTET_STREAM)
	        		.headers(new HttpHeaders(CollectionUtils.toMultiValueMap(headerMap)))
	        		.body(responseBody);
	             
	  }
	
}
