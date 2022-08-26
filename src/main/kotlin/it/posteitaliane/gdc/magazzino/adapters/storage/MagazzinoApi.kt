package it.posteitaliane.gdc.magazzino.adapters.storage

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.SqlOutParameter
import org.springframework.jdbc.core.SqlParameter
import org.springframework.jdbc.core.simple.SimpleJdbcCall
import java.sql.Types

typealias ParamCall = MagazzinoApi.()->Unit

data class MagazzinoApiResponse(val stato:Int, val mess:String, val codiceDoc:String?=null)

class MagazzinoApi(template:JdbcTemplate) :SimpleJdbcCall(template) {

    var beforeCall:ParamCall? = null

    var betweenCall:ParamCall? = null

    var afterCall:ParamCall? = null

    var uid:String? = null

    fun inParam(name: String, type: Int = Types.VARCHAR) = declareParameters(SqlParameter(name, type))

    fun outParam(name: String, type: Int = Types.VARCHAR) = declareParameters(SqlOutParameter(name, type))

    fun withUid(u:String): MagazzinoApi {
        uid = u
        return this
    }

    fun before(call:ParamCall): MagazzinoApi {
        beforeCall = call
        return this
    }

    fun after(call:ParamCall): MagazzinoApi {
        afterCall = call
        return this
    }

    fun config(call:ParamCall?=null): MagazzinoApi {
        betweenCall = call

        beforeCall?.invoke(this)
        betweenCall?.invoke(this)
        afterCall?.invoke(this)

        outParam("stato", Types.INTEGER)
        outParam("mess", Types.VARCHAR)

        return this
    }

    operator fun invoke(uid:String, vararg args:Any?): MagazzinoApiResponse {
        val res = execute(uid, *args)
        return MagazzinoApiResponse(res["stato"] as Int, res["mess"] as String, res.get("codiceDocumento") as String?)
    }

    fun build(procname:String): MagazzinoApi {
        return MagazzinoApi(jdbcTemplate)
            .apply {
                before {
                    withProcedureName(procname)
                    inParam("ParamUserId", Types.VARCHAR)
                }
            }
    }
}

