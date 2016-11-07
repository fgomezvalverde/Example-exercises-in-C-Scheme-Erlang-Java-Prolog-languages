
%% AUTOR : Fabian Gomez Valverde .201281511, Instituto Tecnologico de CostaRica
%% Programa que comprime un archivo binario con el algoritmo de huffman y hace el uso de threads para hacer mas efectivo el proceso.
%%	EXTRAS:
%%		- recorridos de arboles
%%		- crea archivos de arbol,tabla frecuencias,table de conversiones
%%		- da un porcentaje de la compresion al aplicar huffman

-module(progra).
-export([frecuencia_repetida/2,busqueda_char/2,aux_busqueda_char/3,compress/3,tabla_conversiones/2,aplicar_Huffman/2,descomprimir/4]).
-export([crear_arbol/1,prueba/0,menu/4,aux_menu/5]).
-export([leerArchivo/1,escribirArchivo/2,parseBinAChar/1,convertirBytes/1,hacerConteo/1,aux_hacerConteo/2]).
-export([preorden/1,inorden/1,postorden/1,cantidad_hojas_nivel/3]).
%%====================================== FUNCIONES AUXILIARES PARA LISTAS
-export([modificarElemento/3,devuelvePosicion/3,agrupar/1,sort/1,pack_Tabla_Conversiones/2]).

%% Aumenta el contador segun el caracter a evaluar y si posicion es 0 , agrega nuevo
%% LISTA: lista con contadores actuales, POSICION: la posicion del elemento, ELEMENTO: elemento a evaluar
modificarElemento(LISTA,POSICION,_ELEMENTO) when POSICION /= 0  ->
		lists:sublist(LISTA,POSICION-1) ++ [lists:nth(POSICION,LISTA)+1] ++ lists:nthtail(POSICION,LISTA);
modificarElemento(LISTA,_,ELEMENTO) -> LISTA++[ELEMENTO,1].

%% Dado un elemento si esta en la lista y da la posicion, sino esta devuelve 0	
%% LISTA: la lista actual de elementos, ELEMENTO: el char a buscar,POSICION: contador de posicion	
devuelvePosicion([],_,_) -> 0;
devuelvePosicion([ELE,_|_LISTA],ELEMENTO,POSICION) when ELE == ELEMENTO -> POSICION +1;
devuelvePosicion([_,_|LISTA],ELEMENTO,POSICION) -> devuelvePosicion(LISTA,ELEMENTO,POSICION+2).


%% Agrupa la lista del conteo de caracteres EJ: [[2,"a"],[9,"c"]...]
%% LISTA: la lista a agrupar
agrupar([]) -> [];
agrupar([CARACTER,CANTIDAD|RESTO]) -> [[CANTIDAD,CARACTER]]++ agrupar(RESTO).
			
%% QUICKSORT acomoda la tabla de frecunecias de mayor a menos
%% TOMADO DE: http://www.erlang.org/doc/programming_examples/list_comprehensions.html
sort([[Pivot,CHAR]|T]) ->
    sort([ X || X <- T, lists:nth(1,X) < Pivot]) ++
    [[Pivot,CHAR]] ++
    sort([ X || X <- T, lists:nth(1,X) >= Pivot]);
sort([[Pivot,UNO,DOS]|T]) ->
    sort([ X || X <- T, lists:nth(1,X) < Pivot]) ++
    [[Pivot,UNO,DOS]] ++
    sort([ X || X <- T, lists:nth(1,X) >= Pivot]);
sort([]) -> [].	


pack_Tabla_Conversiones(T,[]) -> [T]; 
pack_Tabla_Conversiones(X,[Y|T2]) when  Y == 49 -> pack_Tabla_Conversiones(X++[Y],T2);
pack_Tabla_Conversiones(X,[Y|T2]) when  Y == 48 -> pack_Tabla_Conversiones(X++[Y],T2);
pack_Tabla_Conversiones([],[X|T2]) -> [X]++pack_Tabla_Conversiones([],T2);
pack_Tabla_Conversiones(T1,[X|T2]) ->[T1]++[X]++pack_Tabla_Conversiones([],T2).
			
