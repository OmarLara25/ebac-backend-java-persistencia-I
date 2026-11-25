package com.ebac.modulo60.mongodb.dto;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Telefono {

    @BsonId
    private ObjectId id;

    private int idUsuario;
    private String tipo;
    private String numero;

    public Telefono() {
    }

    public Telefono(int idUsuario, String tipo, String numero) {
        this.idUsuario = idUsuario;
        this.tipo = tipo;
        this.numero = numero;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "Telefono{" +
                "id=" + id +
                ", idUsuario=" + idUsuario +
                ", tipo='" + tipo + '\'' +
                ", numero='" + numero + '\'' +
                '}';
    }
}