package com.pokerace.gameplay.core.player;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Administrator
 */
public class PlayerException extends Exception {

     public PlayerException() { }

        public PlayerException(String message) {super(message);}     
      public PlayerException(String message, Exception innerException){super(message,innerException);}
}
