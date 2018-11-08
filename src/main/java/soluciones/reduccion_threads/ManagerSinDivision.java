package soluciones.reduccion_threads;

public class ManagerSinDivision extends ManagerAbs {

    public ManagerSinDivision(String tipo, int n, int c, int h, int b, int des){
        super(tipo,n,c,h,b,des);
    }
    
    public void iniciarTareas(){
        for (TareaAbs tarea :getAllThreads()){
            if(!tarea.isAlive()){
                ((TareaDFSinDividir)tarea).start();
            }
        }
    }
    
    public void esperarParaTerminar(){}


    public void logicaDivisiones(){}

    public static void main(String[] args){
    	int n = 8;
        int c = 8;
        int h = 12;
        int b = 5;
        int des = 0;

        if(args.length == 5){
            n = Integer.valueOf(args[0]);
            c = Integer.valueOf(args[1]);
            h = Integer.valueOf(args[2]);
            b = Integer.valueOf(args[3]);
            des = Integer.valueOf(args[4]);
        }
        ManagerAbs m = new ManagerSinDivision("DFSin",n,c,h,b,des);
        m.start();
    }
}
