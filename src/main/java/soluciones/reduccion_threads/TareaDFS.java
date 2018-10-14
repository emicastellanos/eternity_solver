package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.util.ArrayList;

public class TareaDFS extends TareaAbs {

    public TareaDFS (Estado estado, Manager m){
        super(estado,m);

    }

    public void avanzarNodo(Ficha ficha, Integer nivel){
        nivel += 1;
        ficha.setUsada(true);
        tablero.aumentarPosicion();
    }

    public void retrocederNodo(Ficha ficha, Integer nivel){
        tablero.retrocederPosicion();
        ficha.setUsada(false);
        nivel -= 1;
    }

    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
        boolean encontro = false;
        synchronized (this){
            if(isDividir() && fichas.size()>1){
                this.setDividir(false);
                encontro = true;
            }
        }
        if(encontro){
            Manager.addEstado(new Estado(tablero.clone(), Utils.getCopia(fichas),nivel));
            //resultLog.info("dividi" + Thread.currentThread().getName());
            Integer cantidad = Manager.interrupciones.get(Thread.currentThread().getName());
            if(cantidad == null){
                Manager.interrupciones.put(Thread.currentThread().getName(), 1);
            }else{
                Manager.interrupciones.put(Thread.currentThread().getName(), ++cantidad);
            }
        }else{
            for (Ficha f : fichas) {
                if(!f.isUsada()) {
                    for(int i=0; i<4;i++) {
                        tablero.insertarFinal(f);
                        if(tablero.esSolucion()) {
                            if (tablero.esSolucionFinal()) {
                                resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                                Tablero resultado = tablero.clone();
                                Manager.SOLUCIONES.add(resultado);
                            } else {
                                avanzarNodo(f,nivel);
                                backRichi(fichas, nivel);
                                retrocederNodo(f,nivel);
                            }
                        }
                        tablero.eliminarUltima();
                        f.rotar();
                    }
                }
            }
        }
        //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());
    }
}
