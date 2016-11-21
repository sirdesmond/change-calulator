package sirdesmond.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import sirdesmond.domain.allUSCoins
import sirdesmond.domain.allUSCoinsValues
import sirdesmond.services.CalculatorService

/**
 * Created by kofikyei on 11/15/16.
 */

@RestController
class USChangeController @Autowired constructor(val calculatorService: CalculatorService) {

    @RequestMapping("/optimalChange/{amount:.+}")
    fun optimalChange(@PathVariable("amount") amount: String) : Any {
        return try{
            calculatorService.optimalChange(amount.toDouble(), allUSCoinsValues).get()
        }catch (e: NumberFormatException){
            INVALID_AMOUNT
        }
    }

    companion object{
        const val INVALID_AMOUNT = "invalid amount provided!"
    }
}