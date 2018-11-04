package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class TareaListaEspera extends TareaAbs {

    public Double porcentaje;

    public TareaListaEspera (ManagerAbs m){
        super(m);
        porcentaje = new Double(0);
    }

    public TareaListaEspera(Estado estado, ManagerAbs m){
        super(estado,m);
        porcentaje = new Double(0);

    }

    public Double getPorcentaje() {
        return porcentaje;
    }

    public void contarInterrupciones(){
        Integer cantidad = ManagerAbs.interrupciones.get(Thread.currentThread().getName());
        if(cantidad == null){
            ManagerAbs.interrupciones.put(Thread.currentThread().getName(), 1);
        }else{
            ManagerAbs.interrupciones.put(Thread.currentThread().getName(), ++cantidad);
        }
    }

    //BUSQUEDA DE NODOS HERMANOS
    private ArrayList<Estado> proximosNodosAnalizar(ArrayList<Ficha> fichas, Integer nivel){
        ArrayList<Estado> nuevosEstados = new ArrayList<>();
        for (Ficha f : fichas) {
            if(!f.isUsada()) {
                for(int i=0; i<4;i++) {
                    tablero.insertarFinal(f);
                    if(tablero.esSolucion()) {
                        avanzarNodo(f, nivel);

                        nuevosEstados.add(new Estado(tablero.clone(), Utils.getCopiaFichasSinUsar(fichas), nivel));

                        retrocederNodo(f, nivel);
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }

        return nuevosEstados;
    }

    public boolean convieneGenerarTrabajo(Integer nivel){
        return nivel <= tablero.getCantidadNivelesFinal() - 2 * tablero.getTamano();
    }

    public void modificarPorcentajeDeExploracion(int pos){
        int div = pos +1 ;
        porcentaje = new Double(div / fichas.size());
    }


    public void backDFS ( Integer nivel){
        for (int pos=0; pos<fichas.size();pos++){
            Ficha f = fichas.get(pos);

            if(!f.isUsada()) {
                modificarPorcentajeDeExploracion(pos);
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

    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){

        if(((ManagerListaEspera)managerAbs).getFlagDividir() == true && convieneGenerarTrabajo(nivel)){

            contarInterrupciones();
            ArrayList<Estado> masTareas = proximosNodosAnalizar(fichas,nivel);
            if(masTareas.size()>0){
                managerAbs.addAllEstado(masTareas);
            }

        }else{
            backDFS(nivel);
            //BACK DFS COMO LO CONOCEMOS
        }
    }

    public synchronized void dormir(){
        try {
            wait();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }
    public synchronized void despertar() {
        notify();
    }

    @Override
    public void run(){
        ((ManagerListaEspera)managerAbs).setAlgunaTareaComenzada(true);

        while (ManagerAbs.TIENE_TAREAS) {
            if (getActual() != null) {
                inicializar();
                long startTime = System.nanoTime();
                backRichi(this.fichas, nivelComienzo);
                long endTime = System.nanoTime();

                BigDecimal duration = new BigDecimal((endTime - startTime));
                BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
                MEDICIONES_LOGGER.info(Thread.currentThread().getName() +" TIEMPO CORRIENDO " + nombreTarea  +" : "+ durationSecs.toString().replace('.', ',') + " SEGUNDOS EN ALGUN core ");
                finalizado = true;
            }else {
                ((ManagerListaEspera)managerAbs).insertarEnListaEspera(this);
                dormir();
            }
            setActual(ManagerAbs.getProximoEstado());
        }
        //MEDICIONES_LOGGER.info("TIEMPO DE CPU " + Thread.currentThread().getName() + " TAREA " + nombreTarea + " : " + (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP).toString().replace('.', ','));
        managerAbs.despertar(); // SOLO SIRVE PARA DESPERTAR EL MANAGER QUE SE QUEDO EN "ESPERAR PARA TERMINAR"
    }
}
