package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.datePicker
import com.github.mvysny.karibudsl.v10.label
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.datepicker.DatePicker.DatePickerI18n
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility
import java.time.LocalDate

class DatePeriod(label:String) : HorizontalLayout() {

    private var from:LocalDate? = null
    private var to:LocalDate? = null

    val value get() = Pair(from,to)

    init {

        val i18nIta = DatePickerI18n()
        i18nIta.monthNames = listOf("Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio", "Giugno",
            "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre")
        i18nIta.weekdays = listOf("Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica")
        i18nIta.weekdaysShort = listOf("Lun", "Mar", "Mer", "Gio", "Ve", "Sab", "Dom")
        i18nIta.week = "Settimana"
        i18nIta.today = "Oggi"
        i18nIta.cancel = "Annulla"

        makeBorder()
        addClassNames(LumoUtility.BorderRadius.LARGE)

        label {
            addClassNames(LumoUtility.Display.BLOCK, LumoUtility.Padding.Left.SMALL, "pv-label-2")
            text = label
        }

        datePicker {
            i18n = i18nIta
            placeholder = "DAL"

            addValueChangeListener {
                from = value
                this@DatePeriod.fireEvent( DatePeriodChangeEvent(this@DatePeriod) )
            }
        }
        datePicker  {
            i18n = i18nIta
            placeholder = "AL"

            addValueChangeListener {
                to = value
                this@DatePeriod.fireEvent( DatePeriodChangeEvent(this@DatePeriod) )
            }
        }
    }

    fun addChangeListener(listener: ComponentEventListener<DatePeriodChangeEvent>): Registration? {
        return addListener(DatePeriodChangeEvent::class.java, listener)
    }

    class DatePeriodChangeEvent(source:DatePeriod, fromClient:Boolean=false) : ComponentEvent<DatePeriod>(source, fromClient)

}