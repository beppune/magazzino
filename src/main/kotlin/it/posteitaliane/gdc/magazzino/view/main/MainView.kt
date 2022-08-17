package it.posteitaliane.gdc.magazzino.view.main

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.Route
import it.posteitaliane.gdc.magazzino.security.SecurityService
import javax.annotation.security.PermitAll

@Route
@PermitAll
class MainView(
    private val security: SecurityService
) : VerticalLayout() {

    private val logoutButton = Button("Logout") { security.logout() }

    init {
        add(H2("Hello Vaadin"), logoutButton)
    }

}