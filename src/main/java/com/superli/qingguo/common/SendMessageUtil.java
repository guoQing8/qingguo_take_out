package com.superli.qingguo.common;

import java.io.IOException;

import com.superli.qingguo.utils.ValidateCodeUtils;
import org.json.JSONException;

import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;

/**
 * 腾讯云短信发送验证码工具类
 * @author QQ
 */
public class SendMessageUtil {

	// 短信应用SDK AppID
	int appid =1400676073; // 1400开头

	// 短信应用SDK AppKey
	String appkey = "18529778f707817f5fa2073df1474766";

	// 短信模板ID，需要在短信应用中申请
	int templateId = 1395005; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请

	// 签名
	String smsSign = "樱花很美个人网"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`
	
	// 验证码
	String str = "000000";
	
	/**
	 * 发送短信验证码
	 * @param phoneNum 需要发送给哪个手机号码

	 */
	public String sendMessage(String phoneNum) {
		try {
			// 随即6位数赋值验证码
			String strTemp = ValidateCodeUtils.generateValidateCode(4).toString();

			
			// 数组具体的元素个数和模板中变量个数必须一致
			// 比如你模板中需要填写验证码和有效时间，{1}，{2}
			// 那你这里的参数就应该填两个
		    String[] params = {strTemp};
		    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
		    
 			// 签名参数未提供或者为空时，会使用默认签名发送短信
		    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNum, templateId, params, smsSign, "", ""); 
		    
		    System.out.println("result = " + result);

			// 发送成功则给验证码赋值
			if (result.result == 0) {
				str = strTemp;
			}
		} catch (HTTPException e1) {
		    // HTTP响应码错误
		    e1.printStackTrace();
		} catch (JSONException e2) {
		    // json解析错误
		    e2.printStackTrace();
		} catch (IOException e3) {
		    // 网络IO错误
		    e3.printStackTrace();
		}
		return str;
	}
	
}

