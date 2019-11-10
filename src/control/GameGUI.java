package control;
/**
 * Любой класс, который будет заниматься отрисовкой игры, должен имплементировать данный интерфейс
 */

import gameLogic.Minesweeper;

public interface GameGUI {

    void drawGame(Minesweeper minesweeper); //Отрисовка игрового поля
}
