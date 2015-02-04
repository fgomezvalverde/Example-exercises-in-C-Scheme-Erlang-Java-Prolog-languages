#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<iostream>
#include<sstream>
#include<fstream>
#include<stdio.h>
#include<stdlib.h>
#include"Header.h"

#define __ std::
int hilos,tamanio_Poblacion,tamanio_Generaciones,cantidad_Grupos,cantidad_Numeros=1;
__ string nombre_Archivo ,elitismo;
int  numeros [MAX_VALUE] ;

void leer_Datos()
{
// RECOLECTA LOS DATOS INICIALES Y LOS ALMACENA EN LAS VARIABLES GLOBALES
 	__ cout << "Nombre , Numero de generaciones, Numero de hilos, tamanio de Poblacion y Elistismo \n";
 	__ cin >> nombre_Archivo>> tamanio_Generaciones >> hilos >> tamanio_Poblacion >> elitismo;

// SE CAMBIA A CHAR EL NOMBRE DEL ARCHIVO PARA USARLO CON EL FSTREAM
        
        nombre_Archivo+=".txt";
        __ string var_split,linea;
        char* nombre = new char[nombre_Archivo.length()+1];
        strcpy(nombre, nombre_Archivo.c_str());
        FILE *entrada;
        char c[LONG_MAX_LINEA];
        __ stringstream ss;
         entrada = fopen(nombre, "r");
        for(int i=0;i<cantidad_Numeros;i++){
                fgets(c, LONG_MAX_LINEA, entrada) ;
                ss << c;
                ss >> linea;
                if(linea=="p") i=MAX_VALUE;
                if(i==0)
                {
                 cantidad_Numeros = atoi((linea.c_str()));
                }
                else if(i==1)
                {       
                        cantidad_Grupos = atoi((linea.c_str()));
                }
                else{
                        if(atoi((linea.c_str()))!=0){
                                numeros[i-2] = atoi((linea.c_str()));
                        }
                }
        
        }
        fclose(entrada);
}

// escribe en el Resultado.txt la solucion mas apta para dada ese tamanio de 
void escribe_Individuo(Individuo pIndividuo)
{
        FILE *fp;
        __ string cuerpo;
        __ stringstream ss;
        fp = fopen ( "Resultado.txt", "w" );
        cuerpo+="Resultado\n\n";
        for(int y = 0 ; y <cantidad_Grupos;y++)
                {
                        cuerpo+="\n\nGrupo ";
                        ss << y;
                        cuerpo+=ss.str();
                        cuerpo+=":";
                        for(int contador_val=0; contador_val< pIndividuo.grupos[y].cantidad; contador_val++)
                        {
                                if(pIndividuo.grupos[y].valores[contador_val]!=0){
                                        __ stringstream ss;
                                        ss<<pIndividuo.grupos[y].valores[contador_val];
                                        cuerpo+=ss.str();
                                        cuerpo+=",";
                                }
                        }
                        cuerpo+="\nDiferencias: ";
                        for(int x = 0 ; x < cantidad_Grupos; x++)
                        {
                                        __ stringstream ss;
                               ss << abs(pIndividuo.grupos[y].peso-pIndividuo.grupos[x].peso);
                                cuerpo+=ss.str();
                                cuerpo+=" , ";
                        }
                }
        char resp[1024];
        strcpy(resp, cuerpo.c_str());
        fputs(resp, fp);
        fclose ( fp );
}

// imprime los valores de un individuo
void imprime_Individuo(Individuo pIndividuo)
{
                for(int y = 0 ; y <cantidad_Grupos;y++)
                {
                        __ cout<<"\n\nGrupo "<<y<<":";
                        for(int contador_val=0; contador_val< pIndividuo.grupos[y].cantidad; contador_val++)
                        {
                                if(pIndividuo.grupos[y].valores[contador_val]!=0){
                                        __ cout<<pIndividuo.grupos[y].valores[contador_val]<<",";
                                }
                        }
                        __ cout<<"\nDiferencias: ";
                        for(int x = 0 ; x < cantidad_Grupos; x++)
                        {
                                
                                __ cout << abs(pIndividuo.grupos[y].peso-pIndividuo.grupos[x].peso) <<" , ";
                        }
                }
}
// pone la estructura por defecto de los cromosomas
Individuo pone_Default(Individuo pIndividuo)
{
        for(int counter_grupo=0 ; counter_grupo < cantidad_Grupos; counter_grupo++)
        {
                pIndividuo.grupos[counter_grupo] = Cromosoma_default;
        }
        return pIndividuo;
}

