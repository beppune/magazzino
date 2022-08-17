package it.posteitaliane.gdc.magazzino.view.login

import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.login.LoginForm
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.BeforeEnterEvent
import com.vaadin.flow.router.BeforeEnterObserver
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route("login")
@PageTitle("Gestione Magazzino - Login")
class LoginView(
    private val login: LoginForm = LoginForm()
) : VerticalLayout(), BeforeEnterObserver {

    init {
        addClassName("login-view")
        setSizeFull()

        justifyContentMode = FlexComponent.JustifyContentMode.CENTER
        alignItems = FlexComponent.Alignment.CENTER

        login.action = "login"

        add(H1("Gestione Magazzino - Login"), login)
    }

    override fun beforeEnter(event: BeforeEnterEvent?) {
        if(event?.location?.queryParameters?.parameters?.containsKey("error") == true )
            login.isError = true
    }
}