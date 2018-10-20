package soluciones.reduccion_threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ManagerListaEspera extends ManagerAbs{

    public boolean flagDividir;
    public static List<TareaListaEspera> listaEspera;

    public ManagerListaEspera (){
        super ("LS");
        listaEspera = Collections.synchronizedList(new ArrayList<>());
        flagDividir = false;
    }


    public void setFlagDividir(boolean flagDividir) {
        this.flagDividir = flagDividir;
    }

    public boolean getFlagDividir(){
        return flagDividir;
    }

    public synchronized void insertarEnListaEspera(TareaListaEspera t){
        listaEspera.add(t);
        //TODO eliminar linea
        System.out.println("inserta en lista espera, cant :"+listaEspera.size());
    }

    public void despertarWorkers(){
        ArrayList<TareaListaEspera> borrar = new ArrayList<>();
        int t = pendientes.size() - indice;
        for(int i=0 ; i<t; i++){
            borrar.add(listaEspera.get(i));
        }

        listaEspera.removeAll(borrar);

        for(int i=0; i< borrar.size(); i++){
            (borrar.get(i)).despertar();
        }
        //TODO eliminar linea
        System.out.println("se borran de lista espera :"+borrar.size());

    }

    public void dormir(int l){
        try {
            wait(l);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public void logicaDivisiones() {

        while (hayNodosParaExplorar() || ( !hayNodosParaExplorar() && hayHilosTrabajando())) { // CONDICION DE QUE NO HAYA EXPOLORADO TODAS LAS POSICIONES
            if (listaEspera.size() >= getCantHilosParalelos() * Double.parseDouble("0.5")) { // CONVIENE GENERAR MAS TRABAJO ?
                setFlagDividir(true); //LUEGO, LOS HILOS QUE HAYAN QUEDADO TRABAJANDO SE VAN A PONER A BUSCAR MAS TAREAS

                dormir(1000);
                if(hayNodosParaExplorar()){
                    despertarWorkers();
                    setFlagDividir(false);
                }

            } else {
                dormir(300);
            }
        }
    }

    public static void main(String [] args){
        ManagerAbs managerAbs = new ManagerListaEspera();
        managerAbs.start();
    }
}
