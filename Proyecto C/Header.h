

#ifndef __HEADER_H
#define __HEADER_H


#define MAX_VALUE 100
#define MAX_VALUE_GROUP 15
#define LONG_MAX_LINEA  1024

struct Cromosoma

{
        int valores[MAX_VALUE];   
        int cantidad ;
        int peso;

}Cromosoma_default = {{0},0,0}; 
 struct Individuo

{
        struct Cromosoma *grupos;
        int matriz_diferencias[MAX_VALUE][MAX_VALUE];  
        int diferencia_total;
}; 


void escribe_Individuo(Individuo pIndividuo);
void leer_Datos();
void generar_Poblacion_Inicial(Individuo pIndividuo, int numeros[]);
void IMPRIME_POBLACION(Individuo poblacion[]);
void calculo_Peso(Individuo poblacion[]);
void Seleccion(Individuo poblacion[]);
void Quicksort(Individuo unarray[], int izq, int der);
int pivot(Individuo unarray[], int izq, int der);
void Cruce (Individuo poblacion[]);



int posicion_Valor(int valor,int array[]);
int contador_Veces(int valor,int limite,int array[]);
void quitar_Repetidos(int array[],int limite,int resultado[]);
Individuo hacer_Cruce(Individuo macho,Individuo hembra);

#endif /*__HEADER_H*/
