package am.audio.game.common.common_domain

enum class GameMode(
    val height: Int,
    val width: Int,
    val bombQuantity: Int,
) {
    Easy(3, 3, 1),
    Medium(4, 5, 2),
    Hard(8, 5, 3);
}
