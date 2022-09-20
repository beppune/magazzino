package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.Utils
import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.view.forms.fields.FileUpload
import it.posteitaliane.gdc.magazzino.view.forms.fields.LoadItemField
import it.posteitaliane.gdc.magazzino.view.forms.fields.maxLengthJs
import it.posteitaliane.gdc.magazzino.view.forms.fields.uppercaseJs
import java.io.Serializable
import java.time.LocalDate


class OrderForm(users:List<User>) : FormLayout(){

    private val repField: ComboBox<String>
    private val projectField: TextField
    private val dateField: DatePicker
    private val upLoadField: FileUpload
    private val remarksField: TextArea

    private val docForms: List<Serializable>

    private var nextButton:Button
    private var importButton:Button
    private var prevButton:Button

    private val formButtons:HorizontalLayout

    init {
        repField = comboBox {
            placeholder = "REFERENTE (max 30 caratteri)"
            isAllowCustomValue = true

            setItems(users.map { "${it.lastName} ${it.firstName}" })

            addCustomValueSetListener {
                value = it.detail.uppercase() // uppercaseJs only set style. value still depends on keyboard input
            }

            isRequired = true
            isRequiredIndicatorVisible = true

            maxLengthJs(30)

            uppercaseJs()
        }

        dateField = datePicker {
            placeholder = "DATA ORDINE"
            i18n = Utils.dateI18nIta
            max = LocalDate.now()
        }

        projectField = textField {
            placeholder = "PROGETTO (max 50 caratteri)"
            maxLength = 50

            addBlurListener{
                value = value?.uppercase()
            }

            uppercaseJs()
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

            addBlurListener{
                value = value?.uppercase()
            }

            uppercaseJs()
        }

        setColspan(remarksField, 2)

        docForms = listOf(repField, dateField, projectField, upLoadField, remarksField)

        /*
            Form Buttons
        */

        formButtons = HorizontalLayout()
        nextButton = Button("Inserisci Merci")
        importButton = Button("Inserisci Merci da Excel").apply { addClassNames(LumoUtility.Margin.Right.AUTO) }
        prevButton = Button("Annulla")

        formButtons.add(importButton, prevButton, nextButton)
        formButtons.addClassNames(LumoUtility.JustifyContent.END,
            LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_90)


        add(formButtons)
        setColspan(formButtons, 2)

        var m1 = LoadItemField(listOf("MERCE 1", "MERCE 2", "MERCE 3"), listOf("A1", "A2", "A3"))

        add(m1)

        setColspan(m1, 2)

        setWidth(50f, Unit.PERCENTAGE)

    }
}