package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField


fun ComboBox<*>.maxLengthJs(max:Int) {
    element.executeJs("this.childNodes.item(0).maxLength = ${max}")
}

fun TextField.uppercaseJs() {
    element.executeJs("this.childNodes.item(0).style['text-transform'] = 'Uppercase'")
}

fun TextArea.uppercaseJs() {
    element.executeJs("this.childNodes.item(0).style['text-transform'] = 'Uppercase'")
}

fun ComboBox<*>.uppercaseJs() {
    element.executeJs("this.childNodes.item(0).style['text-transform'] = 'Uppercase'")
}