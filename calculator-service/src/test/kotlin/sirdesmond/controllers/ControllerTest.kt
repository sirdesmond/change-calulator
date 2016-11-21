package sirdesmond.controllers

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.junit.Test
import org.mockito.Mockito
import sirdesmond.domain.Response
import sirdesmond.services.CalculatorService
import java.util.*

/**
 * Created by kofikyei on 11/18/16.
 */
class ControllerTest{
    val calcService: CalculatorService = Mockito.mock(CalculatorService::class.java)

    val controller = USChangeController(calcService)

    @Test
    fun `should return invalid amount message if amount is invalid`(){
      val expectedResponse = "invalid amount provided!"
       assertThat(controller.optimalChange("invalid") as String,
               equalTo(expectedResponse))
    }

    @Test
    fun `should return valid response when amount is valid`(){
        val expectedResponse = Response()
        Mockito.`when`(calcService.optimalChange(Mockito.anyDouble(),
                Mockito.anySetOf(Int::class.java))).thenReturn(Optional.of(expectedResponse))
        assertThat(controller.optimalChange("12") as Response,
                equalTo(expectedResponse))
    }
}