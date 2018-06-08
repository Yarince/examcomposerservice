package nl.han.ica.examplatform.persistence.databaseconnection

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("jdbc")
class PropertiesExtract {

    @Value("\${username}")
    val username: String = String()
    @Value("\${jdbc.password}")
    val password: String = String()

}