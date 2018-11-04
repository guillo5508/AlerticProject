package com.example.alejandro.pruebamlab;


import java.io.IOException;

public class denuncia {
    String cc_denunciante;
    String num_denunciante;
    String nombre_denunciante;
    String fecha_denuncia;
    String fecha_incidente;
    String descripcion;
    String num_denunciado;

    //Constructor de toda la vida
    //Parámetros: cedula, numD, nombreD, fecha, descripcion, numSospechoso
    public denuncia(String cc_denunciante, String num_denunciante, String nombre_denunciante, String fecha_denuncia, String fecha_incidente,
                    String descripcion, String num_denunciado) {

        super();
        this.cc_denunciante = cc_denunciante;
        this.num_denunciante = num_denunciante;
        this.nombre_denunciante = nombre_denunciante;
        this.fecha_denuncia = fecha_denuncia;
        this.fecha_incidente = fecha_incidente;
        this.descripcion = descripcion;
        this.num_denunciado = num_denunciado;
    }

    //Un Main común y corriente para probar
   /* public static void main(String[] args) throws IOException {

        //cedula, numD, nombreD, fecha, descripcion, numSospechoso

        denuncia test = new denuncia("cc12344", "300123456", "Denunciante Nombre", "31,enero,2019", "desc", "44");
        System.out.println(GetDenunciados());

    }*/
}
