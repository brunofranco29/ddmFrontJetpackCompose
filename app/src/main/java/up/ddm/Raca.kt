package up.ddm

enum class Raca(
    val bonusForca: Int = 0,
    val bonusDestreza: Int = 0,
    val bonusConstituicao: Int = 0,
    val bonusSabedoria: Int = 0,
    val bonusInteligencia: Int = 0,
    val bonusCarisma: Int = 0
) {
    MOUNTAIN_DWARF(bonusForca = 2, bonusConstituicao = 2),
    HILL_DWARF(bonusConstituicao = 2),
    HIGH_ELF(bonusInteligencia = 1, bonusDestreza = 2),
    WOOD_ELF(bonusDestreza = 2, bonusSabedoria = 1),
    DROW(bonusInteligencia = 1, bonusCarisma = 1),
    FOREST_GNOME(bonusInteligencia = 1, bonusDestreza = 1),
    ROCK_GNOME(bonusInteligencia = 2),
    STOUT_HALFLING(bonusDestreza = 2, bonusConstituicao = 1),
    LIGHTFOOT_HALFLING(bonusDestreza = 2),
    DWARF(bonusConstituicao = 2),
    ELF(bonusDestreza = 2),
    HALFLING(bonusDestreza = 2),
    GNOME(bonusInteligencia = 2),
    DRAGONBORN(bonusForca = 2, bonusCarisma = 1),
    HALF_ORC(bonusForca = 2, bonusConstituicao = 1),
    TIEFLING(bonusInteligencia = 1, bonusCarisma = 2),
    HALF_ELF(bonusCarisma = 2, bonusInteligencia = 1),
    HUMAN(bonusForca = 1, bonusDestreza = 1, bonusConstituicao = 1, bonusSabedoria = 1, bonusInteligencia = 1, bonusCarisma = 1);
}