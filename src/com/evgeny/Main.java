package com.evgeny;

import control.GamePlay;
import gui.SwingGUI;

public class Main {

    public static void main(String[] args) {
        GamePlay gamePlay;
        if (args.length==1){
            if (args[0].equals("easy")){
                gamePlay = new GamePlay(9,9,10);
            }else if (args[0].equals("hard")){
                gamePlay = new GamePlay(16,30,99);
            }else {
                gamePlay = new GamePlay(16, 16, 40);
            }
        }else {
            gamePlay = new GamePlay(16,16,40);
        }
        SwingGUI swingGUI = new SwingGUI(gamePlay);
        gamePlay.setGameGUI(swingGUI);
    }
}
