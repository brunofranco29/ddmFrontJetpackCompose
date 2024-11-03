package up.ddm

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "personagem_table")
data class Personagem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val forcaFinal: Int,
    val destrezaFinal: Int,
    val constituicaoFinal: Int,
    val sabedoriaFinal: Int,
    val inteligenciaFinal: Int,
    val carismaFinal: Int,
    val vida: Int,
    val raca: Raca
)