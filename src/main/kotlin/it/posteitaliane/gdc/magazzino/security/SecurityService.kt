package it.posteitaliane.gdc.magazzino.security

import com.vaadin.flow.component.UI
import com.vaadin.flow.server.VaadinServletRequest
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler
import org.springframework.stereotype.Service

@Service
class SecurityService {

    val authenticatedUser: UserDetails?
        get() {
            val context = SecurityContextHolder.getContext()
            val principal = context.authentication.principal
            return if (principal is UserDetails)
                principal
            else
                null
        }

    fun logout() {
        UI.getCurrent().page.setLocation("/")
        val logoutHandler = SecurityContextLogoutHandler()
        logoutHandler.logout(VaadinServletRequest.getCurrent().httpServletRequest, null, null)
    }

}
