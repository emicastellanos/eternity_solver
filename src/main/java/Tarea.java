import java.util.ArrayList;
import java.util.List;

public class Tarea  implements Runnable{

    private Tablero tablero;
    public boolean encontro = false;
    public static ArrayList<Ficha> fichas;
    public static Boolean[] libres;
    public int nivel;

    public Tarea(Tablero tablero,Boolean[] libres,int nivel, ArrayList<Ficha> fichas) {
        this.tablero = tablero;
        this.nivel = nivel;
        this.libres = libres;
        this.fichas =  fichas;
    }

    public void back(List<Ficha> fichas,Ficha f, Integer nivel){

        List<Ficha> aux ;
        tablero.insertarFinal(f);

        if(tablero.esSolucionFinal()&& tablero.esSolucion()){
            encontro=true;
        }
        else{
            if(tablero.esSolucion()){
                for(Ficha proxima : fichas){
                    aux = new ArrayList<Ficha>();
                    for(Ficha e: fichas)
                        if(e.getId()!=proxima.getId())
                            aux.add(e);
                    if(!encontro) {
                        nivel+=1;
                        back(aux, proxima, nivel);
                        nivel-=1;
                    }
                }
            }
        }
        tablero.eliminarUltima();

    }

    @Override
    public void run() {
        List<Ficha> aux ;
        for(Ficha f: fichas){
            aux = new ArrayList<Ficha>();
            for(Ficha e: fichas)
                if(e.getId()!=f.getId())
                    aux.add(e);
            if(!encontro)
                back(aux,f,nivel);
        }
    }
}
