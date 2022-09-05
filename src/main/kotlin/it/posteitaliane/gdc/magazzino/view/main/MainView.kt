package it.posteitaliane.gdc.magazzino.view.main

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.checkbox.CheckboxGroup
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.LitRenderer
import com.vaadin.flow.data.renderer.LocalDateRenderer
import com.vaadin.flow.data.renderer.Renderer
import com.vaadin.flow.data.renderer.TextRenderer
import com.vaadin.flow.router.Route
import it.posteitaliane.gdc.magazzino.adapters.storage.JdbcStorageRepository
import it.posteitaliane.gdc.magazzino.core.Location
import it.posteitaliane.gdc.magazzino.core.Order
import it.posteitaliane.gdc.magazzino.core.OrderSubject
import it.posteitaliane.gdc.magazzino.core.OrderType
import it.posteitaliane.gdc.magazzino.core.ports.StorageRepository
import it.posteitaliane.gdc.magazzino.security.SecurityService
import it.posteitaliane.gdc.magazzino.view.forms.Mainform
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import javax.annotation.security.PermitAll

@Route
@PermitAll
class MainView(
    private val security: SecurityService,
    private val storage: JdbcStorageRepository
) : VerticalLayout() {

    private val logoutButton = Button("Logout") { security.logout() }

    private val orders: Grid<Order> = Grid(Order::class.java)

    private var filter:((Order)->Boolean)? = null

    private var dcs = listOf<Location>()

    init {
        setSizeFull()

        dcs = storage.findLocations()

        add(H2("Hello Vaadin"), logoutButton)

        orders.apply {

            removeAllColumns()

            addColumn("uid" ).apply {
                setHeader("OPERATORE")
            }

            addColumn( LitRenderer
                .of<Order?>("\${item.d}")
                .withProperty("d") {
                    it.issuedate.format(DateTimeFormatter.ofPattern("dd/MM/yyy"))
                })
                .setHeader("DATE")

            addColumn(LitRenderer
                .of<Order?>("\${item.optype}")
                .withProperty("optype") {
                    if(it.type == OrderType.LOAD) "CARICO" else "SCARICO"
                }
            ).setHeader("TIPO")

            addColumn(LitRenderer
                .of<Order?>("\${item.dcname}")
                .withProperty("dcname") {

                    val l = dcs.filter { dc ->
                        dc.name == it.location
                    }
                    l[0].name
                }
            ).setHeader("MAGAZZINO")

            addColumn(
                LitRenderer
                    .of<Order?>("\${item.rep}")
                    .withProperty("rep") {
                        it.rep ?: it.partner ?: "POSTE ITALIANE S.P.A."
                    }
            ).apply {
                setHeader("REFERENTE/FORNITORE")
            }

            addColumn(
                LitRenderer
                    .of<Order?>("\${item.docnum}")
                    .withProperty("docnum") {
                        if(it.subject == OrderSubject.INTERNAL) "USO INTERNO" else it.document?.numdoc
                    }
            ).apply {
                setHeader("NUMERO")
            }

            addColumn( LitRenderer
                .of<Order?>("\${item.d}")
                .withProperty("d") {
                    it.issuedoc?.format(DateTimeFormatter.ofPattern("dd/MM/yyy"))
                })
                .setHeader("DATE")

            setItems { query ->
                storage.findOrders(filter, query.offset, query.limit).stream()
            }

            setSizeFull()
        }

        val form = Mainform(storage=storage)
        form.addChangeListener {
            Notification.show(form.value.map { it.code }.joinToString("."))
        }
        add( form )
        add(orders)

    }

}