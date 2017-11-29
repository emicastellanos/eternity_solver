package soluciones.master_slave;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.GeneradorFichas;
import utilidades.LoggerCustom;

import java.util.ArrayList;


public class MasterSlaveMain {

    private static final int N = 3;
    static final Logger fichasLog = Logger.getLogger("fichasLogger");

    public static void main (String[] args){

        GeneradorFichas generadorFichas = new GeneradorFichas(N);

        ArrayList<Ficha> fichas = generadorFichas.getFichasUnicas();
        for (Ficha f : fichas) {
            fichasLog.info("F" + f.getId() + ": " + f.imprimirse());
        }

        Maestro maestro = new Maestro(new Tablero(N), fichas);
        maestro.generarHilos(2);


    }
}