package com.company;

import org.junit.Assert;
import org.junit.Before;

/**
 * Created with IntelliJ IDEA.
 * User: Rachel
 * Date: 12/9/13
 * Time: 8:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class GameTest {

    private int number = 4;
    private Game game;

    @Before
    public void startUp() {
       game = new Game();

    }

    @org.junit.Test
    public void checkConstructor(){
        int number = game.getNumber();
        Assert.assertEquals(0,number);

        int guess = game.getGuess();
        Assert.assertEquals(0,guess);

        int guesses = game.getGuesses();
        Assert.assertEquals(0,guesses);

    }


    @org.junit.Test
    public void testCheckGuess() throws Exception {
        int guess = 0;
        String result = game.checkGuess(guess);
        Assert.assertEquals("correct", result);


    }

    @org.junit.Test
    public void testCheckGuessIncorrectInput() throws Exception {
//        try{
        String guess = null;
        Integer newNum = Integer.parseInt(guess);
        String result = game.checkGuess(newNum);
        Assert.assertEquals("Please enter the correct format", result);
//        }
//        catch (Exception e){
//            System.out.println("Please enter the correct format");
//            System.out.println("The exception has been caught and handled");
//        }


    }

    @org.junit.Test
    public void testReset() throws Exception {
         int num = 4;
        game.reset(num);
        int newNum = game.getNumber();
        Assert.assertEquals(4,newNum);
        int newGuesses = game.getGuesses();
        Assert.assertEquals(0,newGuesses);

    }


    @org.junit.Test
    public void testResetIncorrectInput() throws Exception {

        String guess = null;
        Integer num = Integer.parseInt(guess);
        game.reset(num);
        int newNum = game.getNumber();
        Assert.assertEquals(4,newNum);
        int newGuesses = game.getGuesses();
        Assert.assertEquals(0,newGuesses);


    }
}
