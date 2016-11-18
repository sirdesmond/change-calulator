package sirdesmond.clients

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

/**
 * Created by kofikyei on 11/15/16.
 */



@Component
class DefaultCalculatorClient : CalculatorClient {
    override fun optimalChange(amount: String): Any = ObjectMapper().writeValueAsString(INVALID_AMOUNT)

    companion object{
        const val INVALID_AMOUNT = "invalid amount provided!"
    }
}