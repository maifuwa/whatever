package com.example.study;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class StudyApplicationTests {

	@Autowired
	StringRedisTemplate template;

	@Test
	void testString() {
		template.opsForValue().set("name", "张三");
		System.out.println(template.opsForValue().get("name"));
	}

	@Test
	void setString() {
		template.opsForValue().set("name", "张三", 60, TimeUnit.SECONDS);
		template.opsForValue().get("name");
	}

	@Test
	void setHash() {

	}

}
