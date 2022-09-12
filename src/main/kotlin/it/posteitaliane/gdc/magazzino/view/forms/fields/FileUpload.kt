package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.github.mvysny.kaributools.fireEvent
import com.vaadin.flow.component.ClickEvent
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.customfield.CustomField
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.theme.lumo.LumoUtility
import kotlin.system.measureTimeMillis

class FileUpload : CustomField<String>(){

    private val buffer = MemoryBuffer()

    init {
        add(
            HorizontalLayout(
                Upload(buffer).apply {
                                     isDropAllowed
                },
                TextField().apply { isReadOnly = true }
            ).apply {
                addClassNames(LumoUtility.Padding.NONE)
            }
        )
    }

    override fun setPresentationValue(p0: String?) {

    }

    override fun generateModelValue(): String {
        return ""
    }
}