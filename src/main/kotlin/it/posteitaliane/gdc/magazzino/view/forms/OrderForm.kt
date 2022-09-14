package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.dom.Element
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.Utils
import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.view.forms.fields.FileUpload
import java.time.LocalDate

class OrderForm(users:List<User>) : FormLayout(){

    private val repField: ComboBox<String>
    private val projectField: TextField
    private val dateField: DatePicker
    private val upLoadField: FileUpload
    private val remarksField: TextArea

    init {
        repField = comboBox {
            placeholder = "REFERENTE (max 30 caratteri)"
            isAllowCustomValue = true

            setItems(users.map { "${it.lastName} ${it.firstName}" })

            addCustomValueSetListener {
                value = it.detail.take(30).uppercase()
            }
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

        upLoadField = FileUpload()
            .apply {
                this@OrderForm.add(this)

                addValueChangeListener {
                    Notification.show(it.value.toString())
                }
            }

        remarksField = textArea {
            placeholder = "max 500 caratteri"
            maxLength = 500
        }

        setColspan(remarksField, 2)

        setWidth(50f, Unit.PERCENTAGE)
    }
}