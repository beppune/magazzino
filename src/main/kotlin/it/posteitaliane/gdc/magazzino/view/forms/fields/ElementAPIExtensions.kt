package it.posteitaliane.gdc.magazzino.view.forms.fields

import com.vaadin.flow.component.combobox.ComboBox
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.dom.Element

private fun execupperjs(element: Element) = element.executeJs("this.childNodes.item(0).style['text-transform'] = 'Uppercase'")

fun ComboBox<*>.maxLengthJs(max:Int) {
    element.executeJs("this.childNodes.item(0).maxLength = ${max}")
}

fun TextField.uppercaseJs() = execupperjs(element)

fun TextArea.uppercaseJs() = execupperjs(element)

fun ComboBox<*>.uppercaseJs() = execupperjs(element)

fun TextField.allowOnlyDigitsJs() {
    element.executeJs("""
        const ALLOWED_CHARS_REGEXP = /[0-9]+/;
        this.childNodes.item(0).addEventListener("keypress", event => {
          if (!ALLOWED_CHARS_REGEXP.test(event.key)) {
            event.preventDefault();
          }
        });
    """.trimIndent())
}