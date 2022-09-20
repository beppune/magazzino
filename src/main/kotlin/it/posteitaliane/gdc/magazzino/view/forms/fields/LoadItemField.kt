package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.TextField
import it.posteitaliane.gdc.magazzino.core.MutableOrder
import it.posteitaliane.gdc.magazzino.core.OrderLine

class LoadItemField(items:List<String>, positions:List<String>) : CustomField<String>(){

    private val itemField: ComboBox<String>
    private val posField: Select<String>
    private val amountField: IntegerField

    private val snField:TextField
    private val ptField:TextField

    private val actionButton:Button
    init {

        itemField = ComboBox<String>().apply {

            setItems(items)
            placeholder = "MERCE"

            isRequired = true
            isRequiredIndicatorVisible = true
            isAllowCustomValue = true

            addCustomValueSetListener {
                value = it.detail.take(50).uppercase()
            }
        }

        posField = Select<String>().apply {
            placeholder = "POSIZIONE"
            setItems(positions)
            isRequiredIndicatorVisible = true


        }

        amountField = IntegerField().apply {
            placeholder = "QUANTITA'"
            isRequiredIndicatorVisible = true
            min = 1

            setWidth(3f, Unit.REM)
        }

        snField = TextField().apply {
            placeholder = "S/N"
            maxLength = 20
            isRequired = false
        }

        ptField = TextField().apply {
            placeholder = "PT"
            maxLength = 8
            isRequired = false

            pattern = "[0-9]*"
        }

        actionButton = Button("Aggiungi Merce").apply {
            addThemeVariants(ButtonVariant.LUMO_SMALL)
        }

        add(itemField, posField, amountField, snField, ptField, actionButton)
    }


    override fun setPresentationValue(line: String?) {

    }

    override fun generateModelValue(): String {
        return ""
    }


}