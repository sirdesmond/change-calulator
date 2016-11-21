package sirdesmond.service

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.assertj.core.api.Assertions
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.it
import sirdesmond.domain.Coin
import sirdesmond.domain.Response
import sirdesmond.domain.USCoin
import sirdesmond.domain.allUSCoinsValues
import sirdesmond.services.CalculatorService
import sirdesmond.services.toResponse
import kotlin.test.assertFailsWith

/**
 * Created by kofikyei on 11/16/16.
 */

class CalculatorServiceSpec : SubjectSpek<CalculatorService>({
    subject { CalculatorService() }



    it("should not calculate change if amount is less than a penny"){

        assertFailsWith<IllegalArgumentException> {
            subject.optimalChange(-1.00, allUSCoinsValues)
        }
    }

    it("should fail if amount is smaller than smallest coin"){

        assertFailsWith<IllegalArgumentException> {
            subject.dpMakeChange(0.03,setOf(5,10))
        }
    }

    it("should calculate change with single coin") {
      Assertions.assertThat(subject.dpMakeChange(0.25,setOf(1, 5, 10, 25, 100))).containsExactly(25)
    }

    it("should calculate change with multiple coin")  {
        Assertions.assertThat(subject.dpMakeChange(0.15,setOf(1, 5, 10, 25, 100))).containsExactly(5, 10)
    }

    it("should calculate change for some crazy set of coins")  {
        Assertions.assertThat(subject.dpMakeChange(0.23,setOf(1, 4, 15, 20, 50))).containsExactly(4, 4, 15)
    }

    it("should calculate change for some seriously crazy set of coins")  {
        Assertions.assertThat(subject.dpMakeChange(0.63,setOf(1, 5, 10, 21, 25))).containsExactly(21,21,21)
    }

    it("should convert list of coins to response"){
        val coins = listOf<Coin>(
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.HalfDollar, USCoin.Quarter, USCoin.Dime
        )

        val expectedResponse = Response(
                silver_dollar = 12,
                half_dollar = 1,
                quarter = 1,
                dime = 1,
                nickel = 0,
                penny = 0
        )

        assertThat(coins.toResponse().get(), equalTo(expectedResponse))
    }

    it("should return optimal change for whole dollars"){
        val expectedResponse = Response(
            silver_dollar = 10
        )
        assertThat(subject.optimalChange(10.00, allUSCoinsValues).get(), equalTo(expectedResponse))
    }

    it("should return optimal change for really small values"){
        val expectedResponse = Response(
                silver_dollar = 0,
                half_dollar = 1,
                quarter = 1,
                dime = 2,
                nickel = 0,
                penny = 4
        )
        assertThat(subject.optimalChange(0.99, allUSCoinsValues).get(), equalTo(expectedResponse))
    }

    it("should return optimal change for simple fractions"){
        val expectedResponse = Response(
                silver_dollar = 1,
                half_dollar = 1,
                quarter = 0,
                dime = 0,
                nickel = 1,
                penny = 1
        )
        assertThat(subject.optimalChange(1.56, allUSCoinsValues).get(), equalTo(expectedResponse))
    }

    it("should return optimal change for bigger fractions"){
        val expectedResponse = Response(
                silver_dollar = 12,
                half_dollar = 1,
                quarter = 1,
                dime = 1,
                nickel = 0,
                penny = 0
        )
        assertThat(subject.optimalChange(12.85, allUSCoinsValues).get(), equalTo(expectedResponse))
    }

})