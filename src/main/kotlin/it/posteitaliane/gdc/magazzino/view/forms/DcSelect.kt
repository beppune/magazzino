package it.posteitaliane.gdc.magazzino.view.forms

import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import it.posteitaliane.gdc.magazzino.core.Location


class DcSelect(label:String, items:List<Location>) : CustomField<List<Location>>() {

    private val allLabel: Label
    private val selectAll:Checkbox
    private val selectGroup:CheckboxGroup<Location>

    init {
        allLabel = Label(label)

        selectAll = Checkbox()

        selectGroup = CheckboxGroup<Location>()
            .apply {

                setItemLabelGenerator(Location::altname)

                setItems(items)

                addSelectionListener {
                    this@DcSelect.fireEvent(DcSelectValueChangeEvent(this@DcSelect))
                }
            }

        selectAll
            .apply {
                if(value) {
                    selectGroup.select(items)
                } else {
                    selectGroup.deselect(items)
                }
            }

        add(allLabel, selectAll, selectGroup)

    }
    override fun setPresentationValue(dcs: List<Location>?) {
        TODO("Not yet implemented")
    }

    override fun generateModelValue(): List<Location> {
        TODO("Not yet implemented")
    }

    class DcSelectValueChangeEvent(source:DcSelect) : ComponentEvent<DcSelect>(source,false)

}