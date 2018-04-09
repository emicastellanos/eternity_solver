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
    private long id ;


    /**Es necesario realmente que haya una referencia al manager ? Podria reemplazarse por el uso de
     * variables estaticas en el manager ? */
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
        id = manager.getNextContador();

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

    @Override
    public long getId() {
        return id;
    }

    public void backRichi(ArrayList<Ficha> fichas, Integer nivel){
        if(dividir && fichas.size()>1){ //porque si queda solo una ficha es mas facil que este thread termine
            //ademas si hay soo una ficha, el creador va a ser el que "encuentre" la solucion
            //resultLog.info(Thread.currentThread().getName() +" entro al dividir " + getNombre());
            this.estado = new Estado(tablero.clone(),ListUtils.getCopia(fichas),nivel);
            /**Quizas haya alguna forma de no crear una tarea si despues el thread padre
             * no va a conseguir una tarea para continuar**/
            /**Quizas se conveniente devolver mas de un estado.. una lista **/
            //desbloquea el manager
            dividir = false;
            bloqueado = true;
            manager.setBloqueado(0);
            resultLog.info(Thread.currentThread().getName() + " DESBLOQUEO AL MANAGER ");
            while (bloqueado){
                //do nothing
                resultLog.info("BackRichi() BLOQUEADO " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {

                }
            }
        }else{
            for (Ficha f : fichas) {
                if(!f.isUsada()) {
                    for(int i=0; i<4;i++) {
                        tablero.insertarFinal(f);
                        if(tablero.esSolucion()) {
                            if (tablero.esSolucionFinal()) {
                                resultLog.info(" ---------------- SE ENCONTRO UNA SOLUCION " + Thread.currentThread().getName());
                                Tablero resultado = tablero.clone();
                                Manager.SOLUCIONES.add(resultado);
                                /*String todas = "";
                                ArrayList<Ficha> todasFichas = resultado.getFichasUsadas();
                                for (Ficha ficha : todasFichas) {
                                    todas += String.valueOf(ficha.getId()) + " - ";
                                }
                                resultLog.info(todas);*/
                            } else {
                                nivel += 1;
                                f.setUsada(true);
                                tablero.aumentarPosicion();
                                backRichi(fichas, nivel);
                                tablero.retrocederPosicion();
                                f.setUsada(false);
                                nivel -= 1;
                            }
                        }
                        tablero.eliminarUltima();
                        f.rotar();
                    }
                }
            }
        }
        //resultLog.info("SALIENDO DEL BACK " + Thread.currentThread().getName());

    }
    
    @Override
    public void run(){
        resultLog.info("......... Comienza THREAD ACTUAL " + Thread.currentThread().getName() + " TAREA " + getNombre() + "con dividir = " + dividir);
        backRichi(fichas,nivelComienzo);
        finalizado = true;
        resultLog.info("......... Finalizo " + Thread.currentThread().getName());

        //SE PUSO ESTA CONSIDERACION PORQUE HABIA CASOS EN LOS QUE SE QUEDABA EL MANAGER BLOQUEADO
        // EN solicitarMas() Y SUPUSE QUE ERA PORQUE LA TAREA QUE TOMO PARA SUBDIVIDIR HABIA CONDUCE
        // A UN CAMINO QUE NUNCA VA A ENCONTRAR ALGUNA FICHA QUE ENCAJE DE SUS SIGUIENTES (o ya termino)

        if(manager.getBloqueado()== this.getId() && dividir){
            System.out.println(Thread.currentThread().getName() + " TENIA QUE DIVIDIR PERO TERMINO, DESBLOQUEA EL MANAGER");
            manager.setBloqueado(0);
        }
    }
}
