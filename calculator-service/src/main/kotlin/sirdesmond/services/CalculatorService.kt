package sirdesmond.services

import org.springframework.stereotype.Component
import sirdesmond.domain.Coin
import sirdesmond.domain.Response
import sirdesmond.domain.USCoin

/**
 * Created by kofikyei on 11/16/16.
 */

@Component
open class CalculatorService {

    open fun optimalChange(amount: Double, coins: Set<Coin>): Response =
         makeChange(amount, coins).toResponse()

    private fun makeChange(amount: Double, coins: Set<Coin>): List<Coin> {
        fun calculate(amount: Int, coins: Set<Coin>, index: Int, memo: MutableMap<String, Int>): Int {

            if (amount == 0) {
                return 0
            }

            val key = "$amount-$index"
            if (memo.containsKey(key)) {
                return memo[key] as Int
            }

            var min = Integer.MAX_VALUE

            coins.mapIndexed { index, coin ->
                if (coin.value <= amount) {
                    val remaining = amount - coin.value

                    val result = calculate(remaining, coins, index, memo)
                    if (result < min) {
                        min = result
                    }

                }
            }

            min = if (min == Integer.MAX_VALUE) min else min + 1

            memo.put(key, min)
            return min
        }

        val minimum = calculate(amount.times(100).toInt(), coins, 0, mutableMapOf())

        return greedyFindCoins(minimum, amount, coins).toList()
    }

    fun greedyFindCoins(min: Int, amount: Double, coins: Set<Coin>): MutableList<Coin> {


        fun doFind(min: Int, amount: Int, coins: Set<Coin>, usedCoins: MutableList<Coin>): MutableList<Coin> {
            val newCoins = coins.filter { it.value <= amount }.sortedBy(Coin::value).reversed()

            if (min == 0) return usedCoins


            usedCoins.add(newCoins.first())
            doFind(min - 1, amount - newCoins.first().value, newCoins.toHashSet(), usedCoins)


            return usedCoins
        }

        return doFind(min, amount.times(100).toInt(), coins, mutableListOf())

    }

}

fun List<Coin>.toResponse(): Response {
    val groups: Map<String, List<Coin>> = this.groupBy { it.javaClass.simpleName }

    return Response(
            silver_dollar = groups.getOrElse(USCoin.SilverDollar.javaClass.simpleName, { emptyList<Coin>() }).size,
            half_dollar = groups.getOrElse(USCoin.HalfDollar.javaClass.simpleName, { emptyList<Coin>() }).size,
            quarter = groups.getOrElse(USCoin.Quarter.javaClass.simpleName, { emptyList<Coin>() }).size,
            dime = groups.getOrElse(USCoin.Dime.javaClass.simpleName, { emptyList<Coin>() }).size,
            nickel = groups.getOrElse(USCoin.Nickel.javaClass.simpleName, { emptyList<Coin>() }).size,
            penny = groups.getOrElse(USCoin.Penny.javaClass.simpleName, { emptyList<Coin>() }).size
    )
}