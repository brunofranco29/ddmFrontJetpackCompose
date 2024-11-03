package up.ddm

import kotlin.math.floor

class DefaultHealthStrategy : HealthStrategy {
    override fun calculateHealth(constitution: Int): Int {
        val constitutionBonus = floor((constitution - 10) / 2.0).toInt()
        return 10 + constitutionBonus
    }
}