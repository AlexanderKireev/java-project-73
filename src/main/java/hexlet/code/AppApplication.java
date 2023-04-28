package hexlet.code;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@EnableAutoConfiguration
// Добавляем поддержку авторизации через токен в свагер
//@SecurityScheme(name = "javainuseapi", scheme = "bearer",
// type = SecuritySchemeType.HTTP, in = SecuritySchemeIn.HEADER)
public class AppApplication {

    public static void main(String[] args){
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
