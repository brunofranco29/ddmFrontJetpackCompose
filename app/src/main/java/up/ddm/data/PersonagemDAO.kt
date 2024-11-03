package up.ddm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import up.ddm.Personagem

@Dao
interface PersonagemDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(personagem: Personagem)

    @Update
    suspend fun update(personagem: Personagem)

    @Delete
    suspend fun delete(personagem: Personagem)

    @Query("DELETE FROM personagem_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM personagem_table ORDER BY id ASC")
    fun getAllPersonagens(): Flow<List<Personagem>>

    @Query("SELECT * from personagem_table WHERE id = :id")
    fun getPersonagem(id: Int): Flow<Personagem>
}