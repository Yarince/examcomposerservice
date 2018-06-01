package nl.han.ica.examplatform.persistence.plugin

import nl.han.ica.examplatform.models.plugin.Plugin

interface IPluginDAO {
    /**
     * This function gets a list of all plugins from the database.
     *
     * @return [ArrayList]<[Plugin]> List of [Plugin]s
     */
    fun getAllPlugins(): ArrayList<Plugin>
}