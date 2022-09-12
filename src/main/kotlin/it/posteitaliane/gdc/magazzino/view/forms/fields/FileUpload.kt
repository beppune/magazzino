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
import kotlin.system.measureTimeMillis

class FileUpload : CustomField<Pair<String?,Boolean>>(){

    private var isOnlyScan: Boolean = false
    private var fileName: String? = null
    private val addButton: Button
    private val removeButton: Button
    private val onlyScan: Checkbox
    private val upload:Upload

    init {

        upload = Upload(MemoryBuffer()).apply {
            isDropAllowed = false
            //setAcceptedFileTypes("application/pdf")
            addClassNames("hidden")
        }

        addButton = Button("Upload PDF...")
            .apply {
                setWidth(15f, Unit.REM)
            }

        removeButton = Button("Rimuovi")
            .apply {
                addThemeVariants(ButtonVariant.LUMO_ERROR)
                setWidth(15f, Unit.REM)
                isVisible = false
            }

        onlyScan = Checkbox("Solo Scansione")
            .apply {
                addClassNames(
                    LumoUtility.Display.FLEX,
                    LumoUtility.AlignItems.CENTER
                )
            }

        addButton.addClickListener {
            upload.getElement().callJsFunction("shadowRoot.getElementById('addFiles').click")
        }

        upload.addSucceededListener {

            fileName = it.fileName

            setPresentationValue(Pair(it.fileName,onlyScan.value))
        }

        removeButton.addClickListener {
            fileName = null

            setPresentationValue(Pair(null,onlyScan.value))
        }

        add(
            HorizontalLayout(upload,addButton,removeButton, onlyScan).apply {
                addClassNames(LumoUtility.Padding.NONE)
            }
        )
    }

    override fun setPresentationValue(value: Pair<String?,Boolean>) {
        if(value.first != null) {
            addButton.isVisible = false
            removeButton.isVisible = true
            removeButton.text = value.first
        } else {
            upload.clearFileList()
            addButton.isVisible = true
            removeButton.isVisible = false
        }
    }

    override fun generateModelValue(): Pair<String?,Boolean> {
        return Pair(fileName,isOnlyScan)
    }
}