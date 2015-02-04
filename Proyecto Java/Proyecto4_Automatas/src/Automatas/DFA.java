/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

import Gramatica.Grammar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Fabian
 */
public class DFA extends NFA {

    public DFA(List<String> pAlfabeto, List<Transicion> pTransiciones, List<String> pEstados, String pEstado_Inicial, List<String> pEstados_Finales) {
        super(pAlfabeto, pTransiciones, pEstados, pEstado_Inicial, pEstados_Finales);
    }

    /***
     * TRANSFORMA UN AUTOMATA A GRAMATICA
     * @return 
     */
    public Grammar toGrammar() {
        List<Transicion> Reglas = cambiarInicial(this.getTransiciones());
        Reglas.addAll(aniadirEpsilon());
        //(List<String> pAlfabeto,List<String> pTerminales,List<String> pReglas,String pEstado_Inicial)
        return new Grammar(this.getAlphabet(),this.getEstados(),Reglas,this.getEstado_Inicial());
    }
    
    /***
     * PARA LOS ESTADOS FINALES, AGREGA UNA TRANSICION EPSILON QUE DETERMINA SALIDA
     * @return 
     */
    private List<Transicion> aniadirEpsilon()
    {
        List<Transicion> Resultado = new ArrayList<>();
        for(int contador=0; contador < this.getEstados_Finales().size() ; contador++)
        {
            Resultado.add(new Transicion(this.getEstados_Finales().get(contador),"epsilon",""));
        }
        return Resultado;
    }
    
    /**
     * CAMBIA UN ESTADO NORMAL , A UN ESTADO INICIAL S, PARA LA GRAMATICA
     * @param pReglas
     * @return 
     */
    private List<Transicion> cambiarInicial(List<Transicion> pReglas)
    {
        for(int contador=0; contador< pReglas.size() ; contador++)
        {
            if(pReglas.get(contador).getInicio().equals(this.getEstado_Inicial()))
            {
                pReglas.set(contador,new Transicion("S", pReglas.get(contador).getFinal(), pReglas.get(contador).getValor()));
            }
            else if(pReglas.get(contador).getFinal().equals(this.getEstado_Inicial()))
            {
                pReglas.set(contador,new Transicion(pReglas.get(contador).getInicio(),"S",pReglas.get(contador).getValor() ));

            }
        }
        return pReglas;
    }
    
    NFA toNFA() {
        return this;
    }

