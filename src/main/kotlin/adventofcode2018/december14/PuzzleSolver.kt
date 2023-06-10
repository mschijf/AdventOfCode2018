package adventofcode2018.december14

import adventofcode2018.PuzzleSolverAbstract

fun main() {
    PuzzleSolver(test=true).showResult()
}

class PuzzleSolver(test: Boolean) : PuzzleSolverAbstract(test) {


    override fun resultPartOne(): Any {
        val puzzleInput = 74501
        val scoreList = mutableListOf(3, 7)
        var elf1 = 0
        var elf2 = 1

        while (scoreList.size < puzzleInput + 10) {
            val newRecipe = scoreList[elf1] + scoreList[elf2]
            if (newRecipe < 10) {
                scoreList.add(newRecipe)
            } else {
                scoreList.add(newRecipe / 10)
                scoreList.add(newRecipe % 10)
            }
            elf1 = (elf1 + 1 + scoreList[elf1]) % scoreList.size
            elf2 = (elf2 + 1 + scoreList[elf2]) % scoreList.size
        }
        return scoreList.takeLast(10).joinToString("")
    }



    override fun resultPartTwo(): Any {
        val puzzleInput = "074501"
        val maxArraySize = 500_000_000
        val scoreList = IntArray(maxArraySize)
        scoreList[0] = 3
        scoreList[1] = 7
        var recipeCount = 2
        var elf1 = 0
        var elf2 = 1
        while (recipeCount < maxArraySize) {
            val newRecipe = scoreList[elf1] + scoreList[elf2]
            if (newRecipe < 10) {
                scoreList[recipeCount++] = newRecipe
                if (scoreList.endsWith(recipeCount, puzzleInput))
                    return recipeCount - puzzleInput.length
            } else {
                scoreList[recipeCount++] = newRecipe / 10
                if (scoreList.endsWith(recipeCount, puzzleInput))
                    return recipeCount - puzzleInput.length
                scoreList[recipeCount++] = newRecipe % 10
                if (scoreList.endsWith(recipeCount, puzzleInput))
                    return recipeCount - puzzleInput.length
            }
            elf1 = (elf1 + 1 + scoreList[elf1]) % recipeCount
            elf2 = (elf2 + 1 + scoreList[elf2]) % recipeCount
        }
        return "not found within $maxArraySize recipes"
    }

    private fun IntArray.endsWith(size: Int, matchValue: String): Boolean {
        if (size < matchValue.length)
            return false

        var x = 0
        for (i in size-matchValue.length until size)
            x = 10*x + this[i]
        return x == matchValue.toInt()
    }
}




