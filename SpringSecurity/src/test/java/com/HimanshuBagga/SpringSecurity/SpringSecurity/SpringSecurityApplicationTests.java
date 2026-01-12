package com.HimanshuBagga.SpringSecurity.SpringSecurity;

import com.HimanshuBagga.SpringSecurity.SpringSecurity.service.JwtSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.HimanshuBagga.SpringSecurity.SpringSecurity.entities.User;
@SpringBootTest
class SpringSecurityApplicationTests {

    @Autowired
    private JwtSecurity jwtSecurity;

	@Test
	void contextLoads() {
        User user = new User();
        user.setId(4L);
        user.setEmail("bagga@gmail.com");
        user.setPassword("1234");
        String token = jwtSecurity.generateAccessToken(user);
        System.out.println(token);
        Long id = jwtSecurity.getUserIdFromToken(token);
        System.out.println(id);
	}

}
