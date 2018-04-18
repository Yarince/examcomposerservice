package nl.han.ica.examplatform

import nl.han.ica.examplatform.persistence.databaseconnection.MySQLConnection
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2



@SpringBootApplication
@EnableSwagger2
class Application


//Run this to start the spring server
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
    val msql = MySQLConnection()
    println(msql.getConnection())
}