// ASIGNA A UN INDIVIDUO CROMOSOMAS ALEATORIOS
void generar_Poblacion_Inicial(Individuo pIndividuo, int numeros[])
{
         int numero_Random ;
//pone a todos los cromosomas con el struct por default
        pIndividuo = pone_Default(pIndividuo);
        for(int counter = 0; counter < cantidad_Numeros ; counter++)
        {
                    numero_Random= rand()%cantidad_Grupos;  
                    pIndividuo.grupos[numero_Random].valores [pIndividuo.grupos[numero_Random].cantidad] = numeros[counter];   
                    pIndividuo.grupos[numero_Random].cantidad++;
        }
}



// CALCULA EL PESO POR GRUPO
void calculo_Peso(Individuo poblacion[])
{
        int resultado;
        for(int i =0 ; i < tamanio_Poblacion ; i++)
        {
//se asigna el peso de cada grupo
                for(int y = 0 ; y <cantidad_Grupos;y++)
                {
                         resultado =0;
                        for(int x = 0 ; x < 3; x++)
                        {
                                resultado+= poblacion[i].grupos[y].valores[x];
                        }
                        poblacion[i].grupos[y].peso = resultado;
                }


//se hace la matriz representativa de cada individuo
                for(int contador_minuendo = 0; contador_minuendo < cantidad_Grupos; contador_minuendo++)
                {
                        for(int contador_sustraendo = 0 ; contador_sustraendo < cantidad_Grupos; contador_sustraendo++)
                        {
                                resultado =  abs( poblacion[i].grupos[contador_minuendo].peso - poblacion[i].grupos     [contador_sustraendo].peso);
//le asigna el valor absoluto de la resta de los dos grupos , la diferencia
                                poblacion[i].matriz_diferencias[contador_minuendo][contador_sustraendo] = resultado;
                        }
                }
//se asigna la diferencia total de cada grupo contra todos
                resultado =0;
                for(int contador_pivot =0 ; contador_pivot < cantidad_Grupos; contador_pivot++)
                {
                        for(int contador_grupos = 0; contador_grupos < cantidad_Grupos ; contador_grupos++)
                        {
                                resultado+= poblacion[i].matriz_diferencias[contador_pivot][contador_grupos];
                        }                
                } 
                poblacion[i].diferencia_total = resultado;  
        }        
}

//se clonan la mitad de los individuos mas aptos
void Seleccion(Individuo poblacion[])
{
//se toma la diferencia total por individuo y se acomoda por orden ascendente para agarrar lo mejores individuos        
        Quicksort(poblacion, 0, tamanio_Poblacion-1);
//se hacen las copias de los mejores y se descartan los peores
        for(int contador_copia=0; contador_copia < tamanio_Poblacion/2; contador_copia++)
        {
                poblacion[contador_copia+tamanio_Poblacion/2] = poblacion[contador_copia];
        }
}

