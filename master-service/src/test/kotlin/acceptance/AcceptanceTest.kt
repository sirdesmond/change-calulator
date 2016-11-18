package acceptance

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.jayway.restassured.RestAssured
import com.jayway.restassured.internal.mapping.Jackson2Mapper
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.context.embedded.LocalServerPort
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import sirdesmond.Application
import sirdesmond.When
import sirdesmond.domain.Response

/**
 * Created by kofikyei on 11/17/16.
 */

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class), webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("system")
class AcceptanceTest {

    @Rule @JvmField
    val rule = WireMockRule(8089)

    @LocalServerPort
    var port: Int= 0

    @Before
    fun setup() { RestAssured.port = port }


    @Test
    fun `should return optimal change for valid requests`() {

        val expectedResponse = Response(
                silver_dollar = 12,
                half_dollar = 1,
                quarter = 1,
                dime = 1,
                nickel = 0,
                penny = 0
        )
        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/optimalChange/12.85"))
                .willReturn(WireMock.aResponse()
                        .withHeader("Content-Type","application/json")
                        .withBody(ObjectMapper().writeValueAsString(expectedResponse)))
        )

        RestAssured.given()
                .When()
                .get("/change/12.85")
                .then()
                .body("silver_dollar", Matchers.equalTo(12))
                .body("half_dollar", Matchers.equalTo(1))
                .body("quarter", Matchers.equalTo(1))
                .body("dime", Matchers.equalTo(1))
                .body("nickel", Matchers.equalTo(0))
                .body("penny", Matchers.equalTo(0))
                .statusCode(200)
    }

    @Test
    fun `should return invalid amount error if amount is not valid`() {
        val expectedResponse = mapOf("message" to INVALID_AMOUNT)


        WireMock.stubFor(WireMock.get(WireMock.urlEqualTo("/optimalChange/invalid_amount"))
                .willReturn(WireMock.aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(ObjectMapper().writeValueAsString(expectedResponse)))
        )

        RestAssured.given()
                .When()
                .get("/change/invalid_amount")
                .then()
                .body("message", Matchers.equalTo(INVALID_AMOUNT))
                .statusCode(200)
    }

    companion object{
        const val INVALID_AMOUNT = "invalid amount provided!"
    }

}