package soluciones.reduccion_threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManagerListaEspera extends ManagerAbs{

    public boolean flagDividir;
    public static List<TareaListaEspera> listaEspera;
    public boolean algunaTareaComenzada;

    public ManagerListaEspera (int n, int c, int h, int b, int des){
        super ("LS",n,c,h,b,des);
        listaEspera = Collections.synchronizedList(new ArrayList<>());
        flagDividir = false;
    }

    public boolean isAlgunaTareaComenzada() {
        return algunaTareaComenzada;
    }

    public void setAlgunaTareaComenzada(boolean algunaTareaComenzada) {
        this.algunaTareaComenzada = algunaTareaComenzada;
    }

    public List<TareaListaEspera> getListaEspera(){
        return listaEspera;
    }

    public void setFlagDividir(boolean flagDividir) {
        this.flagDividir = flagDividir;
    }

    public boolean getFlagDividir(){
        return flagDividir;
    }

    public static synchronized void insertarEnListaEspera(TareaListaEspera t){
        listaEspera.add(t);
        //TODO eliminar linea
        //System.out.println("inserta en lista espera, cant :"+listaEspera.size());
    }

    /**
     * se despiertan todos los hilos posibles de la lista de espera segun la cantidad de tareas pendientes que exista
     **/
    public void despertarWorkers(){
        ArrayList<TareaListaEspera> borrar = new ArrayList<>();
        int t = pendientes.size() - indice;
        if(t > listaEspera.size()){
            t = listaEspera.size();
        }
        for(int i=0 ; i<t; i++){
            borrar.add(listaEspera.get(i));
        }

        listaEspera.removeAll(borrar);

        for(int i=0; i< borrar.size(); i++){
            (borrar.get(i)).despertar();
        }
        //TODO eliminar linea
        MEDICIONES_LOGGER.info("se borran de lista espera :"+borrar.size());

    }

    public void dormir(int l){
        /** l son milisegundos **/
        try {
            wait(l);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**Cada tarea puede calcular su porcentaje de explorado en cada iteracion. Se consideran nodos prometedores (es decir que pueden considerarse a la hora de dividir)
     * a aquellos nodos que tengan un porcentaje menor al pasado por parametro**/
    public boolean hayNodosPrometedores(Double p){
        for(TareaAbs tarea : getAllThreads()){
            if (tarea.isInicializado() && ((TareaListaEspera)tarea).getPorcentaje() < p && !tarea.isFinalizado()) {
                return true;
            }
        }
        return false;
    }

    public boolean hayNodosParaExplorar(){
        return pendientes.size() - indice >= 1;
    }


    @Override
    public void logicaDivisiones() {

        //TODAS LAS TAREAS COMENZARON y HAY TAREAS QUE CONVIENE DIVIDIR O VER QUE ONDA
        while(!isAlgunaTareaComenzada() || (isAlgunaTareaComenzada() && hayNodosPrometedores(new Double(0.85)))) {
            if (listaEspera.size() >= getCantHilosParalelos() * new Double(0.1)) { // CONVIENE GENERAR MAS TRABAJO ?
                setFlagDividir(true); //LUEGO, LOS HILOS QUE HAYAN QUEDADO TRABAJANDO SE VAN A PONER A BUSCAR MAS TAREAS
                MEDICIONES_LOGGER.info("MARCADO COMO SITUACION DE DIVIDIR");
                dormir(100);
                if(hayNodosParaExplorar()){
                    despertarWorkers();
                    setFlagDividir(false);
                    //dormir(1000);
                }

            } else {
                dormir(1000);
            }
        }
    }

    public static void main(String [] args){
        int n = 7;
        int c = 7;
        int h = 4;
        int b = 3;
        int des = 0;

        if(args.length == 5){
            n = Integer.valueOf(args[0]);
            c = Integer.valueOf(args[1]);
            h = Integer.valueOf(args[2]);
            b = Integer.valueOf(args[3]);
            des = Integer.valueOf(args[4]);
        }

        ManagerAbs managerAbs = new ManagerListaEspera(n,c,h,b,des);
        managerAbs.start();
    }
}
