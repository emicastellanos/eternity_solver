package entidades;


public class Ficha {
    private int izq;
    private int arr;
    private int der;
    private int abj;
    private String id;

    public Ficha(int izq, int arr,int der, int abj,String id) {
        this.izq = izq;
        this.der = der;
        this.arr = arr;
        this.abj = abj;
        this.setId(String.format("%1$3s", id).replace(' ','0'));
    }

    public int getIzq() {
        return izq;
    }

    public void setIzq(int izq) {
        this.izq = izq;
    }

    public int getDer() {
        return der;
    }

    public void setDer(int der) {
        this.der = der;
    }

    public int getArr() {
        return arr;
    }

    public void setArr(int arr) {
        this.arr = arr;
    }

    public int getAbj() {
        return abj;
    }

    public void setAbj(int abj) {
        this.abj = abj;
    }

    public String imprimirse(){

        return izq+"-"+arr+"-"+der+"-"+abj;
    }
    
    public String imprimirTecho() {
    	StringBuffer result = new StringBuffer();
    	result.append("   ");
    	result.append(String.format("%1$3s", arr).replace(' ','0'));
    	result.append("   |");
    	
    	return result.toString();
    }
    
    public String imprimirMedio() {
    	StringBuffer result = new StringBuffer();
    	
    	result.append(String.format("%1$3s", izq).replace(' ','0'));
    	result.append("   ");
    	result.append(String.format("%1$3s", der).replace(' ','0') + "|");
    	
    	return result.toString();
    }
    
    public String imprimirAbajo() {
    	StringBuffer result = new StringBuffer();
    	result.append("   ");
    	result.append(String.format("%1$3s", abj).replace(' ','0'));
    	result.append("   |");
    	
    	return result.toString();
    	
    }
    
    public String imprimirseCuadrado(){
    	StringBuffer result = new StringBuffer();
    	result.append("---");
    	result.append(String.format("%1$3s", arr).replace(' ','0'));
    	result.append("---| \n");
    	
    	result.append(String.format("%1$3s", izq).replace(' ','0'));
    	result.append("---");
    	result.append(String.format("%1$3s", der).replace(' ','0') + " |");
    	
    	result.append("\n---");
    	result.append(String.format("%1$3s", abj).replace(' ','0'));
    	result.append("---|\n");
    	
        return result.toString();
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
