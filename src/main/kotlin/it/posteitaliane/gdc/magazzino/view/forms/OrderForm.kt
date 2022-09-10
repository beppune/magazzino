package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.placeholder
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.UploadI18N
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.ValidationException
import com.vaadin.flow.data.validator.StringLengthValidator
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository

class OrderForm(var order:MutableOrder) : FormLayout(){

    val binder = Binder(MutableOrder::class.java)

    val repField:ComboBox<String>

    val saveButton:Button

    init {
        binder.readBean(order)

        repField = comboBox {
            setItems( "ONE", "TWO", "THREE" )
            isAllowCustomValue = true
            placeholder = "REFERENTE"

            binder.forField(this)
                .asRequired("Campo Obbligatorio")
                .bind("rep")

        }

        saveButton = Button("Save") {

            try {
                binder.writeBean(order)
                Notification.show(order.rep)
            } catch (ex:ValidationException) {

            }

        }

        add(
            repField,
            saveButton
        )

        setColspan(repField, 2)

    }

}