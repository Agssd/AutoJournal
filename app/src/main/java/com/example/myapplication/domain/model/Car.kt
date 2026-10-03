package com.example.myapplication.domain.model

data class Car(
    val id: String,
    val brand: String,
    val plate: String,
    val mileage: Int,
    val isFavorite: Boolean = false,

    // ТО / Замены масла
    val maintenanceType: MaintenanceType = MaintenanceType.OIL_CHANGE,
    val nextMaintenanceMileage: Int? = null, // пробег
    val monthsUntilMaintenance: Int? = null  // время в месяцах
) {
    enum class MaintenanceType {
        OIL_CHANGE, // Замена масла
        INSPECTION  // Регулярное ТО
    }

    val remainingKm: Int?
        get() = nextMaintenanceMileage?.let { (it - mileage).coerceAtLeast(0) }

    val maintenanceProgress: Float
        get() {
            if (nextMaintenanceMileage == null || nextMaintenanceMileage == 0) return 0f
            val progress = mileage.toFloat() / nextMaintenanceMileage.toFloat()
            return progress.coerceIn(0f, 1f)
        }

    val maintenanceTitle: String
        get() = when (maintenanceType) {
            MaintenanceType.OIL_CHANGE -> "До замены масла\nчерез "
            MaintenanceType.INSPECTION -> "До ТО\nчерез "
        }

    val maintenanceSubtitle: String
        get() = when {
            remainingKm != null -> "$remainingKm км"
            monthsUntilMaintenance != null -> "$monthsUntilMaintenance месяца"
            else -> "—"
        }
}