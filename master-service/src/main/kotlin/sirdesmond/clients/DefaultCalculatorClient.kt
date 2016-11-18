package sirdesmond.clients

import org.springframework.stereotype.Component
import sirdesmond.domain.Response

/**
 * Created by kofikyei on 11/15/16.
 * Can be used if Hystrix fallback is enabled
 */



@Component
class DefaultCalculatorClient : CalculatorClient {
    override fun optimalChange(amount: String): Any = Response()
}