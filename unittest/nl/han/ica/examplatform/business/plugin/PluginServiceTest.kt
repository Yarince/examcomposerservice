package nl.han.ica.examplatform.business.plugin

import com.nhaarman.mockito_kotlin.doReturn
import nl.han.ica.examplatform.models.plugin.Plugin
import nl.han.ica.examplatform.persistence.plugin.PluginDAO
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class PluginServiceTest {

    @InjectMocks
    private lateinit var pluginService: PluginService

    @Mock
    private lateinit var pluginDAO: PluginDAO

    @Test
    fun testGetAllPlugins(){
        val pluginList = arrayListOf(
                Plugin(1, "Plugin1", "v2", "Description1"),
                Plugin(2, "Plugin2", "v2", "Description2")
        )
        val expected: ResponseEntity<ArrayList<Plugin>> = ResponseEntity(pluginList, HttpStatus.OK)


        doReturn(pluginList).`when`(pluginDAO).getAllPlugins()
        val result: ResponseEntity<ArrayList<Plugin>> = pluginService.getAllPlugins()

        assertEquals(expected, result)
    }


}