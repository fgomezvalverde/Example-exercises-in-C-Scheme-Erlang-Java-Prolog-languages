largo([],0).
largo([_H|T], X):-
	largo(T,X1),
	X is X1+1.

suma([],0).
suma([H|T],X):-
	suma(T,X1),
	X is X1+H.

miembro(Elem,[Elem|_T]).
miembro(Elem,[_H|T]):-miembro(Elem,T).

promedio([],0).
promedio(L,X):- suma(L,Sum),largo(L,Largo),X is Sum/Largo.


mapoper([],_Prom,[]).
mapoper([H|T], Prom, [Z|T2]):-
	oper(H,Prom,Z), mapoper(T,Prom,T2).

oper(X,Y,Z):-ZP is X-Y,Z is ZP*ZP.

varianza(L,X):-promedio(L,Prom),
		mapoper(L,Prom,Zetas),
		promedio(Zetas,X).

sumatoria(0,0):-!.
sumatoria(N,RESULTADO) :-
	N1 is N-1,
	sumatoria(N1,RESUL1),
	RESULTADO is RESUL1+N.


sumalista([],_,R).
sumalista([X1|T1],[X2|T2],R) :-
	sumalista(T1,T2,R1),
	temp is X1+X2,
	R is [X1+X2].


% Esto es un comentario

fibo(1,1):-!.
fibo(0,0):-!.
fibo(X,Y):-X1 is X-1,
	X2 is X-2,
	fibo(X1,Y1),
	fibo(X2,Y2),
	Y is Y1+Y2.

%% Tarea: Leer "Padre Rico, Padre Pobre" Robert Kiyosaki

fibonacci(1,1):-!.
fibonacci(0,0):-!.
fibonacci(X,Y):-fibonacci(X,1,1,0,Y).

fibonacci(X,X,Y,_P,Y):-!.
fibonacci(X,I,Ult,Pen,Y):-
	INext is I+1,
	UltNext is Ult+Pen,
	fibonacci(X,INext,UltNext,Ult,Y).

pares([],[],[]):-!.
pares([H1|T1],[H2|T2],[[H1,H2]|T]):-pares(T1,T2,T).

factores(Num,[1|X]):-factores(Num,2,X).

factores(Num,Num,[Num]):-!.
factores(Num,X,[X|Resto]):- 0 is Num mod X ,!, X1 is X+1,			factores(Num,X1,Resto).
factores(Num,X,Resto):- X1 is X+1, factores(Num,X1,Resto).

cuad(X,Y):-Y is X*X.

map(_Rel,[],[]):-!.
map(Rel,[H1|T1],[H2|T2]):-call(Rel,H1,H2), map(Rel,T1,T2).


contar([],[]):-!.
contar([H|T],X):-contar(T,H,1,X).

contar([],H,ContAct,[[H,ContAct]]):-!.
contar([H|T],H,ContAct,X):-ContFinal is ContAct+1,
		!, contar(T,H,ContFinal,X).
contar([H1|T],H,ContAct,[[H,ContAct]|X]):-
	contar(T,H1,1,X).


factorPrima(Num,X):-factorPrima(Num,2,XTemp),
		contar(XTemp,X).

factorPrima(1,_,[]):-!.
factorPrima(Num,Fact,[Fact|X]):-0 is Num mod Fact,!,
				N is Num/Fact,
				N1 is round(N),
				factorPrima(N1,Fact,X).
factorPrima(Num,Fact,X):-F1 is Fact+1,
			factorPrima(Num,F1,X).























