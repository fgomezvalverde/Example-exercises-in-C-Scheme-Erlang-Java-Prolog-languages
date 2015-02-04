/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmogenetico;

/**
 *
 * @author Fabian
 */
public interface Individuo {
    
    public Individuo cruzar(Individuo pIndividuo);
    
    public long fitness();
    
    public void mutar();
    
    public String toString();
    
}