    public DFA mix(DFA dia, int op) {
        // SE CREAN LOS ESTADOS
        List<List<String>> Estados_Nuevos = crear_Estados(Arrays.asList(this.getEstados(), dia.getEstados()));

        List<Transicion> Transiciones_Resultados = new ArrayList<>();
        List<String> EstadoInicial_Resultados = Arrays.asList(this.getEstado_Inicial(), dia.getEstado_Inicial());
        List<String> EstadoFinal_Resultados = new ArrayList<>();
        EstadoFinal_Resultados.addAll(this.getEstados_Finales());
        EstadoFinal_Resultados.addAll(dia.getEstados_Finales());

        List<String> Temporal;
        String estado1, estado2, valor;
        for (int contador = 0; contador < Estados_Nuevos.size(); contador++) {
            for (int contador_alfabeto = 0; contador_alfabeto < this.getAlphabet().size(); contador_alfabeto++) {
                Temporal = new ArrayList<>();

                //SE AGARRAN LOS VALORES DE LOS ESTADOS DE CADA ESTADO
                estado1 = Estados_Nuevos.get(contador).get(0);
                estado2 = Estados_Nuevos.get(contador).get(1);

                valor = this.getAlphabet().get(contador_alfabeto);
                Temporal.addAll(this.siguienteEstado(estado1, valor));
                Temporal.addAll(dia.siguienteEstado(estado2, valor));

                // SE ALMACENA LA NUEVA TRANSICIONES CON EL VALOR
                Transiciones_Resultados.add(new Transicion(Estados_Nuevos.get(contador), Temporal, valor));

            }
        }
        Transiciones_Resultados = dia.transformar(Transiciones_Resultados);

        List<String> Estados_Resultado = new ArrayList<>();
        for (int cont = 0; cont < Estados_Nuevos.size(); cont++) {
            Estados_Resultado.add(Estados_Nuevos.get(cont).toString());
        }

        //for(int i=0;i< Estados_Nuevos.size();i++) {System.out.println(Estados_Nuevos.get(i).toString());}

        switch (op) {
            case 0:
                EstadoFinal_Resultados = UNION(Estados_Nuevos, EstadoFinal_Resultados);
                return new DFA(this.getAlphabet(), Transiciones_Resultados, Estados_Resultado, EstadoInicial_Resultados.toString(), EstadoFinal_Resultados);
            case 1:
                EstadoFinal_Resultados = INTERSECT(crear_Estados(Arrays.asList(this.getEstados_Finales(), dia.getEstados_Finales())));
                return new DFA(this.getAlphabet(), Transiciones_Resultados, Estados_Resultado, EstadoInicial_Resultados.toString(), EstadoFinal_Resultados);
                
            case 2:
                EstadoFinal_Resultados = UNION(Estados_Nuevos,this.getEstados_Finales());
                return new DFA(this.getAlphabet(), Transiciones_Resultados, Estados_Resultado, EstadoInicial_Resultados.toString(), EstadoFinal_Resultados);
               
            case 3:
                // SE SACAN LA UNION Y LA INTERSECCION
                EstadoFinal_Resultados = UNION(Estados_Nuevos, EstadoFinal_Resultados);
                List<String> INSER = INTERSECT(crear_Estados(Arrays.asList(this.getEstados_Finales(), dia.getEstados_Finales())));
                
                // SE RESTA A LA UNION
                EstadoFinal_Resultados.removeAll(INSER);
                
                return new DFA(this.getAlphabet(), Transiciones_Resultados, Estados_Resultado, EstadoInicial_Resultados.toString(), EstadoFinal_Resultados);             


            default:
                return null;
        }

    }

    /**
     * *
     * CREAS LOS ESTADOS DE LA COMBINACION DE LOS AUTOMATAS
     *
     * @param pEstados
     * @return
     */
    private List<List<String>> crear_Estados(List<List<String>> pEstados) {
        List<List<String>> Resultado = new ArrayList<>();
        List<String> Temporal;
        String estado1, estado2;
        for (int cont = 0; cont < pEstados.get(0).size(); cont++) {
            for (int cont_permu = 0; cont_permu < pEstados.get(1).size(); cont_permu++) {

                estado1 = pEstados.get(0).get(cont);
                estado2 = pEstados.get(1).get(cont_permu);
                Temporal = Arrays.asList(estado1, estado2);
                if (!this.pertenece_listas(Resultado, Temporal)) {
                    Resultado.add(Temporal);
                }
            }
        }
        return Resultado;
    }

    private List<String> UNION(List<List<String>> pEstados_Nuevos, List<String> pFinales) {
        List<String> resultado = new ArrayList<>();
        for (int contador = 0; contador < pFinales.size(); contador++) {
            for (int contador_Nuevo = 0; contador_Nuevo < pEstados_Nuevos.size(); contador_Nuevo++) {
                // REVIZA QUE NO ESTE EL VALOR EN EL RESULTADO Y QUE EL ESTADO FINAL ESTE EN ESE CONJUNTO
                if (pEstados_Nuevos.get(contador_Nuevo).contains(pFinales.get(contador))
                        && !resultado.contains(pEstados_Nuevos.get(contador_Nuevo).toString())) {
                    resultado.add(pEstados_Nuevos.get(contador_Nuevo).toString());
                }
            }
        }
        return resultado;
    }

    private List<String> INTERSECT(List<List<String>> pEstados_Nuevos) {
        List<String> EstadoFinal_Resultados = new ArrayList<>();
        for (int contador = 0; contador < pEstados_Nuevos.size(); contador++) {
            EstadoFinal_Resultados.add(pEstados_Nuevos.get(contador).toString());
        }
        return EstadoFinal_Resultados;
    }
    public final static int UNION = 0;
    public final static int INTERSECT = 1;
    public final static int MINUS = 2;
    public final static int SYMMETRIC_DIFFERENCE = 3;
}
