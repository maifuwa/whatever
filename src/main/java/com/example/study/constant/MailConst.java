package com.example.study.constant;

import java.util.List;

public class MailConst {

    public final static String EMAIL_SEND_MQ = "mail";

    public final static String VERIFY_EMAIL_TYPE_REGISTER = "register" ;
    public final static String VERIFY_EMAIL_TYPE_RESET = "reset";
    public final static List<String> VERIFY_EMAIL_TYPE = List.of(VERIFY_EMAIL_TYPE_REGISTER, VERIFY_EMAIL_TYPE_RESET);


    public final static String  REGISTER_EMAIL_SUBJECT = "注册验证 " + AppConst.APP_NAME;
    public final static String RESET_EMAIL_SUBJECT = "重置密码" + AppConst.APP_NAME;
    public final static String LOGIN_EMAIL_SUBJECT = "准许登录" + AppConst.APP_NAME;
    public final static String TEMPLATE_NAME_VERIFY_EMAIL = "verifyEmail";


}
