package com.hapi.common.util;

import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class OssCredentialsOut {

	@ApiModelProperty("零时token")
	private String securityToken;

	@ApiModelProperty("零时accessKeySecret")
	private String accessKeySecret;

	@ApiModelProperty("零时accessKeyId")
	private String accessKeyId;

	@ApiModelProperty("过期时间")
	private Date expiration;
	
	
}
