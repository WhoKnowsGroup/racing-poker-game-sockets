/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pokerace.gameplay.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author lokesh
 */
public class ChatBot 
{
    List <String> chat_messages ;
    
    public ChatBot()
    {
        this.chat_messages = new ArrayList<>();
        this.chat_messages.add("Ouch,that,hurt");
        this.chat_messages.add("A,good,Choice,by,me,I,hope");
        this.chat_messages.add("Go,Go,Go,you,good,thing");
        this.chat_messages.add("This,next,one,will,bury,me");
        this.chat_messages.add("Some,familiar,faces,here");
        this.chat_messages.add("I,am,taking,you,all,out,for,dinner");
        this.chat_messages.add("lol");
        this.chat_messages.add("Laugh,out,loud,Big,Time");
        this.chat_messages.add("ROFL");
        this.chat_messages.add("Kiss,My,Entire,Ass");
        this.chat_messages.add("I,want,this,win");
        this.chat_messages.add("Leader,Board,Me,Baby");
        this.chat_messages.add("Shonky,result,there");
        this.chat_messages.add("Back,on,the,boil");
        this.chat_messages.add("Slammed,hard");
        this.chat_messages.add("Forget,me,not");
        this.chat_messages.add("Vicious,Deal");
        this.chat_messages.add("Ha,Cha,Cha,cha");
        this.chat_messages.add("I,am,going,hard");
        this.chat_messages.add("Cant,pick,em,sometimes"); 
        this.chat_messages.add("is,nervous");
        this.chat_messages.add("is,happy");
        this.chat_messages.add("is,really,happy");
        this.chat_messages.add("is,optimistic");
        this.chat_messages.add("feels,confident");
        this.chat_messages.add("yells,wow");
        this.chat_messages.add("are,you,kidding!");
        this.chat_messages.add("breaks,into,dance");
        this.chat_messages.add("is,wondering,what,will,happen");
        this.chat_messages.add("screams");
        this.chat_messages.add("tells,a,joke");
        this.chat_messages.add("is,calm");
        this.chat_messages.add("looks,worried");
        this.chat_messages.add("salutes,with,respect");
        this.chat_messages.add("takes,a,bow");
        this.chat_messages.add("is,sporting,a,smirk");
    }
    
    public String chat_message()
    {
        int size = this.chat_messages.size();
        Random rand = new Random();
        int randnum = rand.nextInt(size -1);
        return this.chat_messages.get(randnum);
    }
}