%%====================================== FUNCIONES DE ARCHIVO
%% Dada una direccion , lee todo el contenido y lo retorna como un string
%% NOMBRE: es el nombre del archivos con su extension
leerArchivo(NOMBRE) ->
    {ok, Device} = file:open(NOMBRE, [read]),
    try aux_leerArchivo(Device)
      after file:close(Device)
    end.

%% Recorre el buffer concatenando los datos por linea
%% BUFFER : es el contenido del archivo
aux_leerArchivo(BUFFER) ->
    case io:get_line(BUFFER, "") of
        eof  -> [];
        Line -> Line ++ aux_leerArchivo(BUFFER)
    end.

%% Escribe en un archivo 
%% DATOS: string a escribir, NOMBRE: nombre del archivo a crear
escribirArchivo(DATOS,NOMBRE)->
	{ok, IODevice} = file:open(NOMBRE, [write]),
	file:write(IODevice, DATOS),
	file:close(IODevice).
%%========================================= TRATADO DE LOS STRING Y BYTES
	
%% Convierte los bytes en Char y lo devuelve en una lista
%% DATO: los dos bytes a convertir
parseBinAChar(DATO) ->
	{ok, RESULTADO,[]} = io_lib:fread("~2u", DATO),
	RESULTADO.

%% Divide cada 8 bits un string y lo convierte y los va agregando en una lista convertido
%% TEXTO: es un string de bits	
convertirBytes([]) -> [];
convertirBytes(TEXTO) -> parseBinAChar(string:substr(TEXTO,1,8)) ++ 
						convertirBytes(string:substr(TEXTO,9,string:len(TEXTO))).


%% Dado un string pasa char por char para empezar el conteo
%% TEXTO: el mensaje para contar						
hacerConteo(TEXTO) -> aux_hacerConteo(TEXTO,[]).

aux_hacerConteo([],RESULTADO) -> RESULTADO;
aux_hacerConteo(TEXTO,RESULTADO) -> VALOR = lists:nth(1,TEXTO),
		aux_hacerConteo(string:substr(TEXTO,2,string:len(TEXTO)),
			modificarElemento(RESULTADO,devuelvePosicion(RESULTADO,VALOR,1),VALOR)
						).
								

%% -- DICE SI UNA FRENCUENCIA SE REPITE EN LA LISTA
frecuencia_repetida(_,[]) -> false;
frecuencia_repetida(VALOR,[[ACTUAL|_REST]|_LISTA])  when VALOR == ACTUAL -> true;
frecuencia_repetida(REP,[_T|LISTA]) -> frecuencia_repetida(REP,LISTA).

%% Crea el arbol siempre concatenando los siguientes 2 nodos y despues hace un sort de la lista

crear_arbol([ARBOL]) -> ARBOL;
crear_arbol([UNO,DOS|LISTA_FRECUENCIA]) -> 
		NUEVA_LISTA = [[lists:nth(1,UNO)+lists:nth(1,DOS),UNO,DOS]]++LISTA_FRECUENCIA,
		crear_arbol(sort(NUEVA_LISTA)).	

%% Busca cual es la direccion a cierto caracter en el arbol
%% ARBOL: direcciones de todos los datos, ELEMENTO: elemento a buscar				
busqueda_char([_,N_IZQ,N_DER],ELEMENTO) -> string:concat(aux_busqueda_char(N_IZQ,ELEMENTO,"0"),aux_busqueda_char(N_DER,ELEMENTO,"1")).

aux_busqueda_char([_,CARACTER],ELEMENTO_A_BUSCAR,RESULTADO) when CARACTER == ELEMENTO_A_BUSCAR -> RESULTADO;
aux_busqueda_char([_,_CARACTER],_ELEMENTO_A_BUSCAR,_RESULTADO)  -> "";

aux_busqueda_char([_,N_IZQ,N_DER],ELEMENTO_A_BUSCAR,RESULTADO) -> 
								string:concat(aux_busqueda_char(N_IZQ,ELEMENTO_A_BUSCAR,string:concat(RESULTADO,"0")),aux_busqueda_char(N_DER,ELEMENTO_A_BUSCAR,string:concat(RESULTADO,"1"))).
						

