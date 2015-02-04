/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package algoritmogenetico;

/**
 *
 * @author Fabian
 */
public class Generacion {

    public Generacion(int pCantidad_Individuos, Individuo[] pPoblacion) {
        cantidad_Individuos = pCantidad_Individuos;
        generacion = pPoblacion;
        probabilidades = new double[pPoblacion.length];
    }

    public Individuo getElite(boolean pConFitness_Alto) {
        // SE AGARRA EL PRIMER DATO PARA EMPEZAR A COMPARAR
        double valor_ELITE = generacion[0].fitness();
        int posicion = 0;

        // SEGEMENTO PARA LOS EVALUADOS ALTOS
        if (pConFitness_Alto) {
            for (int contador = 1; contador < generacion.length; contador++) {
                if (generacion[contador].fitness() > valor_ELITE) {
                    valor_ELITE = generacion[contador].fitness();
                    posicion = contador;
                }
            }
        } else {
            for (int contador = 1; contador < generacion.length; contador++) {
                if (generacion[contador].fitness() < valor_ELITE) {
                    valor_ELITE = generacion[contador].fitness();
                    posicion = contador;
                }
            }
        }
        return generacion[posicion];
    }

    public Individuo getIndividuo(int pPosicion) {
        return generacion[pPosicion];
    }

    public void add(Individuo pNuevo) {
    }

    public void llenarPosibilidades(boolean pConFitness_Alto) {
        double sumatoria_Fitness1 = 0;
        double sumatoria_Fitness2 = 0;
        int contador;

        // EL TOTAL DEL VALOR DEL FITNESS
        for (contador = 0; contador < generacion.length; contador++) {
            sumatoria_Fitness1 += generacion[contador].fitness();
            //System.out.println("asdasd " + sumatoria_Fitness1);
        }

        for (contador = 0; contador < generacion.length; contador++) {
            if (generacion[contador].fitness() == 0) {
                sumatoria_Fitness2 += sumatoria_Fitness1 / 1;
                probabilidades[contador] = sumatoria_Fitness1 / 1;
            } else {
                
                // SI EL ES MAXIMO O MINIMO 
                if(pConFitness_Alto){
                    sumatoria_Fitness2 += sumatoria_Fitness1 / generacion[contador].fitness();
                    probabilidades[contador] = sumatoria_Fitness1 / generacion[contador].fitness();
                }
                else
                {
                    sumatoria_Fitness2 += (sumatoria_Fitness1-generacion[contador].fitness()) / generacion[contador].fitness();
                    probabilidades[contador] = (sumatoria_Fitness1-generacion[contador].fitness()) / generacion[contador].fitness();
                }
            }
            //System.out.println("W " + sumatoria_Fitness1 / generacion[contador].fitness());
        }
        sumatoria_Fitness1 = 0;


        for (contador = 0; contador < generacion.length; contador++) {



            // SUMATORIA FITNESS1 = T em imagen q tiene T=1
            sumatoria_Fitness1 += 100 * probabilidades[contador] / sumatoria_Fitness2;

            //System.out.println("T " + sumatoria_Fitness1);

            // SE ASIGNA EL INTERVALO DE PROBABILIDAD
            probabilidades[contador] = sumatoria_Fitness1;

        }

    }

    /**
     * SE COMIENZA EL PROCESO DE LA MITAD DE LA POBLACION CON LOS MAS APTOS
     */
    public void seleccion(boolean pConElite, boolean pConFitnessAlto) {
        int contador;
        int elite = 0;
        // SE VA RELLENAR UNA NUEVA POBLACION CON LOS INDIVIDUOS ANTERIORES HACIENDO SELECCION DE LOS MAS APTOS
        Individuo[] nuevaPoblacion = new Individuo[generacion.length];

        // SI ES CON ELITE SI PONE DE PRIMERO EN LA POBLACION
        if (pConElite) {
            elite = 1;
            nuevaPoblacion[0] = getElite(pConFitnessAlto);
        }

        for (contador = elite; contador < generacion.length / 2; contador++) {
            // A LA MITAD DE LA NUEVA POBLACION SE ASIGNA CON SELECCION
            nuevaPoblacion[contador] = generacion[posicion_Selecta()];
        }
        generacion = null;
        generacion = nuevaPoblacion;

    }

    /**
     * SACANDO UN RANDOM, SEGUN LOS INTERVALOS DE LAS PROBABILIDADES SACA CUALES
     * INDIVIDIDUOS SE SELECCIONAN
     *
     * @return
     */
    private int posicion_Selecta() {
        int random, contador;
        int intervalo_Inicial = 0;
        random = (int) (Math.random() * probabilidades[generacion.length - 1]);
        for (contador = 0; contador < probabilidades.length; contador++) {
            if (intervalo_Inicial <= random && random <= probabilidades[contador]) {
                return contador;
            }
            // ASIGNA DE INTERVALO INICIAL EL PASADO USADO
            intervalo_Inicial = (int) probabilidades[contador];
        }
        //System.out.println("AQUI NO TENGO QUE LLEGAR");
        return 0;
    }

    /**
     * *
     * CUANDO LA POBLACION ESTA PARTIDA A LA MITAD, DE CRUSA ENTRE LOS
     * INDIVIDUOS QUE EXISTEN PARA VOLVER A GENERAR TODA LA POBLACION
     */
    public void cruce() {
        int rand;
        int limite = generacion.length;
        for (int contador = limite / 2; contador < limite; contador++) {
            // RANDOM DEL PRIMER INDIVIDUO A CRUCE
            rand = (int) (Math.random() * (limite / 2));
            Individuo cruce = generacion[rand];

            // RANDOM DEL OTRO INDIVIDUO
            rand = (int) (Math.random() * (limite / 2));



            generacion[contador] = generacion[rand].cruzar(cruce);
        }

    }

    public void mutacion(int pProbabilidad_Mutacion) {
        int rand = (int) (Math.random() * 100);
        if (rand < pProbabilidad_Mutacion) {
            System.out.println("MUTACION");
            for (int contador = 0; contador < generacion.length; contador++) {
                generacion[contador].mutar();
            }
        }

    }

    /**
     * SOLO PARA PRUEBAS ,, IMPRIME VALOR
     */
    public void IMPRIMIR() {
        for (int i = 0; i < generacion.length; i++) {
            if (generacion[i] != null) {
                //System.out.println(generacion[i].toString());
                System.out.println("pos" + i + " valor= " + generacion[i].fitness());
            }
        }
    }
    private int cantidad_Individuos;
    private Individuo[] generacion;
    private double[] probabilidades; // {12,45,68,78,98}
    private int posicion_Actual;
}
