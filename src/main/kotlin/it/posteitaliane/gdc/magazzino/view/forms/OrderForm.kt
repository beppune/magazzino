package it.posteitaliane.gdc.magazzino.view.forms

import com.github.mvysny.karibudsl.v10.*
import com.github.mvysny.kaributools.placeholder
import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.upload.UploadI18N
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.core.OrderSubject
import it.posteitaliane.gdc.magazzino.core.OrderType

class OrderForm : FormLayout(){


    init {
        makeBorder()

        setWidth(66f, Unit.PERCENTAGE)

        select<OrderSubject> {
            placeholder = "TIPO ORDINE"

            setItems(OrderSubject.INTERNAL, OrderSubject.PARTNER)
            setItemLabelGenerator {
                if(it == OrderSubject.INTERNAL)
                    "USO INTERNO"
                else
                    "FORNITORE"
            }
        }

        textField {
            placeholder = "REFERENTE"
        }

        datePicker {
            placeholder = "DATA ORDINE"
        }

        datePicker {
            placeholder = "PROGETTO"
        }

        textArea {
            placeholder = "NOTE"
        }

        horizontalLayout {
            makeBorder()

            checkBox("SOLO DOCUMENTO")
            upload {
                isDropAllowed = false
                addClassNames(LumoUtility.AlignSelf.END)

                val i18nIta = UploadI18N()
                    .apply {
                        addFiles = UploadI18N.AddFiles().setOne("Seleziona PDF")

                    }

                i18n = i18nIta
            }
        }

    }

}