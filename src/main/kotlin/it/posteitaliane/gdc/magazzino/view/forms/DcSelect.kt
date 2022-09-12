package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.core.Location
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository

private fun HasStyle.makeBorder() : Unit {
    addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_90)
}

class DcSelect(private val items:List<Location>) : HorizontalLayout(){


    private val allItems:Checkbox
    private val group:CheckboxGroup<Location>

    private var selected:List<Location> = listOf()

    val value get() = selected

    init {


        makeBorder()

        addClassNames(LumoUtility.JustifyContent.CENTER, LumoUtility.BorderRadius.LARGE)

        label("DATACENTER") {
            addClassNames(LumoUtility.Display.BLOCK, LumoUtility.Padding.Left.SMALL, "pv-label")
        }

        allItems = checkBox() {
            addClassNames(LumoUtility.Display.BLOCK, LumoUtility.Padding.Left.XSMALL)
        }

        group = checkBoxGroup<Location> {

            setItemLabelGenerator(Location::altname)

            setItems(items)

            addSelectionListener {
                selected = it.value.toList()

                this@DcSelect.fireEvent(DcselecthangeEvent(this@DcSelect))
            }
        }

        allItems.apply {
            addClassNames(LumoUtility.Display.BLOCK, "pv-checkbox", LumoUtility.Border.RIGHT, LumoUtility.BorderColor.CONTRAST_90)
            addValueChangeListener {
                if(allItems.value) {
                    group.select(items)
                } else {
                    group.deselect(items)
                }
            }
        }

    }

    fun addValueChangeListener(listener: ComponentEventListener<DcselecthangeEvent>): Registration? {
        return addListener(DcselecthangeEvent::class.java, listener)
    }

    class DcselecthangeEvent(source:DcSelect, fromClient:Boolean=false) : ComponentEvent<DcSelect>(source, fromClient)

}