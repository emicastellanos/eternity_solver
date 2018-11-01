package soluciones.reduccion_threads;

import java.util.List;

/**
 * ESTA IMPLEMENTACION DEJA DORMIDO AL MANAGER MIENTRAS EXISTAN TAREAS SIN FINALIZAR (ni pendientes)
 **/
public class ManagerSinDividir extends ManagerAbs {

    public ManagerSinDividir (String tipo,int n, int c, int h, int b, int des){
        super(tipo,n,c,h,b,des);
    }



    /**como no se va a dividir, la idea es que el manager termine si todas las tareas pendientes estan siendo usadas o si ya terminaron su trabajo*/
    public void logicaDivisiones(){

        while (hayNodosParaExplorar() || ( !hayNodosParaExplorar() && !todosInicializados ())){
            try {
                wait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ManagerAbs m = new ManagerSinDividir("DFS",7,7,4,3,0);
        m.start();
    }
}
