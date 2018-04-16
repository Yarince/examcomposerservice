package nl.han.main.service.greetings

import nl.han.main.business.greetings.model.Greeting
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong

@RestController
private class GreetingController {

    val counter = AtomicLong()

    @GetMapping("/greeting")
    fun greeting(@RequestParam(value = "name", defaultValue = "World") name: String) =
            Greeting(counter.incrementAndGet(), "Hello, $name")
    // localhost:8080/greeting?name=TestName

    @GetMapping("/greeting/{username}")
    fun greetPerson(@PathVariable username: String): Any =
            Greeting(counter.incrementAndGet(),"Hello, $username")
    // localhost:8080/greeting/TestName


}