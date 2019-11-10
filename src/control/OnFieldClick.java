package control;

/**
 * Интерфейс для слушателя, который определяет нажатие на поле
 */
public interface OnFieldClick {

    void onCellClick(int posX, int posY); //Открытие ячейки(нажатие ЛКМ)

    void onCellSetFlag(int posX, int posY); //Установка флага(нажатие ПКМ)
}
