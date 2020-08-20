package com.hapi.common.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.sts.model.v20150401.AssumeRoleRequest;
import com.aliyuncs.sts.model.v20150401.AssumeRoleResponse;
import com.hapi.common.exception.RequestFailException;
/**
 * oss 授权工具
 * @author Admin
 *
 */
public class OssStsUtils {
	
	private static String endpoint = "sts.aliyuncs.com";
	private static String accessKeyId = "LTAI4GKCBW15d8so7qd1p2U8";
	private static String accessKeySecret =  "g8L9d8R6mTjgpOYMZqaYCeedmBBVZX";
	private static String roleArn = "acs:ram::1505378023454326:role/ramossststfmm-dev";
	
	 /**
	  * 获取 admin oss的 临时凭证
	  * @param roleSessionName 表示当前零时凭证给谁用，一般是用户名
	  * @return
	  */
    public static OssCredentialsOut getAdminOssCredentials(String roleSessionName) {
    	String policy = "{\r\n" + 
    			"	\"Statement\": [{\r\n" + 
    			"		\"Action\": \"oss:*\",\r\n" + 
    			"		\"Effect\": \"Allow\",\r\n" + 
    			"		\"Resource\": [\"acs:oss:*:*:test-tfmm\", \"acs:oss:*:*:test-tfmm/*\"]\r\n" + 
    			"	}],\r\n" + 
    			"	\"Version\": \"1\"\r\n" + 
    			"}";
    	
    	long expired = 1800L;
    	
    	System.out.println("roleSessionName:" + roleSessionName );
    	
    	
    	AssumeRoleResponse.Credentials credentials = getOssRoleResponse(endpoint, accessKeyId, accessKeySecret, roleArn, roleSessionName, policy, expired).getCredentials();
    	
    	OssCredentialsOut out = new OssCredentialsOut();
    	out.setAccessKeyId( credentials.getAccessKeyId() );
    	out.setAccessKeySecret( credentials.getAccessKeySecret() );
    	out.setSecurityToken( credentials.getSecurityToken() );
    	out.setExpiration(  DateUtils.nextHourNow(DateUtils.parse( credentials.getExpiration() ), 8 )  );
    	
    	return out;
    }
    
    
    private  static AssumeRoleResponse getOssRoleResponse(String endpoint,String accessKeyId,String accessKeySecret,String roleArn,String roleSessionName ,String policy,long expired ) {        
    	
        try {
        	 // 添加endpoint（直接使用STS endpoint，前两个参数留空，无需添加region ID）
            DefaultProfile.addEndpoint("", "", "Sts", endpoint);
            // 构造default profile（参数留空，无需添加region ID）
            IClientProfile profile = DefaultProfile.getProfile("", accessKeyId, accessKeySecret);
            // 用profile构造client
            DefaultAcsClient client = new DefaultAcsClient(profile);
            final AssumeRoleRequest request = new AssumeRoleRequest();
            request.setMethod(MethodType.POST);
            request.setRoleArn(roleArn);
            request.setRoleSessionName(roleSessionName);
            request.setPolicy(policy); // 若policy为空，则用户将获得该角色下所有权限
            request.setDurationSeconds( expired ); // 设置凭证有效时间
            final AssumeRoleResponse response = client.getAcsResponse(request);
            
            return response;
        } catch (ClientException e) {
        	throw new RequestFailException( e.getLocalizedMessage() );
        }
        
    }
    
    

}
