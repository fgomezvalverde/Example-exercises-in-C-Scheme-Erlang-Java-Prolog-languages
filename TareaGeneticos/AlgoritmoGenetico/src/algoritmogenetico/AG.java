/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmogenetico;

/**
 *
 * @author Fabian
 */
public class AG {
    
    public AG(int pCantidad_generaciones,int pPosibilidad_mutacion,boolean pConElite,boolean pConFitness_Alto,Individuo []pPoblacion_Inicial)
    {
        conElite = pConElite;
        conFitness_Alto = pConFitness_Alto;
        cantidad_Generaciones = pCantidad_generaciones;
        probabilidad_mutacion = pPosibilidad_mutacion;
        poblacion = new Generacion(pPoblacion_Inicial.length,pPoblacion_Inicial);
    }
    
    private void siguienteGeneracion()
    {
        System.out.println("\n\nGENERACION");
        poblacion.llenarPosibilidades(conFitness_Alto);
        poblacion.seleccion(conElite,conFitness_Alto);
        poblacion.cruce();
        poblacion.mutacion(probabilidad_mutacion);
        poblacion.IMPRIMIR();
    }
    
    public void run()
    {
        for(int contador_Generaciones = 0; contador_Generaciones < cantidad_Generaciones; contador_Generaciones++)
        {
            siguienteGeneracion();
            
        }
    }
    
    public Generacion getGeneracion()
    {
        return poblacion;
    }
    
    private boolean conElite;
    private int cantidad_Generaciones;
    private Generacion poblacion;
    private int probabilidad_mutacion = 8;
    private boolean conFitness_Alto;
}
