/*
 *  Brick Destroy - A simple Arcade video game
 *   Copyright (C) 2017  Filippo Ranza
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package test;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

/**This class controls the game frame
 * @author Sabrina Felicia Kusumawati
 * @version 0.2
 */
public class GameFrame extends JFrame implements WindowFocusListener {

    /**
     * Contains the text for the title on the window
     */
    private static final String DEF_TITLE = "Brick Destroy";

    private GameBoard gameBoard;
    private HomeMenu homeMenu;
    private Information information;
    private Leaderboard leaderboard;
    private GameOver gameOver;

    //Audio taken from https://www.youtube.com/watch?v=j29TCZYJgWA - Game Over by MB Music
    private AudioPlayer gameOverAudio = new AudioPlayer("audio/bgm-game-over.wav");
    //Audio taken from https://www.youtube.com/watch?v=br3OzOrARh4 - 8-bit Win by Heatley Bros
    private AudioPlayer winAudio = new AudioPlayer("audio/bgm-win.wav");

    private boolean gaming;

    private int score;
    private int[] leaderBoardScore={1,2,3,4,5};
    private String[] leaderBoardName={"a","a","a","a","a"};

    /**
     * Generates game frame
     */
    public GameFrame(){
        super();

        gaming = false;

        this.setLayout(new BorderLayout());

        gameBoard = new GameBoard(this);

        homeMenu = new HomeMenu(this,new Dimension(450,300));

        information = new Information(this,new Dimension(450,300));

        leaderboard = new Leaderboard(this,new Dimension(450,300));

        gameOver = new GameOver(this, new Dimension(450, 300));

        this.add(homeMenu,BorderLayout.CENTER);

        this.setUndecorated(true);
    }

    /**
     * Initializes GameFrame
     */
    public void initialize(){
        this.setTitle(DEF_TITLE);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
        this.autoLocate();
        this.setVisible(true);
    }

    /** gets Leaderboard name list in string array format
     * @return returns Leaderboard name list in string array format
     */
    public String[] getLeaderBoardName() {return leaderBoardName;}
    /** gets Leaderboard score list in integer array format
     * @return returns Leaderboard score list in integer array format
     */
    public int[] getLeaderBoardScore() {return leaderBoardScore;}

    /** Reads leaderboard scores and names from text file
     * gameframe Leaderboard name and score list in their respective arrays
     */
    public void ReadFile(){
        try{
            File leaderboardScore = new File("src/test/leaderboardscore.txt");
            File leaderboardName = new File("src/test/leaderboardname.txt");
            Scanner scanner1 = new Scanner(leaderboardScore);
            Scanner scanner2 = new Scanner(leaderboardName);
            int x =0;
            while (scanner1.hasNextLine()) {
                leaderBoardScore[x]= scanner1.nextInt();
                leaderBoardName[x++]= scanner2.nextLine();
            }
            scanner1.close();
            scanner2.close();
        }
        catch(FileNotFoundException e)
        {
            System.out.println("txt files failed to open");
            e.printStackTrace();
        }
    }

    /** updates leaderboard name and score list txts
     * @param score contains leaderboard score array
     * @param name contains leaderboard name array
     */
    public void WriteFile(int[] score, String[] name){
        try {
            System.out.println("Save Succeded");
            FileWriter leaderboardScore = new FileWriter("src/test/leaderboardscore.txt",false);
            FileWriter leaderboardName = new FileWriter("src/test/leaderboardname.txt",false);
            int x=0;
            while (x<4) {
                leaderboardScore.write(String.format("%d\n", score[x]));
                leaderboardName.write(String.format("%s\n", name[x++]));
            }
            leaderboardScore.write(String.format("%d", score[4]));
            leaderboardName.write(String.format("%s", name[4]));
            leaderboardScore.close();
            leaderboardName.close();
        }
        catch (IOException e)
        {
            System.out.println("An error occured");
            e.printStackTrace();
        }
    }


    /**
     * enables homemenu
     */
    public void enableHomeMenu(){
        gameOverAudio.stop();
        this.dispose();
        this.remove(gameOver);
        this.add(homeMenu,BorderLayout.CENTER);
        this.setUndecorated(true);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
        gameBoard.muteAudio(homeMenu.getMute());
    }

