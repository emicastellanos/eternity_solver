package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import utilidades.Utils;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class TareaDFSinDividir extends TareaAbs {

    public TareaDFSinDividir (Estado estado, ManagerAbs m){
        super(estado,m);

    }

    @Override
    public void run(){

        long startTime = managerAbs.getTiempoInicial();

        while (actual!=null) {
            inicializar();
            backRichi(this.fichas,nivelComienzo);
            actual = ManagerAbs.getProximoEstado();
        }
        long endTime = System.nanoTime();
        //MEDICIONES_LOGGER.info("TIEMPO DE CPU " + Thread.currentThread().getName() + " TAREA " + nombreTarea + " : " + (new BigDecimal(ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime()).divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP).toString().replace('.', ','));

        BigDecimal duration = new BigDecimal((endTime - startTime));
        BigDecimal durationSecs = (duration.divide(new BigDecimal(1000000000))).setScale(3, RoundingMode.HALF_UP);
        MEDICIONES_LOGGER.info(Thread.currentThread().getName() +" TIEMPO TOTAL " + nombreTarea  +" : "+ durationSecs.toString().replace('.', ',') + " SEGUNDOS ");
    }

	@Override
	public void backRichi(ArrayList<Ficha> fichas, Integer nivel) {

		backDFS(nivelComienzo);
		
	}
}
