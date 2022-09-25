package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.HasOrderedComponents
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
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


class OrderForm(users:List<User>) : FormLayout(), HasOrderedComponents{

    private val repField: ComboBox<String>
    private val projectField: TextField
    private val dateField: DatePicker
    private val upLoadField: FileUpload
    private val remarksField: TextArea

    private val docForms: List<Serializable>

    private var nextButton:Button
    private var prevButton:Button

    private val formButtons:HorizontalLayout

    private val phonyItem:HorizontalLayout

    private val itemFields:MutableList<HorizontalLayout> = mutableListOf()

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

        phonyItem = HorizontalLayout().apply {
            val i = LoadItemField(listOf(), listOf())

            val button = Button("Aggiungi").apply {
                addThemeVariants(ButtonVariant.LUMO_SMALL)
                addClickListener {
                    addRecord(LoadItemField(listOf("MERCE 1 ", "MERCE 2"), listOf("P1", "P2")))
                }
            }

            i.isEnabled = false

            add(i, button)
        }

        formButtons = HorizontalLayout()
        nextButton = Button("Inserisci Merci")
        prevButton = Button("Annulla")

        nextButton.addClickListener {

            if( !upLoadField.value.second ) {

                addComponentAtIndex(indexOf(formButtons), phonyItem)
                setColspan(phonyItem, 2)
                addRecord(LoadItemField(listOf("MERCE 1 ", "MERCE 2"), listOf("P1", "P2")))
            } else {
                Notification.show("Scan Only. Register Order")
            }
        }

        prevButton.addClickListener {
            nextButton.isVisible = true

            if(itemFields.isNotEmpty()) {

                remove(phonyItem)

                itemFields.forEach(::remove)
                itemFields.clear()
            } else {
                Notification.show("Annulla Ordine")
            }
        }

        formButtons.add(prevButton, nextButton)
        formButtons.addClassNames(LumoUtility.JustifyContent.END,
            LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_90)


        add(formButtons)
        setColspan(formButtons, 2)

        setWidth(50f, Unit.PERCENTAGE)

        upLoadField.addValueChangeListener {
            if( it.value.second ) {
                nextButton.text = "Registra Documento"
                itemFields.forEach({it.isEnabled = false})
            } else {
                nextButton.text = "Inserisci Merci"
                itemFields.forEach({it.isEnabled = true})
            }
        }
    }

    private fun addRecord(lineField: LoadItemField) {
        val l = HorizontalLayout(lineField)

        val b = Button("Rimuovi").apply {
            addThemeVariants(ButtonVariant.LUMO_SMALL)
            addClickListener {
                this@OrderForm.remove(l)
            }
        }
        l.add(lineField, b)

        addComponentAtIndex(indexOf(phonyItem), l)
        setColspan(l, 2)
        itemFields.add(l)
    }
}