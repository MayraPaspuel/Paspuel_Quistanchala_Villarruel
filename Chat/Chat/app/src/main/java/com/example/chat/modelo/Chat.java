package com.example.chat.modelo;

public class Chat {

    private String emisor;
    private String receptor;
    private String mensaje;
    private String hora;

    public Chat() {
    }

    public Chat(String emisor, String receptor, String mensaje, String hora) {
        this.emisor = emisor;
        this.receptor = receptor;
        this.mensaje = mensaje;
        this.hora = hora;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getReceptor() {
        return receptor;
    }

    public void setReceptor(String receptor) {
        this.receptor = receptor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
