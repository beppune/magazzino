package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.Utils
import java.time.LocalDate

class DateRangeField(label:String?=null) : CustomField<Pair<LocalDate?,LocalDate?>>() {

    private val from = DatePicker()
    private val to = DatePicker()

    init {


        from.apply {
            i18n = Utils.dateI18nIta
            placeholder = "DAL"

            addClassNames(LumoUtility.Margin.Right.MEDIUM)
        }

        to.apply {
            i18n = Utils.dateI18nIta
            placeholder = "AL"
        }

        from.addValueChangeListener {
            if(it.value != null) {
                to.min = it.value
            }
        }

        to.addValueChangeListener {
            if(it.value != null) {
                from.max = it.value
            }
        }

        if( label != null ) {
            this.label = label
        }
        add(from, to)

    }

    override fun setPresentationValue(pair: Pair<LocalDate?, LocalDate?>) {
        from.value = pair.first
        to.value = pair.second
    }

    override fun generateModelValue(): Pair<LocalDate?, LocalDate?> {
        return Pair(from.value, to.value)
    }


}