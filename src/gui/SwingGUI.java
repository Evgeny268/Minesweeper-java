package gui;

import control.GameGUI;
import control.OnFieldClick;
import gameLogic.FieldCell;
import gameLogic.Minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

/**
 * GUI интерфейс для игры, используется Swing
 */
public class SwingGUI extends JFrame implements GameGUI {

    private OnFieldClick onFieldClick; //Слушатель нажатия на поле
    private JPanel panel;
    private JLabel statusLabel;
    private int IMAGE_SIZE = 30;//Размер ячейки поля
    private int COLS;
    private int ROWS;
    private boolean firstDraw = true;
    private Minesweeper minesweeper;

    public SwingGUI(OnFieldClick onFieldClick){
        this.onFieldClick = onFieldClick;
    }

    private void initPanel(){
        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                FieldCell[][]field = minesweeper.getField();
                for (int i = 0; i < ROWS; i++) {
                    for (int j = 0; j < COLS; j++) {
                        FieldCell currentCell = field[i][j];
                        Image cellImage = null;
                        if (currentCell.isOpened()){
                            if (currentCell.isMined()){
                                cellImage = getImage(ImageType.BOMBED);
                            }else {
                                switch (currentCell.getMinesAround()) {
                                    case 0:
                                        cellImage = getImage(ImageType.ZERO);
                                        break;
                                    case 1:
                                        cellImage = getImage(ImageType.NUM1);
                                        break;
                                    case 2:
                                        cellImage = getImage(ImageType.NUM2);
                                        break;
                                    case 3:
                                        cellImage = getImage(ImageType.NUM3);
                                        break;
                                    case 4:
                                        cellImage = getImage(ImageType.NUM4);
                                        break;
                                    case 5:
                                        cellImage = getImage(ImageType.NUM5);
                                        break;
                                    case 6:
                                        cellImage = getImage(ImageType.NUM6);
                                        break;
                                    case 7:
                                        cellImage = getImage(ImageType.NUM7);
                                        break;
                                    case 8:
                                        cellImage = getImage(ImageType.NUM8);
                                        break;
                                }
                            }
                        }else {
                            if (!minesweeper.isGameOver()) {
                                if (currentCell.isFlagged()) {
                                    cellImage = getImage(ImageType.FLAGED);
                                } else {
                                    cellImage = getImage(ImageType.CLOSED);
                                }
                            }else {
                                if (currentCell.isMined()){
                                    if (currentCell.isFlagged()){
                                        cellImage = getImage(ImageType.FLAGED);
                                    }else {
                                        cellImage = getImage(ImageType.BOMB);
                                    }
                                }else {
                                    if (currentCell.isFlagged()){
                                        cellImage = getImage(ImageType.NOBOMB);
                                    }else {
                                        cellImage = getImage(ImageType.CLOSED);
                                    }
                                }
                            }
                        }
                        g.drawImage(cellImage,j*IMAGE_SIZE,i*IMAGE_SIZE,this);
                    }
                }
            }
        };
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                int posX = e.getY() / IMAGE_SIZE;
                int posY = e.getX() / IMAGE_SIZE;
                if ((posX<0 || posX >= minesweeper.getFieldSizeX()) || (posY<0 || posY>=minesweeper.getFieldSizeY())) return;
                if (e.getButton() == MouseEvent.BUTTON1) //Левая клавига мыши
                    onFieldClick.onCellClick(posX,posY);
                if (e.getButton() == MouseEvent.BUTTON3){ //Правая клавиша мыши
                    onFieldClick.onCellSetFlag(posX,posY);
                }
            }
        });
        panel.setPreferredSize(new Dimension(COLS*IMAGE_SIZE, ROWS*IMAGE_SIZE));
        add(panel);
    }

    private void initStatusLabel(){
        statusLabel = new JLabel("Осталось отметить бомб: "+(minesweeper.getMinesCount()-minesweeper.getFlagsCount()));
        add(statusLabel,BorderLayout.NORTH);
    }

    private void initFrame(){
        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Mineswapper");
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    @Override
    public void drawGame(Minesweeper minesweeper) {
        this.minesweeper = minesweeper;
        if (firstDraw){
            ROWS = minesweeper.getFieldSizeX();
            COLS = minesweeper.getFieldSizeY();
            initPanel();
            initStatusLabel();
            initFrame();
            firstDraw = false;
        }
        panel.repaint();
        if (minesweeper.isGameOver()){
            statusLabel.setText("Игра завершена");
        }else {
            statusLabel.setText("Осталось отметить бомб: "+(minesweeper.getMinesCount()-minesweeper.getFlagsCount()));
        }
    }

    private Image getImage(ImageType type){
        String fName = "/img/";
        switch (type){
            case BOMB:
                fName+="bomb.png";
                break;
            case BOMBED:
                fName+="bombed.png";
                break;
            case CLOSED:
                fName+="closed.png";
                break;
            case FLAGED:
                fName+="flaged.png";
                break;
            case NOBOMB:
                fName+="nobomb.png";
                break;
            case NUM1:
                fName+="num1.png";
                break;
            case NUM2:
                fName+="num2.png";
                break;
            case NUM3:
                fName+="num3.png";
                break;
            case NUM4:
                fName+="num4.png";
                break;
            case NUM5:
                fName+="num5.png";
                break;
            case NUM6:
                fName+="num6.png";
                break;
            case NUM7:
                fName+="num7.png";
                break;
            case NUM8:
                fName+="num8.png";
                break;
            case OPENED:
                fName+="opened.png";
                break;
            case ZERO:
                fName+="zero.png";
                break;
        }
        ImageIcon icon = new ImageIcon(getClass().getResource(fName));
        //return icon.getImage();
        return createResizedCopy(icon.getImage(),IMAGE_SIZE,IMAGE_SIZE,false);
    }

    BufferedImage createResizedCopy(Image originalImage,
                                    int scaledWidth, int scaledHeight,
                                    boolean preserveAlpha)
    {
        int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
        BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
        Graphics2D g = scaledBI.createGraphics();
        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
        g.dispose();
        return scaledBI;
    }

    public enum ImageType{
        BOMB,
        BOMBED,
        CLOSED,
        FLAGED,
        NOBOMB,
        NUM1,
        NUM2,
        NUM3,
        NUM4,
        NUM5,
        NUM6,
        NUM7,
        NUM8,
        OPENED,
        ZERO
    }
}
