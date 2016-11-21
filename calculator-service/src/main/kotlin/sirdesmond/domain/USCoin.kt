package sirdesmond.domain

import sirdesmond.domain.Coin


/**
 * Created by kofikyei on 11/16/16.
 */

sealed class USCoin(value: Int) : Coin(value) {
    object SilverDollar  : USCoin(100)
    object HalfDollar  : USCoin(50)
    object Quarter  : USCoin(25)
    object Dime  : USCoin(10)
    object Nickel  : USCoin(5)
    object Penny  : USCoin(1)

}

val allUSCoins = setOf<Coin>(USCoin.SilverDollar,
        USCoin.HalfDollar, USCoin.Quarter, USCoin.Dime,
        USCoin.Nickel, USCoin.Penny)

val allUSCoinsValues = setOf<Int>(USCoin.SilverDollar.value,
        USCoin.HalfDollar.value, USCoin.Quarter.value, USCoin.Dime.value,
        USCoin.Nickel.value, USCoin.Penny.value)