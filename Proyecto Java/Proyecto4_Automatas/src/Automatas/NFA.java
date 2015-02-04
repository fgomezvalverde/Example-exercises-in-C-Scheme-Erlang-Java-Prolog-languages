/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Automatas;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Fabian
 */
public class NFA {

    public NFA(List<String> pAlfabeto, List<Transicion> pTransiciones, List<String> pEstados, String pEstado_Inicial, List<String> pEstados_Finales) {
        Alfabeto = pAlfabeto;
        Transiciones = pTransiciones;
        Estados = pEstados;
        Estado_Inicial = pEstado_Inicial;
        Estados_Finales = pEstados_Finales;
    }

    public DFA toDFA() {
        if (!this.isDFA()) {
            // SE ALMACENAN LOS DATOS QUE SE VAN SACANDO DE LAS NUEVAS TRANSICIONES
            List<String> Temporal_Inicial, Temporal_Destino;

            // SE CAMBIAN LOS ESTADOS ACTUALES DE STRING A LIST<STRING>
            List<List<String>> Estados_Nuevo = estados_to_List();

            // NUEVAS TRANSICIONES
            List<Transicion> Transiciones_Nuevas = new ArrayList<>();

            String valor;

            for (int contador = 0; contador < Estados_Nuevo.size(); contador++) {
                Temporal_Inicial = Estados_Nuevo.get(contador);

                // SE HACE EL PROCESO CON TODOS LOS ESTASOS Y CON CADA UNO DE LOS CARACTERES DEL ALFABETO
                for (int contador_Alfabeto = 0; contador_Alfabeto < Alfabeto.size(); contador_Alfabeto++) {
                    Temporal_Destino = new ArrayList<>();
                    valor = Alfabeto.get(contador_Alfabeto);

                    // SE BUSCA EL DESTINO PARA CADA UNO DE LOS ESTADOS INICIALES CON EL VALOR ACTUAL
                    for (int contador_Destino = 0; contador_Destino < Temporal_Inicial.size(); contador_Destino++) {
                        List<String> destino = siguienteEstado(Temporal_Inicial.get(contador_Destino), valor);
                        Temporal_Destino.addAll(destino);
                    }

                    Transiciones_Nuevas.add(new Transicion(Temporal_Inicial, Temporal_Destino, valor));

                    // SI EL CONJUNTO NO HA SIDO CONSIDERADO, SE AGREGA
                    if (!pertenece_listas(Estados_Nuevo, Temporal_Destino)) {
                        Estados_Nuevo.add(Temporal_Destino);
                    }

                }
            }


            // SE CAMBIA TODOS LOS CONJUNTOS QUE SON LISTAS EN STRING DE CONJUNTOS
            List<Transicion> Transicion_Resultado = transformar(Transiciones_Nuevas);

            List<String> Estados_Resultado = new ArrayList<>();
            for (int cont = 0; cont < Estados_Nuevo.size(); cont++) {
                Estados_Resultado.add(Estados_Nuevo.get(cont).toString());
            }

            List<String> Finales_Resultado = crearFinales(Estados_Nuevo, Estados_Finales);


            //NFA(List<String> pAlfabeto, List<Transicion> pTransiciones, List<String> pEstados, String pEstado_Inicial, List<String> pEstados_Finales)
            return new DFA(Alfabeto, Transicion_Resultado, Estados_Resultado, Estado_Inicial, Finales_Resultado);

        } else {
            System.out.println("ESTE AUTOMATA ES UN DFA");
            return (DFA) this;
        }


    }

    protected List<Transicion> transformar(List<Transicion> pTransiciones)
    {
                   // SE CAMBIA TODOS LOS CONJUNTOS QUE SON LISTAS EN STRING DE CONJUNTOS
            List<Transicion> Transicion_Resultado = new ArrayList<>();
            String inicio, destino;
            for (int cont = 0; cont < pTransiciones.size(); cont++) {
                inicio = ((List<String>) pTransiciones.get(cont).getInicio()).toString();
                destino = ((List<String>) pTransiciones.get(cont).getFinal()).toString();
                Transicion_Resultado.add(new Transicion(inicio, destino, pTransiciones.get(cont).getValor()));
            } 
            return Transicion_Resultado;
    }
    
