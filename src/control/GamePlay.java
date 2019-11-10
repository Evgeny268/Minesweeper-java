package control;

import gameLogic.Minesweeper;

public class GamePlay implements OnFieldClick {
    protected GameGUI gameGUI = null;
    protected Minesweeper minesweeper;


    public GamePlay(int fieldSizeX, int fieldSizeY, int minesCount) {
        minesweeper = new Minesweeper(fieldSizeX, fieldSizeY, minesCount);
    }

    public void setGameGUI(GameGUI gameGUI) {
        this.gameGUI = gameGUI;
        gameGUI.drawGame(minesweeper);
    }

    @Override
    public void onCellClick(int posX, int posY) {
        minesweeper.clickOnCell(posX,posY);
        gameGUI.drawGame(minesweeper);
    }

    @Override
    public void onCellSetFlag(int posX, int posY) {
        minesweeper.setFlag(posX,posY);
        gameGUI.drawGame(minesweeper);
    }
}
