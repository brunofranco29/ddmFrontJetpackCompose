package up.ddm

import androidx.room.Entity
import androidx.room.PrimaryKey

import kotlin.properties.Delegates

class Atributos {
    // Atributos base (sem bônus)
    var forca: Int = 8
    var destreza: Int = 8
    var constituicao: Int by Delegates.observable(8) { _, old, new ->
        if (old != new) {
            calcularVida()
        }
    }
    var sabedoria: Int = 8
    var inteligencia: Int = 8
    var carisma: Int = 8
    var vida: Int = 0

    class AtributosBonus(
        val bonusForca: Int = 0,
        val bonusDestreza: Int = 0,
        val bonusConstituicao: Int = 0,
        val bonusSabedoria: Int = 0,
        val bonusInteligencia: Int = 0,
        val bonusCarisma: Int = 0
    )

    private val healthStrategy: HealthStrategy = DefaultHealthStrategy()

    // Função para calcular a vida
    fun calcularVida() {
        vida = healthStrategy.calculateHealth(constituicao)
    }

    // Método para definir o valor de um atributo individual
    fun setAtributo(nome: String, valor: Int) {
        if (valor < 8 || valor > 15) throw IllegalArgumentException("Valor deve estar entre 8 e 15")
        when (nome) {
            "Força" -> forca = valor
            "Destreza" -> destreza = valor
            "Constituição" -> constituicao = valor
            "Sabedoria" -> sabedoria = valor
            "Inteligência" -> inteligencia = valor
            "Carisma" -> carisma = valor
            else -> throw IllegalArgumentException("Atributo inválido")
        }
    }

    var atributosBonus: AtributosBonus = AtributosBonus()

    // Função para calcular o atributo final somando o bônus de raça
    fun calcularAtributoFinal(nome: String): Int {
        return when (nome) {
            "Força" -> forca + atributosBonus.bonusForca
            "Destreza" -> destreza + atributosBonus.bonusDestreza
            "Constituição" -> constituicao + atributosBonus.bonusConstituicao
            "Sabedoria" -> sabedoria + atributosBonus.bonusSabedoria
            "Inteligência" -> inteligencia + atributosBonus.bonusInteligencia
            "Carisma" -> carisma + atributosBonus.bonusCarisma
            else -> throw IllegalArgumentException("Atributo inválido")
        }
    }

    fun aplicarBonusRacial(raca: Raca, atributo: String): Int {
        atributosBonus = AtributosBonus(
            bonusForca = raca.bonusForca,
            bonusDestreza = raca.bonusDestreza,
            bonusConstituicao = raca.bonusConstituicao,
            bonusSabedoria = raca.bonusSabedoria,
            bonusInteligencia = raca.bonusInteligencia,
            bonusCarisma = raca.bonusCarisma
        )
        // Return the bonus for the specific attribute
        return when (atributo) {
            "Força" -> atributosBonus.bonusForca
            "Destreza" -> atributosBonus.bonusDestreza
            "Constituição" -> atributosBonus.bonusConstituicao
            "Sabedoria" -> atributosBonus.bonusSabedoria
            "Inteligência" -> atributosBonus.bonusInteligencia
            "Carisma" -> atributosBonus.bonusCarisma
            else -> 0 // Or throw an exception if needed
        }
    }

    // Função para calcular os pontos restantes (considerando custos)
    var pontos = 27

    fun setAtributoComCusto(nome: String, valor: Int) {
        // Verifica se o novo valor está dentro dos limites
        if (valor < 8 || valor > 15) throw IllegalArgumentException("Valor deve estar entre 8 e 15")

        // Calcula o custo do valor atual e do novo
        val atributoAtual = when (nome) {
            "Força" -> forca
            "Destreza" -> destreza
            "Constituição" -> constituicao
            "Sabedoria" -> sabedoria
            "Inteligência" -> inteligencia
            "Carisma" -> carisma
            else -> throw IllegalArgumentException("Atributo inválido")
        }

        val custoAtual = calcularCusto(atributoAtual)
        val custoNovo = calcularCusto(valor)

        // Calcula a diferença de custo
        val diferencaDeCusto = custoNovo - custoAtual

        // Verifica se há pontos suficientes para aplicar a nova configuração
        if (pontos >= diferencaDeCusto) {
            // Aplica o novo valor
            setAtributo(nome, valor)
            pontos -= diferencaDeCusto // Atualiza os pontos disponíveis
        } else {
            throw IllegalArgumentException("Pontos insuficientes")
        }
    }

    // Função para calcular o custo de acordo com o valor do atributo
    fun calcularCusto(valor: Int): Int {
        return when (valor) {
            8 -> 0
            9 -> 1
            10 -> 2
            11 -> 3
            12 -> 4
            13 -> 5
            14 -> 7
            15 -> 9
            else -> throw IllegalArgumentException("Valor fora do intervalo permitido")
        }
    }

    // Função para calcular o total de pontos gastos com base nos valores atuais dos atributos
    fun calcularPontosGastos(): Int {
        return calcularCusto(forca) + calcularCusto(destreza) +
                calcularCusto(constituicao) + calcularCusto(sabedoria) +
                calcularCusto(inteligencia) + calcularCusto(carisma)
    }

    fun getAtributoValor(nome: String): Int {
        return when (nome) {
            "Força" -> forca
            "Destreza" -> destreza
            "Constituição" -> constituicao
            "Sabedoria" -> sabedoria
            "Inteligência" -> inteligencia
            "Carisma" -> carisma
            else -> throw IllegalArgumentException("Atributo inválido")
        }
    }
}
