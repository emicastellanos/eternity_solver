package soluciones.reduccion_threads;

import java.util.List;

/**
 * ESTA IMPLEMENTACION DEJA DORMIDO AL MANAGER MIENTRAS EXISTAN TAREAS SIN FINALIZAR (ni pendientes)
 **/
public class ManagerSinDividir extends ManagerAbs {

    public ManagerSinDividir (String tipo,int n, int c, int h, int b, int des){
        super(tipo,n,c,h,b,des);
    }
    
    public void iniciarTareas(){
        for (TareaAbs tarea :getAllThreads()){
            if(!tarea.isAlive()){
                ((TareaDFSinDividir)tarea).start();
            }
        }
    }
    
    public void esperarParaTerminar(){}

    /**como no se va a dividir, la idea es que el manager termine si todas las tareas pendientes estan siendo usadas o si ya terminaron su trabajo*/
    public void logicaDivisiones(){}

    public static void main(String[] args){
    	int n = 8;
        int c = 8;
        int h = 12;
        int b = 5;
        int des = 1;

        if(args.length == 5){
            n = Integer.valueOf(args[0]);
            c = Integer.valueOf(args[1]);
            h = Integer.valueOf(args[2]);
            b = Integer.valueOf(args[3]);
            des = Integer.valueOf(args[4]);
        }
        ManagerAbs m = new ManagerSinDividir("DFSin",n,c,h,b,des);
        m.start();
    }
}
