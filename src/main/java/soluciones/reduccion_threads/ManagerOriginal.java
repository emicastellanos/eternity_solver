package soluciones.reduccion_threads;

public class ManagerOriginal extends ManagerAbs {

    public void logicaDivisiones(){
        while (cantActivas()>0 || pendientes.size() > indice){
            if(cantActivas() < hilosParalelos && pendientes.size() == indice) { // si hay pendientes no divido
                TareaAbs.setDividir(true);
                cantdivisiones+=1;
                //resultLog.info("managerAbs setea dividir");
            }

            try {
                wait();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}