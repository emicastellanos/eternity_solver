package entidades;


public class Ficha {
    private int izq;
    private int arr;
    private int der;
    private int abj;
    private String id;

    public Ficha (String id){
        this.setId(String.format("%1$3s", id).replace(' ','0'));
    }

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    @Override
    public boolean equals(Object obj) {
        if(((Ficha) obj).getIzq()==this.getIzq() && ((Ficha) obj).getArr() ==this.getArr() && ((Ficha) obj).getDer() ==this.getDer() && ((Ficha) obj).getAbj()==this.getAbj()){
            return true;
        }else if(((Ficha) obj).getIzq()==this.getArr() && ((Ficha) obj).getArr() ==this.getDer() && ((Ficha) obj).getDer() ==this.getAbj() && ((Ficha) obj).getAbj()==this.getIzq()){
            return true;
        }else if(((Ficha) obj).getIzq()==this.getDer() && ((Ficha) obj).getArr() ==this.getAbj() && ((Ficha) obj).getDer() ==this.getIzq() && ((Ficha) obj).getAbj()==this.getArr()){
            return true;
        }else if(((Ficha) obj).getIzq()==this.getAbj() && ((Ficha) obj).getArr() ==this.getIzq() && ((Ficha) obj).getDer() ==this.getArr() && ((Ficha) obj).getAbj()==this.getDer()){
            return true;
        }else return false;
    }
}
