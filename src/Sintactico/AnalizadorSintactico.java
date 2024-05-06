package Sintactico;

import java.util.List;

public class AnalizadorSintactico {
    private List<Token> tokens;
    private int indice;
    private Token proximoToken;

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
        this.indice = 0;
        this.proximoToken = null;
        analizar();
    }

    public void analizar() {
        try {
            while (hayTokensRestantes()) {
                encabezado();
                declaraciones();
                estructuraPrograma();
            }
            aceptar(); // Si el análisis se completó sin errores, se acepta el programa
        } catch (RuntimeException e) {
            error(e.getMessage());
        }
    }

    private boolean hayTokensRestantes() {
        return indice < tokens.size();
    }

    private void avanza() {
        if (indice < tokens.size()) {
            proximoToken = tokens.get(indice);
            indice++;
        } else {
            proximoToken = null; // Ya no hay más tokens para analizar
        }
    }

    private void error(String mensaje) {
        throw new RuntimeException( mensaje);
    }

    private void aceptar() {
        System.out.println("El análisis sintáctico ha finalizado sin errores.");
    }

    private void encabezado() {
        // Verificamos si el próximo token es la palabra clave 'program'
        Token tokenActual = tokens.get(indice);
        if (tokenActual.getToken() != -1) {
            error("Se esperaba la palabra clave 'program'");
        }
        avanza(); // Avanzamos al próximo token

        // Verificamos si hay un identificador después de la palabra clave 'program'
        if (!hayTokensRestantes()) {
            error("Se esperaba un identificador después de la palabra clave 'program'");
        }
        tokenActual = tokens.get(indice);
        if (tokenActual.getToken() != -55) {
            error("Se esperaba un identificador de tipo general (?)");
        }
        avanza(); // Avanzamos al próximo token

        // Verificamos si hay un punto y coma después del identificador
        if (!hayTokensRestantes()) {
            error("Se esperaba un punto y coma ';' después del identificador");
        }
        tokenActual = tokens.get(indice);
        if (tokenActual.getToken() != -75) {
            error("Se esperaba un punto y coma ';' después del identificador ");
        }
        avanza(); // Avanzamos al próximo token
    }

    private void declaraciones() {
        boolean varEncontrada = false;
        while (hayTokensRestantes() && tokens.get(indice).getToken() != -2) {
            Token tokenActual = tokens.get(indice);

            if (tokenActual.getToken() == -15) {
                if (!varEncontrada) {
                    avanza();
                    varEncontrada = true;
                } else {
                    error("La palabra clave 'var' ya fue declarada anteriormente en la línea " + tokenActual.getNo_linea());
                }
            } else {
                if (!varEncontrada) {
                    error("Se esperaba la palabra clave 'var' en la línea " + tokenActual.getNo_linea());
                } else {
                    declaracionVariable();
                }
            }
        }
    }

    private void declaracionVariable() {
        // Manejo de las declaraciones de variables
        Token tokenActual = tokens.get(indice);
        if (tipoDato(tokenActual.getToken())) {
            avanza();

            tokenActual = tokens.get(indice);
            if (tokenActual.getToken() != -77) {
                error("Se esperaba ':' después del tipo de dato en la línea " + tokenActual.getNo_linea());
            }
            avanza();

            // Verificar y consumir identificadores
            while (hayTokensRestantes() &&  identificador(tokens.get(indice).getToken())) {
                avanza();
                tokenActual = tokens.get(indice);
                if (tokenActual.getToken() == -76) {
                    avanza();
                } else if (tokenActual.getToken() == -75) {
                    avanza();
                    break;
                } else {
                    error("Se esperaba ',' o ';' después del identificador en la línea " + tokenActual.getNo_linea());
                }
            }
        } else {
            error("Se esperaba un tipo de dato válido en la línea " + tokenActual.getNo_linea());
        }
    }

    private void estructuraPrograma(){
        // Se verifica que comienze con un begin
        Token tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -2)
            error("Se esperaba la palabra clave 'begin' en la línea " + tokenActual.getNo_linea());
        avanza();
        // Aqui tiene que empezar la verificacion de las sentencias del programa
        while(hayTokensRestantes()){
            tokenActual = tokens.get(indice);
            // Se verifica que termine con un end
            if(indice == tokens.size() -1 && tokenActual.getToken() != -3)
                error("Se esperaba la palabra clave 'end' en la línea " + tokenActual.getNo_linea());
            // Si termina con un end se termina el programa sin errores
            if(tokenActual.getToken() == -3 && indice == tokens.size() -1){
                aceptar();
                System.exit(0);
            }
            // Aqui se tiene que verificar el tipo de sentencia dentro del programa
            // Ya sea por ejemplo un if, una operacion, un write, etc...
            comprobarSentencias(tokenActual);
        }
    }

    private void ifEstructura(){
        avanza();
        Token tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -73)
            error("Se esperaba '(' en la línea " + tokenActual.getNo_linea());
        avanza();
        condicion();
        tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -74)
            error("Se esperaba ')' en la línea " + tokenActual.getNo_linea());
        avanza();
        tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -16)
            error("Se esperaba la palabra clave 'then' en la línea " + tokenActual.getNo_linea());
        avanza();
        tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -2)
            error("Se esperaba la palabra clave 'begin' en la línea " + tokenActual.getNo_linea());
        avanza();
        while(hayTokensRestantes()) {
            tokenActual = tokens.get(indice);
            if( tokenActual.getToken() == -3) {
                avanza();
                return;
            }
            if(tokenActual.getToken() == -6) ifEstructura();
            else comprobarSentencias(tokenActual);
        }

    }

    private void comprobarSentencias(Token tokenActual) {
        switch (tokenActual.getToken()) {
            case -4 -> { // Esto verifica la estructura de un read
            }
            case -5 -> writeEstructura(); // Esto verifica la estructura de un write
            case -6 -> ifEstructura();
            case -7 -> { // Esto verifica la estructura de un else
            }
            case -8 -> { // Esto verifica la estructura de un while
            }
            case -9 -> { // Esto verifica la estructura de un repeat
            }
            case -11, -12, -13, -14, -15 -> { // Esto verifica la estructura de una asignacion
            }
        }
    }

    private void condicion(){
        Token tokenActual = tokens.get(indice);
        if(tokenActual.getToken() == -43){
            avanza();
            tokenActual = tokens.get(indice);
            if(!identificador(tokenActual.getToken()))
                error("Se esperaba un identificador en la línea " + tokenActual.getNo_linea());
        }
        if(!identificador(tokenActual.getToken()) && !isConstante(tokenActual.getToken()))
            error("Se esperaba un identificador en la línea " + tokenActual.getNo_linea() + "se encontro " + tokenActual.getToken());
        avanza();
        tokenActual = tokens.get(indice);
        if(isOperando(tokenActual.getToken())) {
            avanza();
            tokenActual = tokens.get(indice);
            if(!identificador(tokenActual.getToken()) && !isConstante(tokenActual.getToken()))
                error("Se esperaba un identificador o una constante en la línea " + tokenActual.getNo_linea());
            avanza();
            tokenActual = tokens.get(indice);
            if(tokenActual.getToken() != -35)
                error("Se esperaba un '==' en la línea " + tokenActual.getNo_linea());
            avanza();
            tokenActual = tokens.get(indice);
            if(!identificador(tokenActual.getToken()) && !isConstante(tokenActual.getToken()))
                error("Se esperaba un identificador o una constante en la línea " + tokenActual.getNo_linea());
            avanza();
            tokenActual = tokens.get(indice);
        }
        if(tokenActual.getToken() == -74)
            return;
        if(!isLogical(tokenActual.getToken()))
            error("Se esperaba un operador lógico en la línea " + tokenActual.getNo_linea());
        avanza();
        condicion();
    }

    private boolean isOperando(int token){
        return token <= -21 && token >= -25;
    }

    private boolean isLogical(int token){
        return token<=-31 && token >= -42;
    }

    private void writeEstructura(){
        avanza();
        Token tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -73)
            error("Se esperaba '(' en la línea " + tokenActual.getNo_linea());
        avanza();
        tokenActual = tokens.get(indice);
        if(!isPrintable(tokenActual.getToken()))
            error("Se esperaba un identificador o un literal en la línea " + tokenActual.getNo_linea());
        avanza();
        tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -74)
            error("Se esperaba ')' en la línea " + tokenActual.getNo_linea());
        avanza();
        tokenActual = tokens.get(indice);
        if(tokenActual.getToken() != -75)
            error("Se esperaba ';' en la línea " + tokenActual.getNo_linea());
        avanza();
    }

    private boolean isPrintable(int token){
        return identificador(token) || (token >= -65 && token <= -61) || isConstante(token);
    }

    private boolean isConstante(int token){
        return token >= -65 && token <= -61;
    }

    private boolean tipoDato(int token) {
        if (token == -11 || token == -12 || token == -13 || token == -14) {
            return true;
        }
        return false;
    }

    private boolean identificador(int token) {
        if (token == -51 || token == -52 || token == -53 || token == -54 || token == -55) {
            return true;
        } else {
            return false;
        }
    }
}