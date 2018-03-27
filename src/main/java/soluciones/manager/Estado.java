package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class Estado {
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Integer nivel;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public Estado (Tablero t, ArrayList<Ficha> fichas, Integer nivel){
        this.tablero = t;
        this.nivel = new Integer(nivel);
        this.fichas = fichas;
        String todas ="";
        ArrayList<Ficha> todasFichas = tablero.getFichasUsadas();
        for(Ficha ficha : todasFichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        resultLog.info(Thread.currentThread().getName() + " ESTADO (tablero): " + todas);
        todas="";
        for(Ficha ficha : this.fichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        resultLog.info(Thread.currentThread().getName() + " ESTADO  (para usar): " + todas);
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
