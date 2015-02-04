/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utiles;

import java.util.List;
import java.util.Random;

/**
 *
 * @author Fabian
 */
public class StringGenerator {
    public StringGenerator(List<String> expresion_regular)
    {
        Expresion = expresion_regular;
    }
    
    public String getHilera()
    {
        int Random = (int) (Math.random() * Maximo_Hilera);
        String Resultado="";
        for(int contador=0;contador<Random;contador++)
        {
            Resultado+= Expresion.get((int) (Math.random()*Expresion.size()));
        }
        
        return Resultado;
    }
    
    public String getHilera(int Min,int Max)
    {
        Random rand = new Random();
        int Random = rand.nextInt((Max - Min) + 1) + Min;
        String Resultado="";
        for(int contador=0;contador<Random;contador++)
        {
            Resultado+= Expresion.get((int) (Math.random()*Expresion.size()));
        }
        
        return Resultado;
    }
    
    private final int Maximo_Hilera = 20;
    private List<String> Expresion;
}