%% Para cada caracter da la codificacion segun el arbol
%% ARBOL: de donde se saca direccion, TABLA_FRECUENCIAS: ayuda a distinguir cuales caracteres hay
%% EJ: [109,"0001","r","010",...]
tabla_conversiones(_,[]) -> [];
tabla_conversiones(ARBOL,[[_,CARACTER]|RESTO]) -> [CARACTER,busqueda_char(ARBOL,CARACTER)]++tabla_conversiones(ARBOL,RESTO).				

%% Concatena todas la direcciones dads por el arbol segun el texto
%% TEXTO: el texto comprimido por direcciones, ARBOL: segun una direccion del huffman decrifa a que caracter corresponde 
aplicar_Huffman([],_) -> "";
aplicar_Huffman(TEXTO,ARBOL) ->string:concat(busqueda_char(ARBOL,lists:nth(1,TEXTO)),aplicar_Huffman(lists:nthtail(1,TEXTO),ARBOL)).

%% Segun un string en binario, buscar la posicion en que se ubica su codigo y cual es su resultado
busqueda_segun_Binario(_,[]) -> [];
busqueda_segun_Binario(CODIGO,[CARACTER,SU_CODIGO|_TABLA_CONVERSIONES]) when CODIGO == SU_CODIGO ->  CARACTER;
busqueda_segun_Binario(CODIGO,[_CARACTER,_SU_CODIGO|TABLA_CONVERSIONES]) -> busqueda_segun_Binario(CODIGO,TABLA_CONVERSIONES).


%% Hace la compresion del archivo y escribe la porcentaje de compresion y almacenado los archivos correspondientes
%% FILE: direccion del archivos a comprimir, FREQ_THR: threads ...
compress(FILE,_FREQ_THR,_CODE_THR) -> 	TEXTO_BINARIO = leerArchivo(FILE)--"\n",
					TEXTO_NORMAL = convertirBytes(TEXTO_BINARIO),

					TABLA_FRECUENCIAS = sort(agrupar(hacerConteo(TEXTO_NORMAL))),
					io:fwrite("\n\n\n\n\n\n\n\n\nTabla de Frecuencias:\n  "),
					io:write(TABLA_FRECUENCIAS),
					
					ARBOL = crear_arbol(TABLA_FRECUENCIAS),
					escribirArchivo(io_lib:format("~p",[ARBOL]),string:concat("ARBOL",FILE)),
					
					TABLA_CONVERSIONES = tabla_conversiones(ARBOL,TABLA_FRECUENCIAS),
					escribirArchivo(lists:flatten(TABLA_CONVERSIONES),string:concat("TABLA_CONVERSIONES",FILE)),
					
					TEXTO_COMPRIMIDO_BYTES = aplicar_Huffman(TEXTO_NORMAL,ARBOL),
					io:fwrite("\nEl porcentaje de compresion es de: "),
					io:write((1 -(string:len(TEXTO_COMPRIMIDO_BYTES)/string:len(TEXTO_BINARIO)))*100),
					escribirArchivo(TEXTO_COMPRIMIDO_BYTES,string:concat("COMPRIRMIDO",FILE)),
					io:fwrite("\nEl archivo se ha comprimido\n\n"),
					
					menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES),
					TEXTO_DESCOMPRIMIDO = descomprimir(TEXTO_COMPRIMIDO_BYTES,[],[],pack_Tabla_Conversiones([],leerArchivo(string:concat("TABLA_CONVERSIONES",FILE)))),
					io:write(TEXTO_DESCOMPRIMIDO).

descomprimir([],_,RESULTADO,_TABLA_CONVERSIONES) -> RESULTADO;
descomprimir([PRIMERA_LETRA|TEXTO_COMPRIMIDO_BYTES],ACTUAL,RESULTADO,TABLA_CONVERSIONES) -> 
			EXISTE = busqueda_segun_Binario(ACTUAL++[PRIMERA_LETRA],TABLA_CONVERSIONES),
			if EXISTE == [] -> 
				descomprimir(TEXTO_COMPRIMIDO_BYTES,ACTUAL++[PRIMERA_LETRA],RESULTADO,TABLA_CONVERSIONES);
			
			true ->
				descomprimir(TEXTO_COMPRIMIDO_BYTES,[],RESULTADO++[EXISTE],TABLA_CONVERSIONES)
			end.
			
