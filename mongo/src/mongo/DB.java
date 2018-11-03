package mongo;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DB {
	
	public static String key = "VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0";
	public static String db = "alerticdb";
	public static String collection = "denuncias";
	
	//ruta donde guardamos el fichero con los números denunciados:
	public static String localpath = "src/mongo/test.txt";
	
	
	//Método para guardar Denuncia en db (ej: mi_denuncia.Save();)
	public static void save(denuncia d) throws IOException {
	    
		final String POST_PARAMS = 
	    	"{\n" +
	    	"\"cc_denunciante\": \""+ d.cc_denunciante + "\",\r"+
	    	"\"num_denunciante\": \""+ d.num_denunciante + "\",\r"+
	    	"\"nombre_denunciante\": \""+ d.nombre_denunciante + "\",\r"+
	    	"\"fecha_denuncia\": \""+ d.fecha_denuncia + "\",\r"+
	    	"\"descripcion\": \""+ d.descripcion + "\",\r"+
	    	"\"num_denunciado\": \""+ d.num_denunciado + "\"\r"+
	        "\n}";
	    
	    //System.out.println(POST_PARAMS);
	    URL obj = new URL("https://api.mlab.com/api/1/databases/alerticdb/collections/denuncias?apiKey=VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0");
	    HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
	    postConnection.setRequestMethod("POST");
	    postConnection.setRequestProperty("userId", "a1bcdefgh");
	    postConnection.setRequestProperty("Content-Type", "application/json");
	    postConnection.setDoOutput(true);
	    
	    OutputStream os = postConnection.getOutputStream();
	    os.write(POST_PARAMS.getBytes());
	    os.flush();
	    os.close();
	    
	    int responseCode = postConnection.getResponseCode();
	    
	    //System.out.println("POST Response Code :  " + responseCode);
	    //System.out.println("POST Response Message : " + postConnection.getResponseMessage());
	    
	    if (responseCode == HttpURLConnection.HTTP_CREATED || responseCode == 200) { //success
	        BufferedReader in = new BufferedReader(new InputStreamReader(
	            postConnection.getInputStream()));
	        
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in .readLine()) != null) {
	            response.append(inputLine);
	        } in .close();
	        
	        localSync();
	    } else {
	        System.out.println("POST NOT WORKED: "+responseCode);
	    }
	}
	
	//Retorna String en formato JSON con todas las denuncias.
	public static String getDenuncias() throws IOException {
		
		URL urlForGetRequest = new URL("https://api.mlab.com/api/1/databases/"+db+"/collections/"+collection+"?apiKey="+key);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	   
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        
	        while ((readLine = in .readLine()) != null) {
	            response.append(readLine);
	        } in .close();
	        
	        return response.toString();

	    } else {
		    System.out.println("No se pudo realizar la consulta: " + responseCode);
			return null;
	    }
	}
	
	//Retorna String en formato JSON solo con los números denunciados.
	public static String getDenunciados() throws IOException {
		
		URL urlForGetRequest = new URL("https://api.mlab.com/api/1/databases/"+db+"/collections/"+collection+"?f={%22num_denunciado%22:%201}&apiKey="+key);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	   
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        
	        while ((readLine = in .readLine()) != null) {
	            response.append(readLine);
	        } in .close();
	        
	        return response.toString();

	    } else {
	        System.out.println("GET REQUEST FAILED");
	    }
	    System.out.println("No se pudo realizar la consulta: " + responseCode);
		return null;
		
	}
	
	//Verifica si *number* ha sido denunciado (dentro de un string *s* en formato Json)
	public static boolean verifyString(String s, String number) {
        if(s.contains("\"num_denunciado\" : "+"\""+number+"\"")) {
        	System.out.println("CUIDADO!! El número " + number + " es sospechoso");
        	return true;
        }else {
        	return false;
        }
	}
	
	//Consulta la base de datos si number ha sido denunciado.
	public static boolean verifyDB(String number) throws IOException { 
	    
		String querypath =
		"https://api.mlab.com/api/1/databases/"+db+"/collections/"+collection+"?q={\"num_denunciado\":%20" + "\""+number+"\"}&apiKey=VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0";
		//System.out.println(querypath);
		
		URL urlForGetRequest = new URL(querypath);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	    //conection.setRequestProperty("num_denunciado", "\""+number+"\"");
	   
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK || responseCode==200) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        
	        while ((readLine = in .readLine()) != null) {
	            response.append(readLine);
	        } in .close();
	        
	        //System.out.println(response.toString());
	        String temp = response.toString();
	        
	        return verifyString(temp,number);
	        
	    } else {
		    System.out.println("No se pudo realizar la consulta: " + responseCode);
			return false;
	    }

	}
	
	
	//Manejo de ficheros
	public static void localSync() throws IOException {
		String temp = getDenunciados();
		try (PrintStream out = new PrintStream(new FileOutputStream(localpath))) {
		    out.print(temp);
		}
	}

	public static String fileToString() throws IOException {
		
		byte[] encoded = Files.readAllBytes(Paths.get(localpath));
		return new String(encoded, StandardCharsets.UTF_8); 
	}
	
	//Consulta en el fichero si number ha sido denunciado.
	public static boolean verifyLocal(String number) throws IOException {
		return verifyString(fileToString(),number);
	}
}
