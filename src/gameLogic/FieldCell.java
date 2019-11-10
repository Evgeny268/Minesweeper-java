package gameLogic;

/**
 *  Данный класс реализует ячейку минного поля
 *  {@link #opened} - открытая ячейка
 *  {@link #mined} - заминированная ячейка
 *  {@link #flagged} - ячейка, помеченная флагом
 *  {@link #minesAround} - число, которое показывает, сколько мин находится вокруг этой ячейки
 */
public class FieldCell {
    private boolean opened = false;
    private boolean mined = false;
    private boolean flagged = false;
    private int minesAround = 0;


    public FieldCell() {}

    public boolean isOpened() {
        return opened;
    }

    public void openCell(){
        opened = true;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(boolean mined) {
        this.mined = mined;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    public int getMinesAround() {
        return minesAround;
    }

    public void setMinesAround(int minesAround) {
        this.minesAround = minesAround;
    }
}
