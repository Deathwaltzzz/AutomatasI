package U3;

public class Componente {
	protected String componente;
	protected String clasificacion;
	
	public Componente(String componente, String clasificacion) {
		this.componente = componente;
		this.clasificacion = clasificacion;
	}

	public String getComponente() {
		return componente;
	}

	public String getClasificacion() {
		return clasificacion;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}
	
	public void setClasificacion(String clasificacion) {
		this.clasificacion = clasificacion;
	}

	@Override
	public String toString() {
		return "componente: " + componente + " | clasificacion: " + clasificacion + "\n";
	}
	
	
}
