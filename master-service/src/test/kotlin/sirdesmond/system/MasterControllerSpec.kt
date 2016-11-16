package sirdesmond.system

import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured.given
import org.hamcrest.Matchers.equalTo
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import sirdesmond.When


/**
 * Created by kofikyei on 11/15/16.
 */


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MasterControllerSpec {

    @LocalServerPort var port: Int = 0

    @Before
    fun setup() {
        RestAssured.port = port
    }

    @Test fun `should return optimal change`() {

        given()
                .param("amount", "12.85")
                .When()
                .get("/change")
                .then()
                .body("silver_dollar", equalTo(2))
                .body("half_dollar", equalTo(1))
                .body("quarter", equalTo(1))
                .body("dime", equalTo(1))
                .body("nickel", equalTo(0))
                .body("penny", equalTo(0))
                .statusCode(200)
    }
}