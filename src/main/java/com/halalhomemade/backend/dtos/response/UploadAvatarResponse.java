package com.halalhomemade.backend.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public final class UploadAvatarResponse {
	private Boolean success;
	private String url;
}