package com.cosmobond.watchface

internal data class CompanionPetStats(
    var hunger: Int = 50,
    var energy: Int = 50,
    var mood: Int = 50,
) {
    fun tick(deltaMs: Long, active: Boolean) {
        if (!active) return
        if (deltaMs <= 0) return
        val change = (deltaMs / 4_000L).toInt().coerceAtMost(3)
        if (change <= 0) return
        hunger = (hunger + change).coerceAtMost(100)
        mood = (mood + change / 2).coerceAtMost(100)
        energy = (energy - change).coerceIn(0, 100)
    }
}
