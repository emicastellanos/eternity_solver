package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.ListUtils;

import java.util.ArrayList;

public class Tarea extends Thread {
    public static int NRO = 1;
    private boolean finalizado; // cuando llega al final del metodo run ()
    private Tablero tablero;
    private ArrayList<Ficha> fichas;
    private Integer nivelComienzo;
    private boolean dividir;
    private boolean bloqueado;
    private Estado estado;
    private String nombre;
    static final Logger resultLog = Logger.getLogger("resultadoLogger");


    public Tarea(Tablero tablero, ArrayList<Ficha> fichas, String nombre,Integer nivel) {
        this.tablero = tablero;
        this.nivelComienzo = nivel;
        this.finalizado = false;
        this.dividir = false;
        this.fichas = ListUtils.getCopia(fichas);
        String todas ="";
        for(Ficha ficha : fichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        this.nombre = "TAREA " + nombre;
        resultLog.info(this.nombre + " : " + todas);
        NRO++;

    }

    public String getNombre() {
        return nombre;
    }

    public void setDividir (boolean b){
        dividir = b;
    }

    public void setBloqueado(boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public boolean isBloqueado() {
        return bloqueado;
    }

    public boolean isDividir() {
        return dividir;
    }

    public Estado getEstado() {
        return estado;
    }

    public boolean isFinalizado(){
        return finalizado;
    }


    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
        if(dividir ){
            resultLog.info("entro al dividir " + getNombre());
            this.estado = new Estado(tablero.clone(),fichas,nivel);
            //1) desbloquea el manager
            dividir = false;
            Manager.setBloqueado(false);
            /*while (bloqueado){
                //do nothing
                resultLog.warn("BackRichi() BLOQUEADO " + Thread.currentThread().getName());
                resultLog.warn("BackRichi() BLOQUEADO " + getNombre());
                resultLog.warn("BackRichi() BLOQUEADO " + getName());
            }*/
            resultLog.info("ya dividio");
        }else{
            for (Ficha f : fichas) {
                tablero.insertarFinal(f);
                if(tablero.esSolucionFinal() && tablero.esSolucion()){
                    resultLog.info("SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                    Tablero resultado = tablero.clone();
                    Manager.SOLUCIONES.add(resultado);
                    String todas ="";
                    ArrayList<Ficha> todasFichas = resultado.getFichasUsadas();
                    for(Ficha ficha : todasFichas){
                        todas+= String.valueOf(ficha.getId()) + " - ";
                    }
                    resultLog.info(todas);
                }
                else{
                    if (tablero.esSolucion() ) {
                        ArrayList<Ficha> aux = new ArrayList<Ficha>();
                        for (Ficha e : fichas) {
                            if (e.getId() != f.getId()) {
                                aux.add(e);
                            }
                        }
                        nivel += 1;
                        backRichi(aux, nivel);
                        nivel -= 1;
                    }
                }
                tablero.eliminarUltima();
            }
        }
        resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());

    }
    
    @Override
    public void run(){
        backRichi(fichas,nivelComienzo);
        finalizado = true;
        resultLog.info("......... Finalizo " + Thread.currentThread().getName());
        //esto.. mmm
        //SE PUSO ESTA CONSIDERACION PORQUE HABIA CASOS EN LOS QUE SE QUEDABA EL MANAGER BLOQUEADO
        // EN solicitarMas() Y SUPUSE QUE ERA PORQUE LA TAREA QUE TOMO PARA SUBDIVIDIR HABIA CONDUCE
        // A UN CAMINO QUE NUNCA VA A ENCONTRAR ALGUNA FICHA QUE ENCAJE DE SUS SIGUIENTES (o ya termino)
        /*if(dividir == true){
            resultLog.info("......... SE DIO EL CASO FLASHERO          " + Thread.currentThread().getName());
            Manager.setBloqueado(false);
        }*/

        if(Manager.isBloqueado()){
            System.out.println("ENTRA");
            Manager.setBloqueado(false);
        }
    }
}
