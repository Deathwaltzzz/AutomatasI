public class Persona {
    private String nombre;
    private String apPaterno;
    private String apMaterno;
    private int edad;

    public Persona(){
    }

    public Persona(String nombre, String apPaterno, String apMaterno, int edad){
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.nombre = nombre;
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String[] returnArray(){
        return new String[]{this.nombre,this.apPaterno,this.apMaterno,String.valueOf(this.edad)};
    }

}
