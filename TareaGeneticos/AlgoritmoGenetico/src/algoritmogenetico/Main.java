/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmogenetico;

import Problema_De_Maximizar.Mochila;
import Problema_De_Maximizar.MochilaGenerador;

/**
 *
 * @author Fabian
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //generarPoblacion(int PesoMaximo,int cantidadPoblacion)
        Individuo []poblacion = MochilaGenerador.getInstance().generarPoblacion(120,5);
        
        // new AG(int pCantidad_generaciones,float pPosibilidad_mutacion,boolean pConElite,boolean pConFitness_Alto,Individuo []pPoblacion_Inicial)
        AG gen = new AG(2,11,true,false,poblacion);
        gen.run();
        /*for(int cont=0;cont < poblacion.length;cont++){
            System.out.println(poblacion[cont].toString());
        }*/
    }
}
