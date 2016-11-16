package sirdesmond

import com.jayway.restassured.specification.RequestSpecification

/**
 * Created by kofikyei on 11/15/16.
 */


fun RequestSpecification.When(): RequestSpecification {
    return this.`when`()
}

