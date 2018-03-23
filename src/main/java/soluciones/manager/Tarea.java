package soluciones.manager;

import entidades.Ficha;
import entidades.Tablero;
import org.apache.log4j.Logger;
import utilidades.ListUtils;

import java.util.ArrayList;
import java.util.Date;

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
    private Manager manager;


    public Tarea(Tablero tablero, ArrayList<Ficha> fichas, String nombre,Integer nivel,Manager manager) {
        this.tablero = tablero;
        this.nivelComienzo = nivel;
        this.finalizado = false;
        this.dividir = false;
        this.fichas = ListUtils.getCopia(fichas);
        this.nombre = "Thread actual:" + currentThread().getName() + " va a correr en " + getName() + " TAREA " + nombre;
        String todas ="";
        ArrayList<Ficha> todasFichas = tablero.getFichasUsadas();
        for(Ficha ficha : todasFichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        /*resultLog.info(this.nombre + " (tablero): " + todas);
        todas="";
        for(Ficha ficha : fichas){
            todas+= String.valueOf(ficha.getId()) + " - ";
        }
        resultLog.info(this.nombre + " (para usar): " + todas);*/
        NRO++;
        this.manager = manager;

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
        if(dividir && fichas.size()>1){ //porque si queda solo una ficha es mas facil que este thread termine
            //ademas si hay soo una ficha, el creador va a ser el que "encuentre" la solucion
            //resultLog.info(Thread.currentThread().getName() +" entro al dividir " + getNombre());
            this.estado = new Estado(tablero.clone(),fichas,nivel);
            //1) desbloquea el manager
            dividir = false;
            bloqueado = true;
            manager.setBloqueado(false);
            resultLog.info(Thread.currentThread().getName() +" DESBLOQUEO AL MANAGER " );
            while (bloqueado){
                //do nothing
                int i;
                resultLog.info("BackRichi() BLOQUEADO " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                }
                /*resultLog.warn("BackRichi() BLOQUEADO " + getNombre());
                resultLog.warn("BackRichi() BLOQUEADO " + getName());*/
            }
            //resultLog.info(Thread.currentThread().getName() +" ya dividio");
        }else{
            for (Ficha f : fichas) {
                tablero.insertarFinal(f);
                if(tablero.esSolucion()) {
                    if (tablero.esSolucionFinal()) {
                        resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                        Tablero resultado = tablero.clone();
                        if(resultado==null){
                            System.out.println("asd");
                        }
                        Manager.SOLUCIONES.add(resultado);
                        /*String todas = "";
                        ArrayList<Ficha> todasFichas = resultado.getFichasUsadas();
                        for (Ficha ficha : todasFichas) {
                            todas += String.valueOf(ficha.getId()) + " - ";
                        }
                        resultLog.info(todas);*/
                    } else {
                        ArrayList<Ficha> aux = new ArrayList<Ficha>();
                        for (Ficha e : fichas) {
                            if (e.getId() != f.getId()) {
                                aux.add(e);
                            }
                        }
                        nivel += 1;
                        tablero.aumentarPosicion();
                        backRichi(aux, nivel);
                        tablero.retrocederPosicion();
                        nivel -= 1;
                    }
                }
                tablero.eliminarUltima();
            }
        }
        //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());

    }
    
    @Override
    public void run(){
        resultLog.info("......... Comienza " + Thread.currentThread().getName());
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

        //
        if(manager.isBloqueado() && dividir){
            System.out.println(Thread.currentThread().getName() + " TENIA QUE DIVIDIR PERO TERMINO, DESBLOQUEA EL MANAGER");
            manager.setBloqueado(false);
        }
    }
}
