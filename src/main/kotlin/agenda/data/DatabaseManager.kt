package agenda.data

import agenda.data.table.*
import agenda.model.Group
import agenda.util.Directories
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import org.sqlite.JDBC
import org.sqlite.SQLiteDataSource
import java.sql.Connection
import javax.sql.DataSource

object DatabaseManager {

    @JvmStatic private val db: Database by lazy { Database.connect(dataSource()) }

    private fun dataSource(): DataSource {
        return SQLiteDataSource().apply {
            url = JDBC.PREFIX + Directories.databaseFile
            setEnforceForeignKeys(true)
        }
    }

    @JvmStatic internal fun <T> dbQuery(query: Transaction.() -> T): T = transaction(Connection.TRANSACTION_SERIALIZABLE, 1, db, query)

    @JvmStatic fun setup() {
        dbQuery {
            val firstTime = !Groups.exists()

            create(
                Phones,
                Emails,
                Groups,
                Contacts,
                ContactPhones,
                ContactEmails,
                ContactGroups
            )

            if (firstTime) Groups.batchInsert(Group.defaults.map { Group.default(it) }) { group -> this[Groups.name] = group.name }
        }
    }
}
