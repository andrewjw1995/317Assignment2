/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package encoder;

import java.util.*;
//import java.langchar;

/**
 *
 * @author siva.saripilli
 */
public class Encoder {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        Encode();
    }
    
    private static void Encode()
    {
        // Read from file
        
        String inputString = "ABBCBCABA";
        
        Map<Integer, String> encoded = new HashMap<Integer, String>();
        String prefix = "";
        int position = 0;
        
        char nextCharacter = inputString.charAt(position++);
        
        while(!inputString.isEmpty() && !prefix.equals(""))
        {
            if (encoded.containsValue(prefix + nextCharacter))
            {
                prefix = prefix + nextCharacter;
            }
            else {
                String newDictionaryItem = prefix + nextCharacter;
                encoded.put(newDictionaryItem.length(), newDictionaryItem);
                
                prefix = "";
            }
            
            nextCharacter = inputString.charAt(position++);
        }        
        
        System.out.println(encoded);
    }
    
}