package sirdesmond.service

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.jetbrains.spek.api.SubjectSpek
import org.jetbrains.spek.api.dsl.it
import sirdesmond.domain.Coin
import sirdesmond.domain.Response
import sirdesmond.domain.USCoin
import sirdesmond.domain.allUSCoins
import sirdesmond.services.CalculatorService
import sirdesmond.services.toResponse

/**
 * Created by kofikyei on 11/16/16.
 */

class CalculatorServiceSpec : SubjectSpek<CalculatorService>({
    subject { CalculatorService() }



    it("should not calculate change if amount is less than a penny"){

        assertThat(subject.optimalChange(-1.00, allUSCoins).isPresent, equalTo(false))
    }

    it("should be able to find coins for optimal solution"){
        val expectedResponse = mutableListOf<Coin>(
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.SilverDollar, USCoin.SilverDollar, USCoin.SilverDollar,
                USCoin.HalfDollar, USCoin.Quarter, USCoin.Dime
        )
        assertThat(subject.greedilyFindCoins(12.85, allUSCoins), equalTo(expectedResponse))
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
        assertThat(subject.optimalChange(10.00, allUSCoins).get(), equalTo(expectedResponse))
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
        assertThat(subject.optimalChange(0.99, allUSCoins).get(), equalTo(expectedResponse))
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
        assertThat(subject.optimalChange(1.56, allUSCoins).get(), equalTo(expectedResponse))
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
        assertThat(subject.optimalChange(12.85, allUSCoins).get(), equalTo(expectedResponse))
    }

})