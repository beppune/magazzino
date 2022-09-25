package it.posteitaliane.gdc.magazzino.view.main

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.html.H2
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.LitRenderer
import com.vaadin.flow.router.Route
import com.vaadin.flow.server.auth.AnonymousAllowed
import com.vaadin.flow.theme.lumo.LumoUtility
import it.posteitaliane.gdc.magazzino.adapters.storage.JdbcStorageRepository
import it.posteitaliane.gdc.magazzino.core.*
import it.posteitaliane.gdc.magazzino.core.services.FrontOffice
import it.posteitaliane.gdc.magazzino.security.SecurityService
import it.posteitaliane.gdc.magazzino.view.forms.OrderForm
import it.posteitaliane.gdc.magazzino.view.forms.fields.DateRangeField
import it.posteitaliane.gdc.magazzino.view.forms.fields.DcSelect
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.annotation.security.PermitAll

@Route
@PermitAll
@AnonymousAllowed
class MainView(
    private val security: SecurityService,
    private val storage: JdbcStorageRepository,
    private val office: FrontOffice
) : VerticalLayout() {

    private val logoutButton = Button("Logout") { security.logout() }

    private val orders: Grid<Order> = Grid(Order::class.java)

    private var filter:((Order)->Boolean)? = null

    private var form:OrderForm?=null

    init {

        setSizeFull()

        add(H2("Hello Vaadin"), logoutButton)

        orders.apply {

            removeAllColumns()

            addColumn("uid" ).apply {
                setHeader("OPERATORE")
            }

            addColumn("id").setHeader("DOCID")

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

                    storage.findLocations().find {
                        dc -> dc.name == it.location.name
                    }?.altname ?: it.location
                }
            ).setHeader("MAGAZZINO")

            //addColumn("location").setHeader("MAGAZZINO")

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

        val period = DateRangeField("DATA ORDINE")
        period.addValueChangeListener {
            Notification.show(it.value.toString())
        }

        val dcselect = DcSelect(storage.findLocations())
        dcselect.addValueChangeListener {
            Notification.show(dcselect.value.map(Location::altname).toString())
        }

        val order = MutableOrder(
            Operator("MANZOGI9", Area("TORINO", storage.findLocations("TORINO")), Permission.WRITE),
            LocalDateTime.now(),
            storage.findLocations().find { it.code == "TOR" } as Location,
            OrderType.LOAD,
            OrderSubject.INTERNAL
        )

        add(OrderForm(storage.findUsers(), order))

    }

}