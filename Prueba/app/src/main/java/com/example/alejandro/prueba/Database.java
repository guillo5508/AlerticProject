package com.example.alejandro.prueba;

public class Database {
    public String getDatabaseName() {
        return "alerticdb";
    }
    public String getApiKey() {
        return "VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0";
    }
    public String getBaseUrl()
    {
        return "https://api.mlab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }
    public String apiKeyUrl()
    {
        return "?apiKey="+getApiKey();
    }
    public String collectionName()
    {
        return "denuncias";
    }
    public String buildDenunciasSaveURL()
    {
        return getBaseUrl()+collectionName()+apiKeyUrl();
    }
    public String buildDenunciasFetchURL()
    {
        return getBaseUrl()+collectionName()+apiKeyUrl();
    }
    public String createContact(Login contact) {
        return String.format("{\"first_name\": \"%s\", "+ "\"last_name\": \"%s\", " + "\"phone\": \"%s\"}", contact.getnombre(), contact.getApellido(), contact.getNumeroTelefono());
    }
}
