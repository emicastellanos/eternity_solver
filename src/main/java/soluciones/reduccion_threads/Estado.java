package soluciones.reduccion_threads;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Estado {
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Integer nivel;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public Estado(Tablero t, ArrayList<Ficha> fichas, Integer nivel){
        this.tablero = t;
        this.nivel = new Integer(nivel);
        this.fichas = fichas;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public ArrayList<Ficha> getFichas() {
        return fichas;
    }

    public Integer getNivel() {
        return nivel;
    }
}
