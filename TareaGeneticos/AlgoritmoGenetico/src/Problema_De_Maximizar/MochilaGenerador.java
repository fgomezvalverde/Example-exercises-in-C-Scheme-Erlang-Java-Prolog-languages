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
public class MochilaGenerador {
    public static MochilaGenerador getInstance()
    {
        if(_Instance == null)
        {
            _Instance = new MochilaGenerador();
        }
        return _Instance;
    }
    
    
    public Individuo[] generarPoblacion(int PesoMaximo,int cantidadPoblacion)
    {
        Individuo []resultado = new Individuo[cantidadPoblacion];
        for(int contador=0;contador < cantidadPoblacion;contador++)
        {
            resultado[contador] = new Mochila(PesoMaximo,generarIndividuo(PesoMaximo));
        }
        return resultado;
    }
    
    private ArrayList generarIndividuo(int PesoMaximo)
    {
        int total = 0;
        int totalObjetos = (int)Math.random() * Maximo_Objetos;
        long ran_Valor,ran_Peso;
        ArrayList resultado = new ArrayList();
        for(int contador=0;contador< Maximo_Objetos && total<PesoMaximo;contador++)
        {
            ran_Valor = (long) (Math.random() * Maximo_Valor)+1;
            ran_Peso = (long) (Math.random() * Maximo_Peso)+1;
            
            //SI EL PESO YA SE PASA, SALIR DEL WHILE Y DAR EL RESULTADO
            if(total+ran_Peso> PesoMaximo)
            {
                break;
            }
            long []temp = new long[3];
            temp[0]=ran_Valor;
            temp[1]=ran_Peso;
            resultado.add(temp);
            
            total+=ran_Valor;
        }
        return resultado;
    }
    
    
    private static MochilaGenerador _Instance;
    public int Maximo_Valor = 60;
    public int Maximo_Peso = 80;
    private int Maximo_Objetos = 999999;
}
