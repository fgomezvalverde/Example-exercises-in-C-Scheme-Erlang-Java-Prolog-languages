%% HACE LA CREACION DE LAS TABLAS , REPLICANDO 0 SEGUN CANTIDAD
replic([],_,[]).
replic([X|T],CONTADOR,R) :-
	replic_aux(X,CONTADOR,R1),replic(T,CONTADOR,R2),
	lists:append(R1,R2,R).

replic_aux(_,0,[]).
replic_aux(VALOR,CONTADOR,R) :- CONT is CONTADOR-1, replic_aux(VALOR,CONT,R1),
	lists:append([VALOR],R1,R).

remove_at([],_,[]).
remove_at([_|LISTA],0,R) :- remove_at(LISTA,-1,R).
remove_at([ELE|LISTA],POS,R) :- POS1 is POS-1,
	remove_at(LISTA,POS1,R1),
	lists:append([ELE],R1,R).
%%===================================== EJERCICIO 21
insert_at(_,[],_,[]).
insert_at(CHAR,LISTA,LUGAR,R) :-
	length(LISTA,LEN),
	LEN =:= LUGAR,
	append(LISTA,[CHAR],R).
insert_at(CHAR,LISTA,0,R) :-
	insert_at(CHAR,LISTA,-1,R1),
	lists:append([CHAR],R1,R).
insert_at(CHAR,[ELE|LISTA],POS,R) :- POS1 is POS-1,
	insert_at(CHAR,LISTA,POS1,R1),
	lists:append([ELE],R1,R).


%% CREA EL TABLERO DE AJEDREZ CON 0 SEGUN N Y M
creaTabla(M,N,TABLE) :- replic([0],N,ARRAY),replic([ARRAY],M,TABLE).

%%  DADO UNA POSICION X y Y ,DEVOLVER EL VALOR
verPosicion(X,Y,TABLA,VALOR) :-
	nth0(X,TABLA,ARRAY),
	nth0(Y,ARRAY,VALOR).

%% CON EL X y Y CAMBIAR EL VALOR DE LA TABLA
setPosicion(X,Y,TABLA,VALOR,R) :-
	nth0(X,TABLA,ARRAY),
	remove_at(ARRAY,Y,ARRAY1),
	insert_at(VALOR,ARRAY1,Y,RESUL_ARRAY),
	remove_at(TABLA,X,TABLA1),
	insert_at(RESUL_ARRAY,TABLA1,X,R).


/*tourCaballo(_X,_Y,M,N,RECORRIDO,RESULTADO):-
	CASILLAS is (M*N),
	lists:length(RECORRIDO,LEN),
	LEN =:= CASILLAS,
	RESULTADO is RECORRIDO.*/
knigthTour(X,Y,M,N,R) :-
	MAX is (M*N)-1,
	camino(X,Y,M,N,MAX,0,[],R).

camino(X,Y,_,_,MAX,MAX,RECORRIDO,RESULTADO) :-
	lists:append([[X,Y]],RECORRIDO,RESULTADO).

camino(X,Y,M,N,MAX,LEN,RECORRIDO,RESULTADO) :-
	/*length(RECORRIDO,LEN),
	write(LEN),
	write(-----------),
	%get_char(X123),*/
	mover(X,Y,X1,Y2),
	X1 >= 0,
	Y2 >= 0,
	X1 < M,
	Y2 < N,
	not(lists:member([X1,Y2],RECORRIDO)),
	list:append([[X,Y]],RECORRIDO,RECORRIDO1),
	LEN1 is LEN+1,
	camino(X1,Y2,M,N,MAX,LEN1,RECORRIDO1,RESULTADO).



mover(X,Y,X1,Y1) :- X1 is X + 2 , Y1 is Y + 1.
mover(X,Y,X1,Y1) :- X1 is X + 1 , Y1 is Y + 2.
mover(X,Y,X1,Y1) :- X1 is X - 1 , Y1 is Y + 2.
mover(X,Y,X1,Y1) :- X1 is X - 2 , Y1 is Y + 1.
mover(X,Y,X1,Y1) :- X1 is X + 2 , Y1 is Y - 1.
mover(X,Y,X1,Y1) :- X1 is X - 2 , Y1 is Y - 1.
mover(X,Y,X1,Y1) :- X1 is X - 1 , Y1 is Y - 2.
mover(X,Y,X1,Y1) :- X1 is X + 1 , Y1 is Y - 2.

/*mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X+2,Y-1,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).
mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X-2,Y+1,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).
mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X-2,Y-1,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).


mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X+1,Y+2,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).
mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X+1,Y-2,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).
mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X-1,Y+2,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).
mover(X,Y,M,N,CONTADOR,TABLA_MOD,RESULTADO) :-
	tourCaballo(X-1,Y-2,M,N,CONTADOR+1,TABLA_MOD,RESULTADO).*/




prueba(X,Y,M,N,R) :-
	creaTabla(M,N,TABLA),
	tourCaballo(X,Y,M,N,1,TABLA,R).


