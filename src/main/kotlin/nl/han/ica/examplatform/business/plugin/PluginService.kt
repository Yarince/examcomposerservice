package nl.han.ica.examplatform.business.plugin

import nl.han.ica.examplatform.models.plugin.Plugin
import nl.han.ica.examplatform.persistence.plugin.PluginDAO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

/**
 * Exam service for handling requests related to the [Plugin] model.
 *
 * @param pluginDAO [PluginDAO] The PluginDAO
 */
@Service
class PluginService(private val pluginDAO: PluginDAO) {

    /**
     * Get all Plugins from the database.
     *
     * @return [ResponseEntity]<[ArrayList]<[Plugin]>> All plugins currently in the database.
     */
    fun getAllPlugins(): ResponseEntity<ArrayList<Plugin>> = ResponseEntity(pluginDAO.getAllPlugins(), HttpStatus.OK)

}
