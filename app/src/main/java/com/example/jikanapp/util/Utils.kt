package com.example.jikanapp.util

import com.example.jikanapp.domain.model.full.Genre
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import obtenerElementoDelMedio

class Utils {


    companion object {

        fun getCurrentDayOfWeek(): String {
            // Obtener la fecha y hora actuales
            val currentMoment: Instant = Clock.System.now()
            // Convertir a LocalDate en la zona horaria local
            val currentDate = currentMoment.toLocalDateTime(TimeZone.currentSystemDefault()).date
            // Obtener el día de la semana
            return currentDate.dayOfWeek.name // Esto te dará el nombre del día de la semana
        }

        fun <T> listToStrings(lista: List<T>): String {
            return lista.joinToString(", ") { it.toString() }
        }
    }


}