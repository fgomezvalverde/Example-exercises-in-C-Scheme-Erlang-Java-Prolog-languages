/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gramatica;

import Automatas.Transicion;
import java.util.List;

/**
 *
 * @author Fabian
 */
public class Grammar {
    
    public Grammar(List<String> pAlfabeto,List<String> pTerminales,List<Transicion> pReglas,String pEstado_Inicial)
    {
        Alfabeto = pAlfabeto;
        Terminales = pTerminales;
        Reglas = pReglas;
        Estado_Inicial = pEstado_Inicial;
    }
    
    List<String> getDerivation(String s)
    {
        return null;
    }
    
    @Override
    public String toString()
    {
        String Resultado="";
        for(int contador=0; contador< Reglas.size();contador++)
        {
            Resultado+= Reglas.get(contador).getInicio()+" -> "+Reglas.get(contador).getValor()+Reglas.get(contador).getFinal()+"\n";
        }
        return Resultado;
    }
    
    private List<String> Alfabeto;
    private List<String> Terminales;
    private List<Transicion> Reglas;
    private String Estado_Inicial;
}
