package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.IntegerField
import com.vaadin.flow.component.textfield.NumberField
import com.vaadin.flow.component.textfield.TextField
import it.posteitaliane.gdc.magazzino.core.OrderLine
import kotlin.math.max

class LoadItemField(items:List<String>, positions:List<String>) : CustomField<OrderLine>(){

    private val itemField: ComboBox<String>
    private val posField: Select<String>
    private val amountField: IntegerField

    private val snField:TextField
    private val ptField:TextField

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

        add(itemField, posField, amountField, snField, ptField)
    }


    override fun setPresentationValue(value: OrderLine?) {
        TODO("Not yet implemented")
    }

    override fun generateModelValue(): OrderLine {
        TODO("Not yet implemented")
    }


}