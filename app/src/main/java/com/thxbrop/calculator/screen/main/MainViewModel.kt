package com.thxbrop.calculator.screen.main

import com.thxbrop.calculator.eventOf
import com.thxbrop.calculator.screen.BaseViewModel

class MainViewModel : BaseViewModel<MainState, MainEvent>(MainState()) {
    override fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Append -> {
                val formula = readable.formula
                _state.value = readable.copy(
                    formula = if (formula.size == 1 && formula.first() == "0") {
                        if (event.s in arrayOf("+", "-", "×", "÷")) listOf("0", event.s)
                        else listOf(event.s)
                    } else buildList {
                        val last = formula.last()
                        if (last in arrayOf("+", "-", "×", "÷")) {
                            if (event.s in arrayOf("+", "-", "×", "÷")) {
                                addAll(formula.dropLast(1))
                                add(event.s)
                            } else {
                                addAll(formula)
                                add(event.s)
                            }
                        } else if (last.last() == '.') {
                            if (event.s in arrayOf("+", "-", "×", "÷")) {
                                addAll(formula.dropLast(1))
                                add(formula.last().dropLast(1))
                                add(event.s)
                            } else {
                                addAll(formula.dropLast(1))
                                add("${formula.last()}${event.s}")
                            }
                        } else {
                            if (event.s in arrayOf("+", "-", "×", "÷")) {
                                addAll(formula)
                                add(event.s)
                            } else {
                                addAll(formula.dropLast(1))
                                add(last + event.s)
                            }
                        }
                    },
                    scroll = false
                )
            }
            MainEvent.Drop -> {
                var formula = readable.formula.toMutableList()
                val last = formula.last()
                if ((last.length > 1)) {
                    formula[formula.size - 1] = last.dropLast(1)
                } else {
                    formula = formula.dropLast(1).ifEmpty { listOf("0") }.toMutableList()
                }
                _state.value = readable.copy(
                    formula = formula,
                    scroll = false
                )
            }
            MainEvent.Clear -> {
                _state.value = readable.copy(
                    formula = listOf("0"),
                    scroll = false
                )
            }
            MainEvent.Percent -> {
                val last = readable.formula.last()
                val double = last.toDoubleOrNull()
                double?.let {
                    val list = readable.formula.toMutableList().apply {
                        removeLast()
                        add((it / 100).toString())
                    }
                    _state.value = readable.copy(
                        formula = list,
                        scroll = false
                    )
                }

            }
            MainEvent.Decimal -> {
                val formula = readable.formula.toMutableList()
                val last = formula.last()
                if (last.contains('.')) return
                if (last in arrayOf("+", "-", "×", "÷")) {
                    formula.add("0.")
                } else formula[formula.size - 1] = "$last."
                _state.value = readable.copy(
                    formula = formula,
                    scroll = false
                )
            }
            MainEvent.Calculate -> calculate()
        }
    }

    private fun calculate() {
        val formula = readable.formula.toMutableList()
        if (formula.first() in arrayOf("+", "-", "×", "÷")) formula.removeFirst()
        if (formula.last() in arrayOf("+", "-", "×", "÷")) formula.removeLast()
        while ("×" in formula || "÷" in formula) {
            val indexTimes = formula.indexOf("×")
            val indexDiv = formula.indexOf("÷")
            if (indexTimes == -1) {
                val last = formula[indexDiv - 1].toDouble().toBigDecimal()
                val next = formula[indexDiv + 1].toDouble().toBigDecimal()
                if (next.toDouble() == 0.0) {
                    _state.value = readable.copy(
                        message = eventOf("Divisor cannot be 0.")
                    )
                    return
                }
                val result = last / next
                formula[indexDiv] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexDiv - 1)
                formula.removeAt(indexDiv)
            } else if (indexDiv == -1) {
                val last = formula[indexTimes - 1].toDouble().toBigDecimal()
                val next = formula[indexTimes + 1].toDouble().toBigDecimal()
                val result = last * next
                formula[indexTimes] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexTimes - 1)
                formula.removeAt(indexTimes)
            } else if (indexTimes < indexDiv) {
                val last = formula[indexTimes - 1].toDouble().toBigDecimal()
                val next = formula[indexTimes + 1].toDouble().toBigDecimal()
                val result = last.times(next)
                formula[indexTimes] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexTimes - 1)
                formula.removeAt(indexTimes)
            } else {
                val last = formula[indexDiv - 1].toDouble().toBigDecimal()
                val next = formula[indexDiv + 1].toDouble().toBigDecimal()
                if (next.toDouble() == 0.0) {
                    _state.value = readable.copy(
                        message = eventOf("Divisor cannot be 0.")
                    )
                    return
                }
                val result = last / next
                formula[indexDiv] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexDiv - 1)
                formula.removeAt(indexDiv)
            }

        }

        while ("+" in formula || "-" in formula) {
            val indexPlus = formula.indexOf("+")
            val indexMinus = formula.indexOf("-")
            if (indexPlus == -1) {
                val last = formula[indexMinus - 1].toDouble().toBigDecimal()
                val next = formula[indexMinus + 1].toDouble().toBigDecimal()
                val result = last - next
                formula[indexMinus] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexMinus - 1)
                formula.removeAt(indexMinus)
            } else if (indexMinus == -1) {
                val last = formula[indexPlus - 1].toDouble().toBigDecimal()
                val next = formula[indexPlus + 1].toDouble().toBigDecimal()
                val result = last + next
                formula[indexPlus] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexPlus - 1)
                formula.removeAt(indexPlus)
            } else if (indexPlus < indexMinus) {
                val last = formula[indexPlus - 1].toDouble().toBigDecimal()
                val next = formula[indexPlus + 1].toDouble().toBigDecimal()
                val result = last + next
                formula[indexPlus] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexPlus - 1)
                formula.removeAt(indexPlus)
            } else {
                val last = formula[indexMinus - 1].toDouble().toBigDecimal()
                val next = formula[indexMinus + 1].toDouble().toBigDecimal()
                val result = last - next
                formula[indexMinus] = if (result.compareTo(result.toLong().toBigDecimal()) != 0) {
                    result.toString()
                } else result.toInt().toString()
                formula.removeAt(indexMinus - 1)
                formula.removeAt(indexMinus)
            }

        }
        val list = readable.pre.toMutableList()
        list.add(readable.formula.joinToString(separator = "") + "=${formula.first()}")
        _state.value = readable.copy(
            pre = list,
            formula = formula,
            scroll = true
        )
    }

}