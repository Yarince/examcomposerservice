package nl.han.ica.examplatform

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class Application


//Run this to start the spring server
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
