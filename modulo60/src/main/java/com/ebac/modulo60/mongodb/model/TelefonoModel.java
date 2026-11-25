package com.ebac.modulo60.mongodb.model;

import com.ebac.modulo60.mongodb.dto.Telefono;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;


public class TelefonoModel {

    private final MongoCollection<Document> collection;

    public TelefonoModel(MongoDatabase database){
        this.collection = database.getCollection("telefonos");
    }


    //CREATE

    public boolean guardar(Telefono telefono){
        try {

            Document documento = new Document()
                    .append("idUsuario", telefono.getIdUsuario())
                    .append("tipo", telefono.getTipo())
                    .append("numero", telefono.getNumero());

            collection.insertOne(documento);
            telefono.setId(documento.getObjectId("_id"));

            return true;

        } catch (Exception e) {
           System.err.println("Error al Guardar el Teléfono " + e.getMessage());
           return false;
        }
    }

    //READ ALL
    public List<Telefono> obtenerTelefonos(){
        List<Telefono> telefonos = new ArrayList<>();

        for(Document documento : collection.find()){
            telefonos.add(convertirTelefono(documento));
        }

        return telefonos;
    }

    public Telefono obtenerPorId(String id){
        try {
            Document documento = collection.find(
                    Filters.eq("_id", new ObjectId(id))
            ).first();

            return documento != null ? convertirTelefono(documento) : null;
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public boolean actualizar(Telefono telefono, List<String> campos){
        if(telefono.getId() == null || campos.isEmpty()){
            return false;
        }

        List<Bson> actualizaciones = new ArrayList<>();

        for(String campo : campos){
            switch (campo){

                case "1":
                    actualizaciones.add(
                            Updates.set("idUsuario", telefono.getIdUsuario())
                    );
                    break;

                case "2":
                    actualizaciones.add(
                            Updates.set("tipo", telefono.getTipo())
                    );
                    break;

                case "3":
                    actualizaciones.add(
                            Updates.set("numero", telefono.getNumero())
                    );
                    break;
            }
        }

        if(actualizaciones.isEmpty()){
            return false;
        }

        var resultado = collection.updateOne(
                Filters.eq("_id",telefono.getId()),
                Updates.combine(actualizaciones)
        );

        return resultado.getMatchedCount() > 0;
    }

    //BORRAR REGISTRO
    public boolean eliminar(String id){
        try{
            var resultado = collection.deleteOne(
                    Filters.eq("_id", new ObjectId(id))
            );

            return resultado.getDeletedCount() > 0;

        }catch (IllegalArgumentException e){
            return false;
        }
    }


    // CONVERT DOCUMENT TO DTO
    private Telefono convertirTelefono(Document document){
        Telefono telefono = new Telefono();

        telefono.setId(document.getObjectId("_id"));
        telefono.setIdUsuario(document.getInteger("idUsuario"));
        telefono.setTipo(document.getString("tipo"));
        telefono.setNumero(document.getString("numero"));

        return telefono;

    }
}
