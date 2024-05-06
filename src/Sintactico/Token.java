package Sintactico;

public class Token {
    protected  String lexema;
    protected int token;
    protected int pos_Tabla;
    protected int no_linea;

    public Token(String lexema, int token, int pos_Tabla, int no_linea) {
        super();
        this.lexema = lexema;
        this.token = token;
        this.pos_Tabla = pos_Tabla;
        this.no_linea = no_linea;
    }
    public String getLexema() {
        return lexema;
    }

    public int getToken() {
        return token;
    }

    public int getPos_Tabla() {
        return pos_Tabla;
    }

    public int getNo_linea() {
        return no_linea;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public void setPos_Tabla(int pos_Tabla) {
        this.pos_Tabla = pos_Tabla;
    }

    public void setNo_linea(int no_linea) {
        this.no_linea = no_linea;
    }
    @Override
    public String toString() {
        return "Token [lexema=" + lexema + ", token=" + token + ", pos_Tabla=" + pos_Tabla + ", no_linea=" + no_linea
                + "]";
    }


}
