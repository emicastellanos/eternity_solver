package utilidades;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AnalizadorHermanos {

    public Tablero tablero;
    public ArrayList<Ficha> fichas;
    public HashMap<String, Integer> hermanosPorNivel;
    static final Logger resultLog = Logger.getLogger("analizadorLogger");

    public AnalizadorHermanos (int N){
        int colores = N-1;
        GeneradorFichas g = new GeneradorFichasUnicas(N,colores);
        tablero = new Tablero(N);
        fichas = g.getFichasUnicas();
        hermanosPorNivel = new HashMap<>();
    }

    public HashMap<String, Integer> getHermanosPorNivel() {
        return hermanosPorNivel;
    }

    public int avanzarNodo(Ficha ficha, int nivel){
        nivel = nivel + 1 ;
        ficha.setUsada(true);
        tablero.aumentarPosicion();
        return nivel;
    }

    public int retrocederNodo(Ficha ficha, int nivel){
        tablero.retrocederPosicion();
        ficha.setUsada(false);
        nivel-=1;
        return nivel;
    }

    public void contar (int nivel){
        int c = 0;
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        c++;
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
        if(c!=0){
            hermanosPorNivel.put(String.format("%1$2s", nivel ).replace(' ','0')+ "->" +tablero.toString(),c);
        }
    }

    public void back(int nivel){
        contar(nivel);
        for (Ficha f : fichas) {
            if (!f.isUsada()) {
                for (int i = 0; i < 4; i++) {
                    tablero.insertarFinal(f);
                    if (tablero.esSolucion()) {
                        if (tablero.esSolucionFinal()) {

                        } else {
                            nivel = avanzarNodo(f,nivel);
                            back(nivel);
                            nivel = retrocederNodo(f,nivel);
                        }
                    }
                    tablero.eliminarUltima();
                    f.rotar();
                }
            }
        }
    }

    public void recorrido(){
        back(0);
    }





    public static void main (String[] args){
        int N = 6; /*** TAMAÑO DEL TABLERO ***/
        AnalizadorHermanos analizador = new AnalizadorHermanos (N);
        analizador.recorrido();

        HashMap niveles = analizador.getHermanosPorNivel();
        ArrayList<String> mambo = new ArrayList(niveles.keySet());
        Collections.sort(mambo);
        for(String s: mambo){
            resultLog.info(s +" --> "+ niveles.get(s));
        }

    }
}
