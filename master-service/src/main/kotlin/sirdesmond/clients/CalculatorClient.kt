package sirdesmond.clients

import org.springframework.cloud.netflix.feign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Created by kofikyei on 11/15/16.
 */
@FeignClient("calculator",fallback = DefaultCalculatorClient::class)
interface CalculatorClient {
    @RequestMapping(method = arrayOf(RequestMethod.GET), value = "/optimalChange/{amount}")
    fun optimalChange(@PathVariable("amount") amount: String): Any
}
