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

    open fun optimalChange(amount: Double, coins: Set<Int>): Optional<Response> =
          Optional.of(dpMakeChange(amount, coins).toResponse())


    fun dpMakeChange(amount: Double, coins: Set<Int>) : List<Int>{
        require(coins.size > 0, { "Coins cannot be empty" })

        val sum: Int = amount.times(100).toInt()
        if (sum == 0) return emptyList()
        require(coins.any { it <= sum }, { "target too low" })

        val usedCoins = mutableMapOf<Int, List<Int>?>(0 to emptyList<Int>())
        for (target in 1..sum) {
            usedCoins[target] = coins.filter { it <= target }
                    .map { coin -> usedCoins[target - coin]?.let { it + listOf(coin) } ?: null }
                    .filterNotNull()
                    .minBy { it.size }
        }
        return (usedCoins[sum] ?: emptyList()).sorted()
    }

    //greedy solution only works for coins like US denominations
    //dp works now...retiring greedy

    fun greedilyFindCoins(amount: Double, coins: Set<Int>): MutableList<Int> {
        fun doFind(amount: Int, coins: Set<Int>, usedCoins: MutableList<Int>): MutableList<Int> {
            if (amount == 0) return usedCoins
            val newCoins = coins.filter { it <= amount }.sorted().reversed()
            usedCoins.add(newCoins.first())
            doFind(amount - newCoins.first(), newCoins.toHashSet(), usedCoins)
            return usedCoins
        }

        return doFind(amount.times(100).toInt(), coins, mutableListOf())
    }
}

fun List<Int>.toResponse():Response{
    val groups: Map<Int, List<Int>> = this.groupBy { it}

    return Response(
            silver_dollar = groups.getOrElse(100,{ listOf()}).size ,
            half_dollar = groups.getOrElse(50,{ listOf()}).size,
            quarter = groups.getOrElse(25,{ listOf()}).size,
            dime = groups.getOrElse(10,{ listOf()}).size,
            nickel = groups.getOrElse(5,{ listOf()}).size,
            penny = groups.getOrElse(1,{ listOf()}).size
    )
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