void Cruce (Individuo poblacion[])
{
        Individuo macho,hembra;
        for(int contador_cruce =0; contador_cruce < tamanio_Poblacion-1; contador_cruce+=2)
        {
                if(elitismo=="SI" && contador_cruce ==0)
                {
                        contador_cruce+=2;
                }
//se empieza hacer los cruces
                memcpy(&macho,&poblacion[contador_cruce], sizeof(Individuo));
                memcpy(&hembra,&poblacion[contador_cruce+1], sizeof(Individuo));
                poblacion[contador_cruce]=hacer_Cruce(macho,hembra);    
        }
}
Individuo hacer_Cruce(Individuo macho,Individuo hembra)
{
        Individuo resultado;
        resultado.grupos=new Cromosoma[cantidad_Grupos];
        pone_Default(resultado);

        int ran_macho =rand()%cantidad_Grupos;
        int ran_hembra=rand()%cantidad_Grupos;
        int ran,valor,cantidad;
        int numero_copia[MAX_VALUE];

//elmina los valores que estan repetidos entre los genes macho y hembra
        for(int contador_revision=0; contador_revision < macho.grupos[ran_macho].cantidad; contador_revision++)
        {
                valor = macho.grupos[ran_macho].valores[contador_revision];
// si la suma de las repeticiones de ese valor, de macho y hembra sean mayor que la total, se descartara un repetido con valor 0
                if(contador_Veces(valor,cantidad_Numeros,numeros) < contador_Veces(valor,macho.grupos[ran_macho].cantidad,macho.grupos[ran_macho].valores) + contador_Veces(valor,hembra.grupos[ran_hembra].cantidad,hembra.grupos[ran_hembra].valores)&& valor!=0)
                        {
                            macho.grupos[ran_macho].valores[contador_revision]=0; 
                        }
        }

        memcpy(numero_copia, numeros, MAX_VALUE); // hace un clon del array de nuemeros
//quita los valores ya usados en el cruce
        quitar_Repetidos(macho.grupos[ran_macho].valores,macho.grupos[ran_macho].cantidad,numero_copia);
        quitar_Repetidos(hembra.grupos[ran_hembra].valores,hembra.grupos[ran_hembra].cantidad,numero_copia);

//agrega todos los valores que no estan contemplados en los genes de macho y hembra
        for(int counter=0; counter< cantidad_Numeros;counter++)
        {       
                ran=rand()%(cantidad_Grupos-2);
                valor = numero_copia[counter];
                if(valor!=0)
                {
                        cantidad = resultado.grupos[ran].cantidad; 
                        resultado.grupos[ran].valores[cantidad] = valor;
                        resultado.grupos[ran].cantidad++;
                }
        }

//agrega a el individuo resultado los 2 genes con la seguridad que no estan repetidos
        resultado.grupos[cantidad_Grupos-1] = macho.grupos[ran_macho];
        resultado.grupos[cantidad_Grupos-2] = hembra.grupos[ran_hembra];
        return resultado; 
        
}
//modifica el arreglo de entrada poniendole un 0
void quitar_Repetidos(int array[],int limite,int resultado[])
{
        int posicion;
        for(int contador=0; contador< limite; contador++)
        {
                posicion = posicion_Valor(array[contador],resultado);
                resultado[posicion] = 0;
        }
}
//determina la posicion del valor en el arreglo
int posicion_Valor(int valor,int array[])
{
        for(int contador=0;contador < cantidad_Numeros ; contador++)
        {
                if(array[contador]==valor)
                {return contador;}
        }
        return -1;
}
// dado un valor cuenta cuantas veces se encuentra ese valor en el arreglo
int contador_Veces(int valor,int limite,int array[])
{
        int resultado=0;
        for(int contador=0; contador < limite; contador++)
        {
                if(array[contador]==valor && valor !=0)
                {
                        resultado++;
                }
        }
        return resultado;
}
//============++CODIGO ADQUERIDO DE http://blog.e-urrea.com/noticias/2011/03/23/metodo-de-ordenacion-burbuja-y-quicksort-en-c/
int pivot(Individuo unarray[], int izq, int der)
{
    int i;
    int pivote;
    Individuo valor_pivote;
    Individuo aux;

    pivote = izq;
    valor_pivote = unarray[pivote];
    for (i=izq+1; i<=der; i++){
        if (unarray[i].diferencia_total < valor_pivote.diferencia_total){
                pivote++;
                aux=unarray[i];
                unarray[i]=unarray[pivote];
                unarray[pivote]=aux;

        }
    }
    aux=unarray[izq];
    unarray[izq]=unarray[pivote];
    unarray[pivote]=aux;
    return pivote;
}

void Quicksort(Individuo unarray[], int izq, int der)
{
     int pivote;
     if(izq < der){
        pivote=pivot(unarray, izq, der);
        Quicksort(unarray, izq, pivote-1);
        Quicksort(unarray, pivote+1, der);
     }
}
//=============================================================================================================
int main()
{
// lee los datos iniciales de consola y del txt        
        leer_Datos();
        Individuo poblacion [tamanio_Poblacion];
//crea la poblacion al azar
        for(int contador = 0 ; contador < tamanio_Poblacion ; contador++)
        {        
          poblacion[contador].grupos = new Cromosoma[cantidad_Grupos];
          generar_Poblacion_Inicial(poblacion[contador],numeros);  
        }        

   
        for(int contador_Generaciones=0 ; contador_Generaciones < tamanio_Generaciones ; contador_Generaciones++)
        {
                calculo_Peso(poblacion);
                Seleccion(poblacion);
                __ cout<< "\n\nElite";
                imprime_Individuo(poblacion[0]);                // la mejor poblacion esta dada por 0, por el ordenamiento de diferencia
                Cruce(poblacion);          
        }
        calculo_Peso(poblacion);
        escribe_Individuo(poblacion[0]);
	return(0);
}





