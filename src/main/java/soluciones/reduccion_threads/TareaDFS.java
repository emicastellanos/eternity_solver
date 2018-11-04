package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.util.ArrayList;

public class TareaDFS extends TareaAbs {

    public TareaDFS (Estado estado, ManagerAbs m){
        super(estado,m);

    }

    @Override
    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
        boolean encontro = false;
        synchronized (this){
            if(isDividir() && fichas.size()>1){
                this.setDividir(false);
                //MEDICIONES_LOGGER.info("FLAG DIVIDIR = FALSE");
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
            backDFS(nivel);
        }

    }
}
