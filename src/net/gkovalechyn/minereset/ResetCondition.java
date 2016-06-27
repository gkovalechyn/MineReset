
/*
 * File:   ResetCondition.java
 * Author: gkovalechyn
 *
 * Created on Feb 9, 2016, 9:05:49 PM
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.gkovalechyn.minereset;

/**
 *
 * @author gkovalechyn
 */
public enum ResetCondition {
    TIME,
    PERCENTAGE_LEFT;
    
    public static ResetCondition getByString(String s){
        if ("Time".equalsIgnoreCase(s)){
            return ResetCondition.TIME;
        }else if ("PERCENTAGE_LEFT".equalsIgnoreCase(s)){
            return ResetCondition.PERCENTAGE_LEFT;
        }else{
            return null;
        }
    }

    @Override
    public String toString() {
        switch(this){
            case PERCENTAGE_LEFT:
                return "PERCENTAGE_DUG";
            case TIME:
                return "TIME";
        }
        
        return null;
    }
    
    
}
