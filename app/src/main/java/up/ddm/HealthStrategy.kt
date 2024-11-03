package up.ddm

interface HealthStrategy {
    fun calculateHealth(constitution: Int): Int
}