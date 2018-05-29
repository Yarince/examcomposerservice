package nl.han.ica.examplatform.business.plugin

import nl.han.ica.examplatform.models.plugin.Plugin
import nl.han.ica.examplatform.persistence.plugin.PluginDAO
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class PluginService(private val pluginDAO: PluginDAO) {
    fun getAllPlugins(): ResponseEntity<Plugin> = pluginDAO.getAllPlugins()

}
