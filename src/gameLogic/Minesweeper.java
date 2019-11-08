package gameLogic;

import java.util.ArrayList;
import java.util.Random;

public class Minesweeper {
    protected FieldCell [][]field;
    protected int fieldSizeX = 9;
    protected int fieldSizeY = 9;
    private int minesCount;
    private int flagsCount = 0;
    private boolean firstClick = true;
    private boolean gameOver = false;

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

    protected void openCellsAround(int posX, int posY){
        ArrayList<Pair> needOpen = new ArrayList<>();
        for (int i = 0; i < fieldSizeX; i++) {
            for (int j = 0; j < fieldSizeY; j++) {
                for (int k = i-1; k <= i+1; k++) {
                    for (int l = j-1; l <= j+1; l++){
                        if ((k<0 || k>=fieldSizeX) || (l<0 || l>=fieldSizeY)) continue;
                        if (i==k && j==l) continue;
                        if (!field[k][l].isOpened()){
                            field[k][l].openCell();
                            needOpen.add(new Pair(k,l));
                        }
                    }
                }
            }
        }
        for (int i = 0; i < needOpen.size(); i++) {
            openCellsAround(needOpen.get(i).first,needOpen.get(i).second);
        }
    }

    private class Pair{
        int first;
        int second;

        public Pair(int first, int second) {
            this.first = first;
            this.second = second;
        }
    }
}
