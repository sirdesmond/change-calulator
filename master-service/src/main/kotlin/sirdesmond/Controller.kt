package sirdesmond

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import sirdesmond.domain.Response

/**
 * Created by kofikyei on 11/15/16.
 */
@RestController
class Controller {

    @RequestMapping("/change",method = arrayOf(RequestMethod.GET))
    fun calculate(@RequestParam("amount") amount: Double) : Response  {
        return Response()
    }
}