/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

/**
 *
 * @author Fabian
 */
public class Transicion {
    
    public Transicion(Object pInicio,Object pFinal,String pValor)
    {
        Inicio=pInicio;
        Final=pFinal;
        Valor=pValor;
    }
    
    
    
    @Override
    public String toString()
    {
        return Inicio+"-"+Final+"-"+Valor;
    }
    
    public Object getInicio(){return Inicio;}
    public Object getFinal() {return Final;}
    public String getValor(){return Valor;}
    
    private Object Inicio;
    private Object Final;
    private String Valor;
}
