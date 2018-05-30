package nl.han.ica.examplatform.controllers.plugin

import io.swagger.annotations.*
import nl.han.ica.examplatform.business.plugin.PluginService
import nl.han.ica.examplatform.models.plugin.Plugin
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for HTTP interaction with [Plugin]s.
 *
 * @param pluginService [PluginService] The PluginService
 */
@RestController()
@RequestMapping("/plugins")
@Api("question", description = "Creating, updating and deleting plugins")
class PluginController(private val pluginService: PluginService) {

    /**
     * HTTP REST function to get a list of all [Plugin]s from the database.
     */
    @GetMapping()
    @ApiOperation(value = "Get all plugins", notes = "Fetches all plugins from database")
    @ApiResponses(
            ApiResponse(code = 200, message = "Fetched"),
            ApiResponse(code = 500, message = "Internal server error")
    )
    fun getPlugins(): ResponseEntity<ArrayList<Plugin>> = pluginService.getAllPlugins()
}
