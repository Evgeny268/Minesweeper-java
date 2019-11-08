package gameLogic;

public class FieldCell {
    protected boolean opened = false;
    protected boolean mined = false;
    protected boolean flagged = false;
    protected int minesAround = 0;

    public FieldCell(boolean mined) {
        this.mined = mined;
    }

    public boolean isOpened() {
        return opened;
    }

    public void openCell(){
        opened = true;
    }

    public boolean isMined() {
        return mined;
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
