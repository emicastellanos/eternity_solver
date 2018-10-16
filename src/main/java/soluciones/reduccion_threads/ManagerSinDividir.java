package soluciones.reduccion_threads;

import java.util.List;

/**
 * ESTA IMPLEMENTACION DEJA DORMIDO AL MANAGER MIENTRAS EXISTAN TAREAS SIN FINALIZAR (ni pendientes)
 **/
public class ManagerSinDividir extends ManagerAbs {

    public boolean hayTareas(){
        return pendientes.size()<indice;
    }

    //ESTE METODO NOS DICE TRUE O FALSE DEPENDIENDEO DE SI HAY ALGUN THREAD QUE TIENE UNA SETEADA QUE TODAVIA NO INICIALIZO (ni siquiera la empezo a trabajar)
    public boolean todosInicializados(){
        List<TareaAbs> tareas = getAllThreads();
        for (TareaAbs tarea : tareas){
            if (!tarea.isInicializado()) {
                return false;
            }
        }
        return true;
    }

    /**como no se va a dividir, la idea es que el manager termine si todas las tareas pendientes estan siendo usadas o si ya terminaron su trabajo*/
    public void logicaDivisiones(){

        while (hayTareas() || ( !hayTareas() && !todosInicializados ())){
            try {
                wait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ManagerAbs m = new ManagerSinDividir();
        m.start();
    }
}
