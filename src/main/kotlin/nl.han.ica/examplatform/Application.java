package nl.han.ica.examplatform;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableAutoConfiguration
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        System.out.println("Hello world2");
        SpringApplication.run(Application.class, args);
    }
}
