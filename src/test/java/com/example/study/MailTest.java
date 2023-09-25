package com.example.study;

import com.example.study.server.MailServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
public class MailTest {

    @Autowired
    MailServer server;

    @Test
    void testSendMail() {
        server.sendVerifyEmail("1938311440@qq.com", Map.of());
    }

}
