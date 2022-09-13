package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.github.mvysny.kaributools.fireEvent
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.StartedEvent
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.UploadI18N
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.theme.lumo.LumoUtility
import java.time.LocalDate
import java.time.LocalTime
import kotlin.system.measureTimeMillis

typealias FileValue = Pair<String?,Boolean>

class FileUpload : CustomField<FileValue>(){

    private val addButton:Button
    private val removeButton:Button
    private val onlyScan:Checkbox

    private val upload:Upload

    private var fileName:String?=null

    init {
        addClassNames(LumoUtility.Padding.Vertical.XSMALL)

        addButton = Button("Carica PDF").apply {
            setWidth(50f, Unit.PERCENTAGE)
        }
        removeButton = Button("Rimuovi").apply {
            addThemeVariants(ButtonVariant.LUMO_ERROR)

            setWidth(50f, Unit.PERCENTAGE)
            isVisible = false
        }
        onlyScan = Checkbox("Solo Scansione")

        upload = Upload(MemoryBuffer()).apply {
            addClassNames(LumoUtility.Display.HIDDEN)
            addSucceededListener {
                fileName = it.fileName
                removeButton.text = fileName
                removeButton.isVisible = true
                addButton.isVisible = false

                this@FileUpload.updateValue()
            }
        }

        addButton.addClickListener {
            upload.getElement().callJsFunction("shadowRoot.getElementById('addFiles').click")
        }

        removeButton.addClickListener {
            upload.clearFileList()
            removeButton.text = null
            removeButton.isVisible = false
            addButton.isVisible = true
            updateValue()
        }

        add(addButton, removeButton, onlyScan, upload)
    }
    override fun setPresentationValue(fv: FileValue?) {
        onlyScan.value = fv?.second
    }

    override fun generateModelValue(): FileValue {
        return Pair(removeButton.text, onlyScan.value)
    }


}