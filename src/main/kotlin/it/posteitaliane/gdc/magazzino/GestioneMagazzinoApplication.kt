package it.posteitaliane.gdc.magazzino

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

@EnableWebSecurity
@SpringBootApplication
class GestioneMagazzinoApplication

fun main(args: Array<String>) {
	runApplication<GestioneMagazzinoApplication>(*args)
}
