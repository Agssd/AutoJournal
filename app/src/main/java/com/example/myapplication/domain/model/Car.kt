package com.example.myapplication.domain.model

data class Car(
    val id: String,
    val brand: String,
    val plate: String,
    val mileage: Int,
    val isFavorite: Boolean = false,

    // Поля для ТО / Замены масла
    val maintenanceType: MaintenanceType = MaintenanceType.OIL_CHANGE,
    val nextMaintenanceMileage: Int? = null, // Целевой пробег для ТО (например, 248 800)
    val monthsUntilMaintenance: Int? = null  // Или оставшееся время в месяцах
) {
    enum class MaintenanceType {
        OIL_CHANGE, // Замена масла
        INSPECTION  // Регулярное ТО
    }

    // Рассчитываем оставшийся пробег
    val remainingKm: Int?
        get() = nextMaintenanceMileage?.let { (it - mileage).coerceAtLeast(0) }

    // Вычисляем прогресс для LinearProgressIndicator (от 0.0f до 1.0f)
    val maintenanceProgress: Float
        get() {
            if (nextMaintenanceMileage == null || nextMaintenanceMileage == 0) return 0f
            // Примерная логика: соотношение текущего пробега к целевому
            val progress = mileage.toFloat() / nextMaintenanceMileage.toFloat()
            return progress.coerceIn(0f, 1f)
        }

    // Динамический заголовок
    val maintenanceTitle: String
        get() = when (maintenanceType) {
            MaintenanceType.OIL_CHANGE -> "До замены масла\nчерез "
            MaintenanceType.INSPECTION -> "До ТО\nчерез "
        }

    // Динамический подзаголовок (текст, который выделяем оранжевым)
    val maintenanceSubtitle: String
        get() = when {
            remainingKm != null -> "$remainingKm км"
            monthsUntilMaintenance != null -> "$monthsUntilMaintenance месяца"
            else -> "—"
        }
}