    /**
     * enables gameboard
     */
    public void enableGameBoard(){
        winAudio.stop();
        gameOverAudio.stop();
        this.dispose();
        this.remove(homeMenu);
        this.add(gameBoard,BorderLayout.CENTER);
        this.setUndecorated(false);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
        gameBoard.muteAudio(homeMenu.getMute());
    }

    /**
     * disables gameboard
     */
    public void disableGameBoard(){
        this.dispose();
        this.remove(gameBoard);
        this.add(homeMenu,BorderLayout.CENTER);
        this.setUndecorated(true);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
        homeMenu.setMute(gameBoard.getMute());
    }

    /**
     *enables information screen
     */
    public void enableInformation(){
        this.dispose();
        this.remove(homeMenu);
        this.add(information,BorderLayout.CENTER);
        this.setUndecorated(true);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
    }
    /**
     *disables information screen
     */
    public void disableInformation(){
        this.dispose();
        this.remove(information);
        this.add(homeMenu,BorderLayout.CENTER);
        this.setUndecorated(true);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
    }

    /**
     *enables gameover screen
     * @param win if win is true then win audio will be played instead of the gameover audio
     */
    public void enableGameOver(boolean win){
        ReadFile();
        if (!gameBoard.getMute()){
            if (win){
                winAudio.play();
                winAudio.loop();
            }
            else
            {
                gameOverAudio.play();
                gameOverAudio.loop();
            }
        }
        this.dispose();
        this.remove(gameBoard);
        this.add(gameOver,BorderLayout.CENTER);
        this.setUndecorated(true);
        initialize();
        /*to avoid problems with graphics focus controller is added here*/
        this.addWindowFocusListener(this);
    }

    /**
     * disables gameover screen
     */
    public void disableGameOver(){
        this.dispose();
        this.remove(gameOver);
    }

    /**
     * enables leaderboad screen
     * @param fromTutorial if fromtutorial is true then gameover will be disabled
     */
    public void enableLeaderboard(boolean fromTutorial){
        ReadFile();
        leaderboard.setSCORE(leaderBoardScore);
        leaderboard.setNAME(leaderBoardName);

        if (!fromTutorial)
        {
            this.dispose();
            this.remove(gameOver);
            this.add(leaderboard,BorderLayout.CENTER);
            this.setUndecorated(true);
            initialize();
            /*to avoid problems with graphics focus controller is added here*/
            this.addWindowFocusListener(this);
        }
        else {
            this.dispose();
            this.remove(information);
            this.add(leaderboard,BorderLayout.CENTER);
            leaderboard.setFromInformationl(false);
            this.setUndecorated(true);
            initialize();
            /*to avoid problems with graphics focus controller is added here*/
            this.addWindowFocusListener(this);
        }
    }

    /** disables leaderboard screen
     * @param fromTutorial if fromtutorial is true then information will be disabled
     */
    public void disableLeaderboard(boolean fromTutorial){
        if (fromTutorial)
        {
            this.dispose();
            this.remove(leaderboard);
            this.add(information,BorderLayout.CENTER);
            this.setUndecorated(true);
            initialize();
            /*to avoid problems with graphics focus controller is added here*/
            this.addWindowFocusListener(this);
        }
        else {
            this.dispose();
            this.remove(leaderboard);
            this.add(homeMenu,BorderLayout.CENTER);
            this.setUndecorated(true);
            initialize();
            /*to avoid problems with graphics focus controller is added here*/
            this.addWindowFocusListener(this);
            winAudio.stop();
            gameOverAudio.stop();
            homeMenu.setMute(gameBoard.getMute());
        }
    }


    /** sets score according to parametes
     * @param score contains score
     */
    public void setScore(int score) {this.score = score;}


    /** gets score
     * @return returns score
     */
    public int getScore(){return score;}

    /**
     * sets position of windows
     */
    private void autoLocate(){
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (size.width - this.getWidth()) / 2;
        int y = (size.height - this.getHeight()) / 2;
        this.setLocation(x,y);
    }


    @Override
    public void windowGainedFocus(WindowEvent windowEvent) {
        /*
            the first time the frame loses focus is because
            it has been disposed to install the GameBoard,
            so went it regains the focus it's ready to play.
            of course calling a method such as 'onLostFocus'
            is useful only if the GameBoard as been displayed
            at least once
         */
        gaming = true;
    }

    @Override
    public void windowLostFocus(WindowEvent windowEvent) {
        if(gaming)
            gameBoard.onLostFocus();

    }
}
