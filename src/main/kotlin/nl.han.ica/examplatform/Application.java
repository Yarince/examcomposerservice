package nl.han.ica.examplatform;


import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.sql.Connection;

@SpringBootApplication
@EnableSwagger2
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

        Connection con = MySQLConnection.INSTANCE.getConnection();
    }
}
