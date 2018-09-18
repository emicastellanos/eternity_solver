package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.util.ArrayList;

public class TareaDFS extends TareaAbs {

    public TareaDFS (Estado estado, Manager m){
        super(estado,m);

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
        }else{
            for (Ficha f : fichas) {
                if(!f.isUsada()) {
                    for(int i=0; i<4;i++) {
                        tablero.insertarFinal(f);
                        //sumar();

                        if(tablero.esSolucion()) {
                            if (tablero.esSolucionFinal()) {
                                resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                                Tablero resultado = tablero.clone();
                                Manager.SOLUCIONES.add(resultado);
                            } else {
                                nivel += 1;
                                f.setUsada(true);
                                tablero.aumentarPosicion();
                                backRichi(fichas, nivel);
                                tablero.retrocederPosicion();
                                f.setUsada(false);
                                nivel -= 1;
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