%% RECORRIDOS DE ARBOLES ==========================================================================
preorden([]) -> [];
preorden([FRECUENCIA,IZQ,DER])-> [FRECUENCIA]++preorden(IZQ)++preorden(DER);
preorden([_FRECUENCIA,CARACTER]) -> [CARACTER].

inorden([]) -> [];
inorden([FRECUENCIA,IZQ,DER])-> inorden(IZQ)++[FRECUENCIA]++inorden(DER);
inorden([_FRECUENCIA,CARACTER]) -> [CARACTER].
			
postorden([]) -> [];			
postorden([FRECUENCIA,IZQ,DER]) -> postorden(IZQ)++postorden(DER)++[FRECUENCIA];
postorden([_FRECUENCIA,CARACTER]) -> [CARACTER].

%%cantidad_hojas_nivel([],_,RESULTADO)-> RESULTADO;
%%cantidad_hojas_nivel([FRECUs
cantidad_hojas_nivel(_,_,_) -> io:write("").



% MENU DE LA APP
menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES) -> 
	io:fwrite("\n\n\n\n\n\n\nSeleccione la opcion que desea:\n"),
	io:fwrite("\t1.Imprimir Arbol\n"),
	io:fwrite("\t2.Recorrido Preorden\n"),
	io:fwrite("\t3.Recorrido Inorden\n"),
	io:fwrite("\t4.Recorrido Postorden\n"),
	io:fwrite("\t5.Imprimir Tabla de Frecuencias\n"),
	io:fwrite("\t6.Imprimir Tabla de Conversiones\n"),
	io:fwrite("\t7.Descomprimir Archivo\n"),
	io:fwrite("\n\t9.Salir\n"),
	{ok, [X]} = io:fread("Opcion : ", "~d"),
	io:fwrite("\n\n\n\n"),
	aux_menu(X,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE).

aux_menu(1,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> io:write(ARBOL),menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(2,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> io:write(preorden(ARBOL)),menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(3,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> io:write(inorden(ARBOL)),menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(4,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> io:write(postorden(ARBOL)),menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(5,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> io:write(TABLA_FRECUENCIAS),menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(6,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> io:write(TABLA_CONVERSIONES),menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(7,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES,FILE) -> BITS = leerArchivo(string:concat("COMPRIRMIDO",FILE))--"\n",
				TEXTO_DESCOMPRIMIDO = descomprimir(BITS,[],[],pack_Tabla_Conversiones([],leerArchivo(string:concat("TABLA_CONVERSIONES",FILE)))),
				io:fwrite("TEXTO DESCOMPRIMIDO:\n"),io:fwrite(TEXTO_DESCOMPRIMIDO),
				TEXTO_BITS = [C + $0 || <<C:1>> <= list_to_binary(TEXTO_DESCOMPRIMIDO)],
				escribirArchivo(TEXTO_BITS,string:concat("RESULTADO",FILE)),
				menu(FILE,ARBOL,TABLA_FRECUENCIAS,TABLA_CONVERSIONES);
aux_menu(9,_ARBOL,_TABLA_FRECUENCIAS,_TABLA_CONVERSIONES,_FILE) -> io:fwrite("EXIT");
aux_menu(_X,_ARBOL,_TABLA_FRECUENCIAS,_TABLA_CONVERSIONES,_FILE) -> io:fwrite("ERROR DE DIGITO").


%% EJECUTADOR INICIAL, PONER LOS PARAMETROS INICIALES PARA EMPEZAR PROGRAMA*************************************************
prueba() -> compress("prueba.txt",1,1). 

%% PAGINA PARA HACER CODIGO BIN : http://www.roubaixinteractive.com/PlayGround/Binary_Conversion/Binary_To_Text.asp










