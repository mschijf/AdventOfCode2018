package adventofcode2018.december24

import adventofcode2018.PuzzleSolverAbstract
import com.tool.mylambdas.splitByCondition
import kotlin.math.min

fun main() {
    PuzzleSolver(test = true).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {

    private fun createArmy(armyName: String): List<Group> {
        return inputLines
            .splitByCondition { it.isEmpty() }
            .first { it.first() == "$armyName:" }.drop(1)
            .mapIndexed { index, rawInput -> Group.of("$armyName group " + (index+1), rawInput) }
    }

    override fun resultPartOne(): Any {
        val infectionArmy = createArmy("Infection")
        val immuneArmy = createArmy("Immune System")

        combat(infectionArmy, immuneArmy)
        return "(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }})"
    }

    override fun resultPartTwo(): Any {
        val infectionArmy = createArmy("Infection")
        val immuneArmy = createArmy("Immune System")

        immuneArmy.forEach { it.unit.boostAttackDamage(1500) }
        combat(infectionArmy, immuneArmy)

        return "(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }})"
    }

    private fun combat(infectionArmy: List<Group>, immuneArmy: List<Group>) {
        println("(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }})")
        while (infectionArmy.any{it.alive()} && immuneArmy.any{it.alive()}) {
            val selection = targetSelectionPhase(infectionArmy.filter { it.alive() }, immuneArmy.filter { it.alive() })
            selection.attackingPhase()
            println("(infection, immunity) = (${infectionArmy.sumOf { it.unitCount }}, ${immuneArmy.sumOf { it.unitCount }})")
        }
    }

    private fun targetSelectionPhase(infectionArmy: List<Group>, immuneArmy: List<Group>): Map<Group, Group> {
        val x = immuneArmy.select(infectionArmy)
        val y = infectionArmy.select(immuneArmy)
        return x+y
    }

    private fun List<Group>.select(defendingGroupList: List<Group>): Map<Group, Group> {
        val chosen = mutableSetOf<Group>()
        val result = this
            .sortedByDescending { 10000L * it.effectivePower() + it.unit.initiative }
            .associateWith { attacker ->
                defendingGroupList
                    .filterNot { it in chosen }
                    .filter{it.calculatedDamage(attacker.unit.attackType, attacker.effectivePower()) > 0}
                    .maxByOrNull { 1_000_000L*it.calculatedDamage(attacker.unit.attackType, attacker.effectivePower()) + 100L*it.effectivePower() + it.unit.initiative }
                    .also { if (it != null) chosen += it }
            }
        return result.mapNotNull { (key, value) -> value?.let { key to it } }.toMap()
    }

    private fun Map<Group, Group>.attackingPhase() {
        this.keys.sortedByDescending { it.unit.initiative }.forEach {attacker ->
            val defender = this[attacker]!!
            defender.attackedBy(attacker.unit.attackType, attacker.effectivePower())
        }
    }

}


class Group(
    val name: String,
    var unitCount: Int,
    val unit: Unit
) {

    fun alive(): Boolean =
        unitCount > 0

    fun effectivePower(): Int =
        unitCount * unit.attackDamage

    fun attackedBy(attackType: AttackType, attackPower: Int) {
        val damage = calculatedDamage(attackType, attackPower)
        val unitsLost = min(unitCount, damage/unit.hitPoints)
//        println("    $name loses $unitsLost units")
        unitCount -= unitsLost
    }

    fun calculatedDamage(attackType: AttackType, attackPower: Int) =
        when (attackType) {
            in unit.immuneList -> 0
            in unit.weaknessList -> 2*attackPower
            else -> attackPower
        }

    companion object {
        fun of(name: String, rawInput: String): Group {
            val unitCount = rawInput.substringBefore(" unit")
            val unit = Unit.of(rawInput)
            return Group(name, unitCount.toInt(), unit)
        }
    }

    override fun toString(): String {
        return "$name ${if (!alive()) "+" else ""}"
    }

    fun print() {
        println("$name ${if (!alive()) "+" else ""}: unit count: $unitCount (Effective Power: ${effectivePower()}) ==> $unit")
    }
}

class Unit(
    val hitPoints: Int,
    val attackType: AttackType,
    var attackDamage: Int,
    val initiative: Int,
    val immuneList: List<AttackType>,
    val weaknessList: List<AttackType>,
) {

    companion object {
        fun of(rawInput: String): Unit {
            val hitPoints = rawInput.substringAfter(" each with ").substringBefore(" hit points ")
            val attackDamage = rawInput.substringAfter(" that does ").substringBefore(" ")
            val attackType = rawInput.substringAfter(attackDamage + " ").substringBefore(" damage")
            val initiative = rawInput.substringAfter("at initiative ")

            val immuneTo = rawInput.toAttackTypeList("immune to ")
            val weakTo = rawInput.toAttackTypeList("weak to ")

            return Unit(
                hitPoints.toInt(), AttackType.valueOf(attackType.uppercase()), attackDamage.toInt(), initiative.toInt(),
                immuneTo, weakTo
            )
        }

        private fun String.toAttackTypeList(searcher: String): List<AttackType> {
            return if (this.contains(searcher))
                this.substringAfter(searcher).substringBefore(";").substringBefore(")")
                    .split(",")
                    .map { AttackType.valueOf(it.trim().uppercase()) }
            else
                emptyList()
        }

    }

    fun boostAttackDamage(extraDamage: Int) {
        attackDamage += extraDamage
    }

    override fun toString(): String {
        return "hitPoints: $hitPoints, attackType: $attackType, attackDamage: $attackDamage, initiative: $initiative, " +
                "immune to: '$immuneList', weak to: '$weaknessList'"
    }
}

enum class AttackType {
    FIRE, COLD, SLASHING, RADIATION, BLUDGEONING
}