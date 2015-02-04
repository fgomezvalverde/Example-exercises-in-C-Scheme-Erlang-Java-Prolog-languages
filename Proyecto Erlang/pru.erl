-module(pru).
-export([fact/1,agru/1]).

fact(1) -> 1;
fact(N) -> N * fact(N-1).

agru([]) -> [];
agru([X,Y|LISTA]) -> [[X,Y]]++agru(LISTA).