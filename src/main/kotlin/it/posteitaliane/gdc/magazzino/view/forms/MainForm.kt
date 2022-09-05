package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.vaadin.flow.component.ComponentEvent
import com.vaadin.flow.component.ComponentEventListener
import com.vaadin.flow.component.HasStyle
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.shared.Registration
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.core.Location
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository

fun HasStyle.makeBorder() : Unit {
    addClassNames(LumoUtility.Border.ALL, LumoUtility.BorderColor.CONTRAST_90)
}

class Mainform(private val storage: StorageRepository) : HorizontalLayout(){

    private val items = storage.findLocations()

    private val allLabel: Label
    private val allItems:Checkbox
    private val group:CheckboxGroup<Location>

    private var selected:List<Location> = listOf()

    val value get() = selected

    init {


        setId("line-1")
        makeBorder()

        addClassNames(LumoUtility.JustifyContent.CENTER)

        allLabel = label("DATACENTER") {
            addClassNames(LumoUtility.Display.BLOCK, "pv-label")
        }

        allItems = checkBox()

        group = checkBoxGroup<Location> {

            setItemLabelGenerator(Location::altname)

            setItems(items)

            addSelectionListener {
                selected = it.value.toList()

                this@Mainform.fireEvent(MainFormChangeEvent(this@Mainform))
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

    fun addChangeListener(listener: ComponentEventListener<MainFormChangeEvent>): Registration? {
        return addListener(MainFormChangeEvent::class.java, listener)
    }

    class MainFormChangeEvent(source:Mainform, fromClient:Boolean=false) : ComponentEvent<Mainform>(source, fromClient)

}