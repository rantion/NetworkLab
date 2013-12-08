package com.company;

/**
 * Created with IntelliJ IDEA.
 * User: Rachel
 * Date: 12/8/13
 * Time: 12:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class Game {
        private int number, guesses, guess;
        private String guessResult;

        public Game(){
            number = 0;
            guess = 0;
            guesses = 0;
        }

        public void setGuesses(int guesses) {
            this.guesses = guesses;
        }

        public String getGuessResult() {
            return guessResult;
        }

        public void setGuessResult(String guessResult) {
            this.guessResult = guessResult;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getGuesses() {
            return guesses;
        }

        public int getGuess() {
            return guess;
        }

        public void setGuess(int guess) {
            this.guess = guess;
        }

        public String checkGuess(int guess){
            guessResult="";
            if(guess>getNumber()){
                guessResult = "too high";
            }
            if(guess<getNumber()){
                guessResult = "too low";
            }
            else if(guess == number){
                guessResult = "correct";
            }
            guesses++;
//            System.out.println(guessResult);
//            System.out.println("Guesses: "+guesses);
            return guessResult;
        }

        public void reset(int newNumber){
            setNumber(newNumber);
            guesses = 0;

        }

    }

