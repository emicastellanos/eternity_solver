package soluciones.reduccion_threads;

import java.util.List;

/**
 * ESTA IMPLEMENTACION DEJA DORMIDO AL MANAGER MIENTRAS EXISTAN TAREAS SIN FINALIZAR (ni pendientes)
 **/
public class ManagerSinDividir extends ManagerAbs {

    public boolean hayTareas(){
        List<TareaAbs> threads = getAllThreads();
        for(TareaAbs thread : threads){
            if (!thread.isFinalizado()){
                return true;
            }
        }

        return pendientes.size() > indice;
    }

    public void logicaDivisiones(){
        while (hayTareas()){
            try {
                wait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
