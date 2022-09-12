package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.Utils
import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.view.forms.fields.FileUpload
import java.time.LocalDate

class OrderForm() : FormLayout(){

    private val refField: TextField
    private val projectField: TextField
    private val dateField: DatePicker
    private val upLoadField: FileUpload
    private val remarksField: TextArea

    init {
        refField = textField {
            placeholder = "REFERENTE (max 30 caratteri)"
            maxLength = 30
        }

        dateField = datePicker {
            placeholder = "DATA ORDINE"
            i18n = Utils.dateI18nIta
            max = LocalDate.now()
        }

        projectField = textField {
            placeholder = "PROGETTO (max 50 caratteri)"
            maxLength = 50
        }

        upLoadField = FileUpload().apply { this@OrderForm.add(this) }

        remarksField = textArea {
            placeholder = "max 500 caratteri"
        }

        setColspan(remarksField, 2)

        setWidth(50f, Unit.PERCENTAGE)
    }
}