package com.pokerace.gameplay.core;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Administrator
 */
public class BusRuleViolation {
    public String Description;
    public ExceptionTypeEnums ExceptionType;
    public String RuleName;
    public BusRuleViolation(ExceptionTypeEnums invalidPropType, String ruleName, String description){
        
    }

    public String getDescription() {
        return Description;
    }

    public ExceptionTypeEnums getExceptionType() {
        return ExceptionType;
    }

    public String getRuleName() {
        return RuleName;
    }
}
