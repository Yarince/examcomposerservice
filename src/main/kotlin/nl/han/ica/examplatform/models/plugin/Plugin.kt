package nl.han.ica.examplatform.models.plugin

import io.swagger.annotations.ApiModelProperty

/**
 * Represents a plugin from the database.
 */
data class Plugin(
        @ApiModelProperty(notes = "The id of the plugin")
        val pluginId: Int,
        @ApiModelProperty(notes = "The name of the plugin")
        val pluginName: String,
        @ApiModelProperty(notes = "The version of the plugin")
        val pluginVersion: String,
        @ApiModelProperty(notes = "The description of the plugin")
        val pluginDescription: String
)
