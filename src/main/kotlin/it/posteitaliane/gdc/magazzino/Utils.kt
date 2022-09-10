package it.posteitaliane.gdc.magazzino

import com.vaadin.flow.component.datepicker.DatePicker

object Utils {

    val dateI18nIta: DatePicker.DatePickerI18n

    init {
        dateI18nIta = DatePicker.DatePickerI18n()
        dateI18nIta.monthNames = listOf(
            "Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"
        )
        dateI18nIta.weekdays = listOf("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica")
        dateI18nIta.weekdaysShort = listOf("Lun", "Mar", "Mer", "Gio", "Ve", "Sab", "Dom")
        dateI18nIta.week = "Settimana"
        dateI18nIta.today = "Oggi"
        dateI18nIta.cancel = "Annulla"
    }

}