package com.patrick.disruptoromssettlement.api;

import com.patrick.disruptoromssettlement.bean.res.Account;
import com.patrick.disruptoromssettlement.bean.res.CaptchaResponse;
import com.patrick.disruptoromssettlement.bean.res.CounterRes;
import com.patrick.disruptoromssettlement.cache.CacheType;
import com.patrick.disruptoromssettlement.cache.RedisStringCache;
import com.patrick.disruptoromssettlement.service.AccountService;
import com.patrick.disruptoromssettlement.util.Captcha;
import com.patrick.disruptoromssettlement.uuid.GudyUuid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.patrick.disruptoromssettlement.bean.res.CounterRes.FAIL;
import static com.patrick.disruptoromssettlement.bean.res.CounterRes.RELOGIN;

@RestController
@RequestMapping("/login")
@Log4j2
public class LoginController {

    @Autowired
    private AccountService accountService;
    @RequestMapping("/captcha")
    public CounterRes captcha() throws Exception{
        //生成验证码 120 *４０图片
        Captcha captcha = new Captcha(120, 40, 4, 10);

        //将验证码ID入缓存
        String uuid = String.valueOf(GudyUuid.getInstance().getUUID());
        RedisStringCache.cache(uuid, captcha.getCode(), CacheType.CAPTCHA);

        //使用base64编码图片，并返回给UI {uuid:, base64:}
        CaptchaResponse response = new CaptchaResponse(uuid, captcha.getBase64ByteStr());
        return new CounterRes(response);

    }

    @RequestMapping("/userlogin")
    public CounterRes login(@RequestParam long uid,
                            @RequestParam String password,
                            @RequestParam String captcha,
                            @RequestParam String captchaId) throws Exception{

        Account account = accountService.login(uid, password,
                captcha, captchaId);

        if(account == null){
            return new CounterRes(FAIL,
                    "用户名密码/验证码错误，登录失败",null);
        }else {
            return new CounterRes(account);
        }
    }

    @RequestMapping("/loginfail")
    public CounterRes loginFail(){
        return new CounterRes(RELOGIN,"请重新登陆",null);
    }
}