    /**
     * *
     * DADO LA LISTA DE CONJUNTOS , DETERMINA CUALES CONJUNTOS SON LOS FINALES
     * CON LA CONVERSIONES DEL AUTOMATA
     *
     * @param pFinales_Antes
     * @param pEstados
     * @return
     */
    private List<String> crearFinales(List<List<String>> pEstados_Nuevos, List<String> pFinales) {
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

    /**
     * *
     * TERMINA SI DOS LISTAS TIENEN LOS MISMOS VALORES PERTUMUTADOS
     */
    protected boolean pertenece_listas(List<List<String>> pListas, List<String> pUna) {
        for (int contador = 0; contador < pListas.size(); contador++) {
            if (mismas_listas(pListas.get(contador), pUna)) {
                return true;
            }
        }
        return false;
    }

    private boolean mismas_listas(List<String> uno, List<String> dos) {
        for (int cont = 0; cont < dos.size(); cont++) {
            if (!uno.contains(dos.get(cont))) {
                return false;
            }
        }
        return true;
    }

    /**
     * *
     * TRANSFORMA LOS ESTADOS ACTUALES DEL NFA a LIST<STRING> para LA CONVERSION
     *
     * @return
     */
    private List<List<String>> estados_to_List() {
        List<String> temp;
        List<List<String>> Resultado = new ArrayList<>();

        for (int contador = 0; contador < Estados.size(); contador++) {
            temp = new ArrayList<>();
            temp.add((String) Estados.get(contador));
            Resultado.add(temp);
        }
        return Resultado;
    }

    /**
     * *
     * DADO UNA CINTA DE CARACTERES, DETERMINAR SI EL AUTOMATA DICE QUE SI O NO
     * A LA TIRA
     *
     * @param s
     * @return
     */
    public boolean recognize(String s)
    {
        return recognize_aux(s,Estado_Inicial);
    }
    
    private boolean recognize_aux(String s, String estado) {
        char actual_valor;
        List<String> Estados_Nuevos = new ArrayList<>();
        if (s.length() == 0) {
            if (Estados_Finales.contains(estado)) {
                return true;
            } else {
                return false;
            }
        }

        // SE AGARRA EL SIGUIENTE VALOR DE LA TIRA
        actual_valor = s.charAt(0);

        // SE AGARRA LOS NUEVOS ESTADOS        
        Estados_Nuevos = siguienteEstado(estado, actual_valor + "");

        // SI NINGUN ESTADO RESPONDE AL ESTADO ACTUAL Y VALOR(NFA) , RETORNA FALSE
        if (Estados_Nuevos.get(0).equals("[]")) {
            return false;
        }
        for (int contador = 0; contador < Estados_Nuevos.size(); contador++) {
            if (recognize_aux(s.substring(1), Estados_Nuevos.get(contador))) {
                return true;
            }
        }
        //System.out.println("VALOR-"+actual_valor+"-estado-"+estado);

        return false;
    }

    
    protected List<String> siguienteEstado(String pEstado, String pValor) {
        List<String> Resultado = new ArrayList<>();
        for (int contador = 0; contador < Transiciones.size(); contador++) {
            if (Transiciones.get(contador).getInicio().equals(pEstado)
                    && (Transiciones.get(contador).getValor().equals(pValor) || Transiciones.get(contador).getValor().equals("epsilon"))) {
                Resultado.add((String) Transiciones.get(contador).getFinal());
            }
        }
        if (Resultado.isEmpty()) {
            Resultado.add("[]");
            return Resultado;
        } else {
            return Resultado;
        }
    }


    /**
     * *
     * DETERMINA SI UN AUTOMATA ES DETERMINISTA SABIENDO SI TIENE LA MISMA DE
     * CANTIDAD DE TRANSICIONES Y MISMA CANTIDAD DE ALFABETO
     *
     * @return
     */
    public boolean isDFA() {
        int cantidad_alfabeto = Alfabeto.size();
        for (int contador = 0; contador < Estados.size(); contador++) {
            if (cantidadTransiciones((String) Estados.get(contador)) != cantidad_alfabeto) {
                return false;
            }
        }
        return true;
    }

    /**
     * *
     * DADO EL NOMBRE DE UN ESTADO,SABER CUANTAS TRANSICIONES DE INICIO
     * TIENE.... AUXILIAR DE isDFA()
     *
     * @param Estado
     * @return
     */
    private int cantidadTransiciones(String Estado) {
        int cantidad = 0;
        for (int contador = 0; contador < Transiciones.size(); contador++) {
            if (Transiciones.get(contador).getInicio().equals(Estado)) {
                cantidad++;
            }
        }
        return cantidad;
    }

    public List<String> getAlphabet() {
        return Alfabeto;
    }

    public List<Transicion> getTransiciones() {
        return Transiciones;
    }

    public List<String> getEstados() {
        return Estados;
    }

    public String getEstado_Inicial() {
        return Estado_Inicial;
    }

    public List<String> getEstados_Finales() {
        return Estados_Finales;
    }

    // SOLO PARA PRUEBAS
    public void IMPRIME() {
        System.out.println("ALFABETO");
        for (int i = 0; i < Alfabeto.size(); i++) {
            System.out.println(Alfabeto.get(i));
        }
        System.out.println("ESTADOS");
        for (int i = 0; i < Estados.size(); i++) {
            System.out.println(Estados.get(i));
        }
        System.out.println("INICIAL");

        System.out.println(Estado_Inicial);

        System.out.println("FINALES");
        for (int i = 0; i < Estados_Finales.size(); i++) {
            System.out.println(Estados_Finales.get(i));
        }
        System.out.println("TRANSICIONES");
        for (int i = 0; i < Transiciones.size(); i++) {
            System.out.println(Transiciones.get(i).toString());
        }
    }
    private List<String> Alfabeto;
    private List<Transicion> Transiciones;
    private List<String> Estados;
    private String Estado_Inicial;
    private List<String> Estados_Finales;
}
