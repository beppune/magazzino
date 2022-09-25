package it.posteitaliane.gdc.magazzino

import com.vaadin.flow.component.page.AppShellConfigurator
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository
import it.posteitaliane.gdc.magazzino.core.services.FrontOffice
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@Theme("magazzino")
@EnableWebSecurity
@SpringBootApplication
class GestioneMagazzinoApplication : SpringBootServletInitializer(), AppShellConfigurator {

	@Bean
	fun frontOffice(storageRepository: StorageRepository): FrontOffice {
		return FrontOffice(storageRepository)
	}

}

fun main(args: Array<String>) {
	runApplication<GestioneMagazzinoApplication>(*args)
}
