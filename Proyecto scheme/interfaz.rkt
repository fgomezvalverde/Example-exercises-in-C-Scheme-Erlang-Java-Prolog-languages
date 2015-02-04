
(require htdp/draw)


(require lang/posn)
(start/cartesian-plane 800 800)

(define (conteo) 0.5)

(define (grafica funcion)
    (grafica_aux_pos funcion 0)
    (grafica_aux_neg funcion 0)
    )

(define (grafica_aux_pos funcion x)
    (cond [(= x 200) 0]
          [else
           (draw-solid-line  (make-posn (+ x 400) (- 400 (funcion x))) (make-posn (+ x (+ 400 (conteo))) (- 400 (funcion (+ x (conteo))))) 'red)
           (print (funcion x))
           (display "\n")
           (grafica_aux_pos funcion (+ x (conteo)))
           ]))

(define (grafica_aux_neg funcion x)
    (cond [(= x -200) 0]
          [else
           (draw-solid-line  (make-posn (+ x 400) (- 400 (funcion x))) (make-posn (+ x (+ 400 (conteo))) (- 400 (funcion (- x (conteo))))) 'red)
           (print (funcion x))
           (display "\n")
           (grafica_aux_neg funcion (- x (conteo)))
           ]))
  



(grafica (eval '(lambda (x)  (expt x 2))))
