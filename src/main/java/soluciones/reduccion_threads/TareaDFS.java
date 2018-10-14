package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.util.ArrayList;

public class TareaDFS extends TareaAbs {

    public TareaDFS (Estado estado, ManagerAbs m){
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
            ManagerAbs.addEstado(new Estado(tablero.clone(), Utils.getCopia(fichas),nivel));
            Integer cantidad = ManagerAbs.interrupciones.get(Thread.currentThread().getName());
            if(cantidad == null){
                ManagerAbs.interrupciones.put(Thread.currentThread().getName(), 1);
            }else{
                ManagerAbs.interrupciones.put(Thread.currentThread().getName(), ++cantidad);
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
                                ManagerAbs.SOLUCIONES.add(resultado);
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
