/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Problema_De_Maximizar;

import algoritmogenetico.Individuo;
import java.util.ArrayList;

/**
 *
 * @author Fabian
 */
public class Mochila implements Individuo{
    
    public Mochila(int pPesoMaximo,ArrayList pMochila)
    {
        Peso_Maximo = pPesoMaximo;
        Objetos = pMochila;
    }
    
    public ArrayList getObjetos()
    {
        return Objetos;
    }

    private void RevizarPeso()
    {
        for(int contador=Objetos.size()-1;pesoTotal()>Peso_Maximo;contador--)
        {
            Objetos.remove(contador);
        }
    }
    
    @Override
    public String toString()
    {
        String resultado = "";
        for(int contador=0; contador < Objetos.size(); contador++)
        {
            resultado+= "VALOR-"+(long)((ArrayList)Objetos.get(contador)).get(0)+" PESO-"+(long)((ArrayList)Objetos.get(contador)).get(1)+"\n";
        }
        return resultado;
    }
    
    /***
     * DEVUELBE EL VALOR QUE SE USA EN EL ALGORITMO SIN PASAR EL PESO DE LA MOCHILA
     * @return 
     */
    @Override
    public long fitness()
    {
        long total= 0;
        for(int contador=0; contador < Objetos.size(); contador++)
        {
            long[] temp = ((long[])Objetos.get(contador));
            total+= temp[0];
        }
        return total;
    }

    /***
     * DEVUELVE EL PESO TOTAL DE LA MOCHILA
     * @return 
     */
    private long pesoTotal()
    {
        long total= 0;
        for(int contador=0; contador < Objetos.size(); contador++)
        {
            total+= (long)((long[])Objetos.get(contador))[1];
        }
        return total;        
    }
    
    @Override
    public Individuo cruzar(Individuo pIndividuo) 
    {
        ArrayList crusado = new ArrayList();
        for(int contador=0;contador < Objetos.size()/2 && contador < ((Mochila)pIndividuo).getObjetos().size()/2 ;contador++)
        {
            crusado.add((long[]) ((Mochila)pIndividuo).getObjetos().get((int) (Math.random()*((Mochila)pIndividuo).getObjetos().size()/2)));
            crusado.add((long[])Objetos.get(((int)Math.random()*Objetos.size()/2)));
        }
        Objetos = crusado;
        RevizarPeso();
        return new Mochila(Peso_Maximo,Objetos);
    }
    
    @Override
    public void mutar()
    {
        long ran_Valor;
        long ran_Peso;
        long []temp;
        
        // SE MUTA A LA CUARTA PARTE DE LOS OBJETOS DE UN INDIVIDUO
        for(int contador=0; contador < Objetos.size()/2 ; contador++)
        {
            
                ran_Valor = (long) (Math.random() * MochilaGenerador.getInstance().Maximo_Valor);
                ran_Peso = (long) (Math.random() * MochilaGenerador.getInstance().Maximo_Peso);
            
                //SI EL PESO YA SE PASA, SALIR DEL WHILE Y DAR EL RESULTADO
                temp = new long[3];
                temp[0]=ran_Valor;
                temp[1]=ran_Peso;
                Objetos.set((int) (Math.random()*Objetos.size()-1), temp);
        }
        RevizarPeso();
    }
    
    
    
    
    private int Peso_Maximo;
    
    //FORMA DE OBJETOS: [ [VALOR,PESO],[VALOR,PESO],...]
    private ArrayList Objetos;
}
