package sirdesmond.services

import org.springframework.stereotype.Component
import sirdesmond.domain.Coin
import sirdesmond.domain.Response
import sirdesmond.domain.USCoin
import java.util.*

/**
 * Created by kofikyei on 11/16/16.
 */

@Component
open class CalculatorService {

    open fun optimalChange(amount: Double, coins: Set<Coin>): Optional<Response> =
          if(amount<USCoin.Penny.value / 100) Optional.empty()
             else greedilyFindCoins(amount, coins).toResponse()

    /*this Dynamic Programming function can find minimum number of coins for all denominations
    * couldn't get it to fetch actual coins used...deferring to greedy solution
    * */
    private fun makeChange(amount: Double, coins: Set<Coin>): List<Coin> {
        fun universalfindNumberOfCoins(amount: Int, coins: Set<Coin>, index: Int, memo: MutableMap<String, Int>): Int {
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
                    val result = universalfindNumberOfCoins(remaining, coins, index, memo)
                    if (result < min) {
                        min = result
                    }
                }
            }
            min = if (min == Integer.MAX_VALUE) min else min + 1
            memo.put(key, min)
            return min
        }


        val optimalNumberOfCoins: Int = universalfindNumberOfCoins(amount.times(100).toInt(), coins, 0, mutableMapOf())
        return greedilyFindCoins(amount, coins).toList()
    }


    /*for time constraints, I used a greedy approach to find actual coins which will work for
    * denominations like the US Coin
    * */
    fun greedilyFindCoins(amount: Double, coins: Set<Coin>): MutableList<Coin> {
        fun doFind(amount: Int, coins: Set<Coin>, usedCoins: MutableList<Coin>): MutableList<Coin> {
            if (amount == 0) return usedCoins
            val newCoins = coins.filter { it.value <= amount }.sortedBy(Coin::value).reversed()
            usedCoins.add(newCoins.first())
            doFind(amount - newCoins.first().value, newCoins.toHashSet(), usedCoins)
            return usedCoins
        }

        return doFind(amount.times(100).toInt(), coins, mutableListOf())
    }
}

fun List<Coin>.toResponse(): Optional<Response> {
    val groups: Map<String, List<Coin>> = this.groupBy { it.javaClass.simpleName }

    return Optional.of(Response(
            silver_dollar = groups.getOrElse(USCoin.SilverDollar.javaClass.simpleName, { emptyList<Coin>() }).size,
            half_dollar = groups.getOrElse(USCoin.HalfDollar.javaClass.simpleName, { emptyList<Coin>() }).size,
            quarter = groups.getOrElse(USCoin.Quarter.javaClass.simpleName, { emptyList<Coin>() }).size,
            dime = groups.getOrElse(USCoin.Dime.javaClass.simpleName, { emptyList<Coin>() }).size,
            nickel = groups.getOrElse(USCoin.Nickel.javaClass.simpleName, { emptyList<Coin>() }).size,
            penny = groups.getOrElse(USCoin.Penny.javaClass.simpleName, { emptyList<Coin>() }).size
    ))
}