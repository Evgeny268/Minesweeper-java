package gameLogic;

import java.util.ArrayList;
import java.util.Random;

/**
 * Данный класс реализует функциональность игрового минного поля
 * {@link #field} - массив, хранящий ячейки поля
 * @see FieldCell - ячейка поля
 * {@link #fieldSizeX} - размер поля по оси X
 * {@link #fieldSizeY} - размер поля по оси Y
 * {@link #minesCount} - количество мин на поле
 * {@link #flagsCount} - количество установленных флагов
 * {@link #gameOver} - статус окончания игры
 */
public class Minesweeper {
    protected FieldCell [][]field;
    protected int fieldSizeX = 9;
    protected int fieldSizeY = 9;
    protected int minesCount;
    protected int flagsCount = 0;
    protected boolean firstClick = true;
    protected boolean gameOver = false;

    /**
     *
     * @param fieldSizeX - размер поля по оси X
     * @param fieldSizeY - размер поля по оси Y
     * @param minesCount - количество мин, которое необходимо сгенерировать на случайных ячейках поля
     */
    public Minesweeper(int fieldSizeX, int fieldSizeY, int minesCount) {
        this.fieldSizeX = fieldSizeX;
        this.fieldSizeY = fieldSizeY;
        if (minesCount>0 && minesCount<fieldSizeX*fieldSizeY) {
            this.minesCount = minesCount;
        }else {
            this.minesCount = (fieldSizeX*fieldSizeY)/4;
        }
        field = new FieldCell[fieldSizeX][fieldSizeY];
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                field[i][j] = new FieldCell();
            }
        }
    }

    /**
     * Клик по ячейке поля
     * @param posX - позиция ячейки по оси X
     * @param posY - позиция ячейки по оси Y
     */
    public void clickOnCell(int posX, int posY){
        if ((posX<0 || posX>=fieldSizeX) || (posY<0 || posY>=fieldSizeY)) return;
        if (gameOver || field[posX][posY].isFlagged()|| field[posX][posY].isOpened()) return;
        if (firstClick){
            generateMinedField(posX,posY);
            makeCountMinesAround();
            firstClick = false;
        }else {
            field[posX][posY].openCell();
            if (field[posX][posY].isMined()){
                gameOver = true;
                return;
            }
        }
        openCellsAround(posX,posY);
        checkGameOver();
    }

    /**
     * Установка или снятие флага на ячейку поля
     * @param posX - позиция ячейки по оси X
     * @param posY - позиция ячейки по оси Y
     */
    public void setFlag(int posX, int posY){
        if ((posX<0 || posX>=fieldSizeX) || (posY<0 || posY>=fieldSizeY)) return;
        if (gameOver || field[posX][posY].isOpened()) return;
        if (field[posX][posY].isFlagged()){
            field[posX][posY].setFlagged(false);
            flagsCount--;
        }else {
            field[posX][posY].setFlagged(true);
            flagsCount++;
        }
        checkGameOver();
    }

    public FieldCell[][] getField() {
        return field;
    }

    public int getFieldSizeX() {
        return fieldSizeX;
    }

    public int getFieldSizeY() {
        return fieldSizeY;
    }

    public int getMinesCount() {
        return minesCount;
    }

    public int getFlagsCount() {
        return flagsCount;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Генерация мин на поле в случайных местах (генерация происходит после первой открытой ячейки, в ней никогда не бывает мины)
     * @param startPosX - позиция первой открытой ячейки по оси X
     * @param startPosY - позиция первой открытой ячейки по оси Y
     */
    protected void generateMinedField(int startPosX, int startPosY){
        ArrayList<Integer> minePosition = new ArrayList<>();
        for (int i = 0; i < fieldSizeX * fieldSizeY; i++) {
            minePosition.add(i);
        }
        int tapCellPos = (startPosX*fieldSizeY)+startPosY;
        minePosition.remove(Integer.valueOf(tapCellPos));
        for (int i = 0; i < minesCount; i++) {
            Random rnd = new Random(System.currentTimeMillis());
            int deleteNumber = rnd.nextInt(minePosition.size());
            int posX = minePosition.get(deleteNumber)/fieldSizeY;
            int posY = minePosition.get(deleteNumber)-(posX*fieldSizeY);
            field[posX][posY].setMined(true);
            minePosition.remove(Integer.valueOf(minePosition.get(deleteNumber)));
        }
    }

    /**
     * Расчет числа, которое показывает, сколбько бомб окружает текущую ячейку (расчет для всех ячеек поля)
     */
    protected void makeCountMinesAround(){
        for (int i = 0; i < fieldSizeX; i++) { //два цикла по всем ячейкам поля
            for (int j = 0; j < fieldSizeY; j++) {
                int minesAround = 0;
                for (int k = i-1; k <= i+1; k++) { //ячейки вокруг текущей ячейки
                    for (int l = j-1; l <= j+1; l++) {
                        if ((k<0 || k>=fieldSizeX) || (l<0 || l>=fieldSizeY)) continue;
                        if (i==k && j==l) continue;
                        if (field[k][l].isMined())
                            minesAround++;
                    }
                }
                field[i][j].setMinesAround(minesAround);
            }
        }
    }


    /**
     * Проверка окончания игры (подрыв на мине, или отмечены все мины)
     */
    protected void checkGameOver(){
        if (flagsCount == minesCount){
            for (int i = 0; i < fieldSizeX; i++) {
                for (int j = 0; j < fieldSizeY; j++) {
                    if (!field[i][j].isMined()&&field[i][j].isFlagged()) return;
                    if (!field[i][j].isFlagged()&&!field[i][j].isOpened()) return;
                }
            }
        }else return;
        gameOver = true;
    }

    /**
     * Открытие ячеек вокруг текущей
     * @param posX - Позицця по оси X ячейки, по которой был произведен клик
     * @param posY - Позицця по оси Y ячейки, по которой был произведен клик
     */
    protected void openCellsAround(int posX, int posY){
        ArrayList<Pair> needOpen = new ArrayList<>();
        field[posX][posY].openCell();
        for (int i = posX-1; i <= posX+1; i++) {
            for (int j = posY-1; j <= posY+1; j++) {
                if ((i<0 || i>=fieldSizeX) || (j<0 || j>=fieldSizeY)) continue;
                if (!field[i][j].isOpened()){
                    if (!field[i][j].isMined() && !field[i][j].isFlagged()){
                        if (field[i][j].getMinesAround()==0){
                            needOpen.add(new Pair(i,j));
                        }else {
                            field[i][j].openCell();
                        }
                    }
                }
            }
        }
        for (int i = 0; i < needOpen.size(); i++) {
            openCellsAround(needOpen.get(i).first,needOpen.get(i).second);
        }
    }

    protected class Pair{
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}
