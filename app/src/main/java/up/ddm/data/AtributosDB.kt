package up.ddm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.CoroutineScope
import up.ddm.Atributos
import up.ddm.Personagem


@Database(entities = [Personagem::class], version = 1, exportSchema = false)
abstract class AtributosDB : RoomDatabase() {

    abstract fun personagemDAO(): PersonagemDAO

    companion object {
        @Volatile
        private var INSTANCE: AtributosDB? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AtributosDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AtributosDB::class.java,
                    "atributos_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}