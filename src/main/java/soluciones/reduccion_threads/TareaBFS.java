package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.util.ArrayList;

public class TareaBFS extends TareaAbs {

    public TareaBFS() { }

    public TareaBFS (ManagerAbs m){
        super(m);
    }

    public TareaBFS (Estado estado, ManagerAbs m){
        super(estado,m);
    }

    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
       boolean lockDividir = false;
       ArrayList<Estado> subTareas = new ArrayList<>();
       for (int i=0; i<fichas.size(); i++) {
           Ficha f = fichas.get(i);
           if(!f.isUsada()) {
               for(int rotacion=0; rotacion<4;rotacion++) {
                   tablero.insertarFinal(f);
                   if(tablero.esSolucion()) {
                       if (tablero.esSolucionFinal()) {
                           resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                           Tablero resultado = tablero.clone();
                           ManagerAbs.SOLUCIONES.add(resultado);
                       } else {
                           nivel += 1;
                           f.setUsada(true);
                           tablero.aumentarPosicion();
                           //sincronizar este pedacito
                           if (!lockDividir && isDividir()) {
                               setDividir(false);
                               lockDividir = true;
                           }
                           if (lockDividir) {
                               subTareas.add(new Estado(tablero.clone(), Utils.getCopia(fichas), nivel));
                               //por que no ir agregando de una ? esto es acceso a zona sincronizada
                               //ManagerAbs.addEstado(new Estado(tablero.clone(), Utils.getCopia(fichas),nivel));
                           } else {
                               backRichi(fichas, nivel);
                           }
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
       if (lockDividir){
           ManagerAbs.addAllEstado(subTareas);
       }
    }
    //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());
}
