package sirdesmond

import com.jayway.restassured.RestAssured
import com.jayway.restassured.RestAssured.given
import com.jayway.restassured.http.ContentType
import com.palantir.docker.compose.DockerComposeRule
import com.palantir.docker.compose.connection.DockerPort
import com.palantir.docker.compose.connection.waiting.HealthChecks.toRespondOverHttp
import org.hamcrest.Matchers.*
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test


/**
 * Created by kofikyei on 11/15/16.
 */


class IntegrationSpec {

     val INTERNAL_PORT: Int = 8080
     val TO_EXTERNAL_URI : (DockerPort) -> String =
             { port  -> port.inFormat("http://\$HOST:\$EXTERNAL_PORT") }



    @Rule @JvmField val docker = DockerComposeRule.builder()
            .file("../docker-compose.yml")
            .waitingForService("calculator-service", toRespondOverHttp(INTERNAL_PORT, TO_EXTERNAL_URI))
            .waitingForService("master-service", toRespondOverHttp(INTERNAL_PORT, TO_EXTERNAL_URI))
            .saveLogsTo("build/docker-logs")
            .build()

    @Before
    fun setup() {
        val masterServiceContainer = docker.containers().container("master-service")
        RestAssured.baseURI = TO_EXTERNAL_URI(masterServiceContainer.port(INTERNAL_PORT))
    }

    @Test
    fun `should return optimal change`() {

        given()
                .When()
                .get("/change/12.85")
                .then()
                .body("silver_dollar", equalTo(12))
                .body("half_dollar", equalTo(1))
                .body("quarter", equalTo(1))
                .body("dime", equalTo(1))
                .body("nickel", equalTo(0))
                .body("penny", equalTo(0))
                .statusCode(200)
    }


    @Test
    fun `should return invalid error message when amount is  invalid`() {
        given()
                .When()
                .contentType(ContentType.TEXT)
                .get("/change/invalid")
                .then()
                .body(containsString("invalid amount provided!"))
    }

    @Test
    fun `should fallback to default client when calculator service is down`() {
        docker.dockerCompose().container("calculator-service").stop()

        given()
                .When()
                .contentType(ContentType.JSON)
                .get("/change/12.85")
                .then()
                .body(containsString("invalid amount provided!"))
    }


}