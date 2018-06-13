package nl.han.ica.examplatform;


import nl.han.ica.examplatform.models.question.Question;
import nl.han.ica.examplatform.persistence.question.QuestionDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

@SpringBootApplication
@EnableSwagger2
public class Application implements WebMvcConfigurer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        QuestionDAO q = new QuestionDAO();
        ArrayList<String> a = new ArrayList<>();
        a.add("nee");
        a.add("ja");
        q.insertQuestion(new Question(18, null, "OpenQuestion", "MultipleChoice", "toetsie", 5 , 2, "Tentamen", "MultipleChoice", "1.0", "1.0", a, null), null);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET", "POST", "DELETE", "PUT");
            }
        };
    }

}
