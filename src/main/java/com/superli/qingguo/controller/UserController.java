package com.superli.qingguo.controller;

import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import com.superli.qingguo.common.R;
import com.superli.qingguo.entity.User;
import com.superli.qingguo.service.UserService;
import com.superli.qingguo.utils.MessageUtils;
import com.superli.qingguo.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author superli
 * @Description
 * @Date 2022/5/4 22:21
 */
@Slf4j
@RequestMapping("/user")
@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机验证码
     * @param user

     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user){
        //获取手机号
        String phone = user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
            MessageUtils messageUtils = new MessageUtils();
            String code = messageUtils.sendMessage(phone);


            log.info("code={}",code);
            System.out.println(code);
//            //调用xx云提供短信服务API完成短信发送
//
//// 短信应用 SDK AppID
//                int appid = 1400676073; // SDK AppID 以1400开头
//// 短信应用 SDK AppKey
//                String appkey = "18529778f707817f5fa2073df1474766";
//// 需要发送短信的手机号码
//                String[] phoneNumbers = {phone};
//// 短信模板 ID，需要在短信应用中申请
//                int templateId = 1395005; // NOTE: 这里的模板 ID`7839`只是示例，真实的模板 ID 需要在短信控制台中申请
//// 签名
//                String smsSign = "小小表情包"; // NOTE: 签名参数使用的是`签名内容`，而不是`签名ID`。这里的签名"腾讯云"只是示例，真实的签名需要在短信控制台申请
//
//                try {
//                    String[] params = {code};
//                    SmsSingleSender ssender = new SmsSingleSender(appid, appkey);
//                    SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumbers[0],
//                                templateId, params, smsSign, "", "");
//                    System.out.println(result);
//                } catch (HTTPException e) {
//                    // HTTP 响应码错误
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    // JSON 解析错误
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    // 网络 IO 错误
//                    e.printStackTrace();
//                }

            //需要将生成验证码保存在session中
            //session.setAttribute(phone,code);
            //将生成的验证码缓存在redis中,并且设置有效期5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("手机短信验证码发送成功");
        }

        return R.error("手机短信验证码发送失败");


    }

    /**
     * 移动端用户登录
     * @param map

     * @return
     */
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
        //Object code1 = session.getAttribute(phone);
        //从redis缓存中取出验证码
        Object  code1= redisTemplate.opsForValue().get(phone);


        //两者一致登录成功
        if(code1!=null&&code.equals(code1)){
            //登录成功,如果手机号不存在,完成新用户注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);

            }
            session.setAttribute("user",user.getId());
            //登录成功后删除redis中的缓存验证码
            redisTemplate.delete(phone);
            return R.success(user);
        }
        else {
            return R.error("登录失败");
        }

    }
}
