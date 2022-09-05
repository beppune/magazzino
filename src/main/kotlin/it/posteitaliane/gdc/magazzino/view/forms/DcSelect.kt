package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.checkBox
import com.github.mvysny.karibudsl.v10.checkBoxGroup
import com.github.mvysny.karibudsl.v10.label
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.core.Location

fun HasStyle.makeBorder() {
    addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_90)
}

class DcSelect(allLabel:String, private val items:List<Location>) : HorizontalLayout(){

    private var allItems:Checkbox
    private val group:CheckboxGroup<Location>

    val value get() = group.selectedItems

    init {


        setId("line-1")
        makeBorder()

        addClassNames(LumoUtility.JustifyContent.CENTER, LumoUtility.BorderRadius.LARGE)


        label(allLabel) {
            addClassNames(LumoUtility.Display.BLOCK, LumoUtility.Padding.Left.SMALL, "pv-label")
        }

        allItems = checkBox() {
            addClassNames(LumoUtility.Display.BLOCK, LumoUtility.Padding.Left.XSMALL)
        }

        group = checkBoxGroup<Location> {

            setItemLabelGenerator(Location::altname)

            setItems(items)

            addSelectionListener {

                this@DcSelect.fireEvent(MainFormChangeEvent(this@DcSelect))
            }
        }

        allItems.apply {
            addClassNames(LumoUtility.Display.BLOCK, "pv-checkbox", LumoUtility.Border.RIGHT, LumoUtility.BorderColor.CONTRAST_90)
            addValueChangeListener {
                if(value) {
                    group.select(items)
                } else {
                    group.deselect(items)
                }
            }
        }

    }

    fun addChangeListener(listener: ComponentEventListener<MainFormChangeEvent>): Registration? {
        return addListener(MainFormChangeEvent::class.java, listener)
    }

    class MainFormChangeEvent(source:DcSelect, fromClient:Boolean=false) : ComponentEvent<DcSelect>(source, fromClient)

}