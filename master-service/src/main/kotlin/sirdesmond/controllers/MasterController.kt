package sirdesmond.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController
import sirdesmond.clients.CalculatorClient

/**
 * Created by kofikyei on 11/15/16.
 */
@RestController
class MasterController @Autowired constructor(val calculatorClient: CalculatorClient) {

    @RequestMapping("/change/{amount:.+}", method = arrayOf(RequestMethod.GET))
    fun optimalChange(@PathVariable("amount") amount: String): Any =
             calculatorClient.optimalChange(amount)
}