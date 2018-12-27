package agenda.data

import agenda.data.table.*
import agenda.util.Directories
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import org.sqlite.JDBC
import org.sqlite.SQLiteDataSource
import java.sql.Connection
import javax.sql.DataSource

object DatabaseManager {

    private val db: Database by lazy { Database.connect(dataSource()) }

    private fun dataSource(): DataSource {
        return SQLiteDataSource().apply {
            url = JDBC.PREFIX + Directories.databaseFile
            setEnforceForeignKeys(true)
        }
    }

    internal fun <T> dbQuery(query: Transaction.() -> T): T = transaction(Connection.TRANSACTION_SERIALIZABLE, 2, db, query)

    init {
        dbQuery {
            create(
                Phones,
                Emails,
                Groups,
                Contacts,
                ContactPhones,
                ContactEmails,
                ContactGroups
            )
        }
    }
}
