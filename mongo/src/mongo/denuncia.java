package mongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class denuncia {
	String cc_denunciante;
	String num_denunciante;
	String nombre_denunciante;
	String fecha_denuncia;
	String descripcion;
	String num_denunciado;
	
	//Parámetros: cedula, numD, nombreD, fecha, descripcion, numSospechoso
	public denuncia(String cc_denunciante, String num_denunciante, String nombre_denunciante, String fecha_denuncia,
			String descripcion, String num_denunciado) {
		
		super();
		this.cc_denunciante = cc_denunciante;
		this.num_denunciante = num_denunciante;
		this.nombre_denunciante = nombre_denunciante;
		this.fecha_denuncia = fecha_denuncia;
		this.descripcion = descripcion;
		this.num_denunciado = num_denunciado;
	}

	public void Save() throws IOException {
	    
		final String POST_PARAMS = 
	    	"{\n" +
	    	"\"cc_denunciante\": \""+ this.cc_denunciante + "\",\r"+
	    	"\"num_denunciante\": \""+ this.num_denunciante + "\",\r"+
	    	"\"nombre_denunciante\": \""+ this.nombre_denunciante + "\",\r"+
	    	"\"fecha_denuncia\": \""+ this.fecha_denuncia + "\",\r"+
	    	"\"descripcion\": \""+ this.descripcion + "\",\r"+
	    	"\"num_denunciado\": \""+ this.num_denunciado + "\"\r"+
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
	        // print result
	        //System.out.println(response.toString());
	        //System.out.println("SUCCESS");
	    } else {
	        System.out.println("POST NOT WORKED");
	    }
	}
	
	
	public static String GetDenuncias() throws IOException {
		
		URL urlForGetRequest = new URL("https://api.mlab.com/api/1/databases/alerticdb/collections/denuncias?apiKey=VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0");
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
	

	public static String GetDenunciados() throws IOException {
		
		URL urlForGetRequest = new URL("https://api.mlab.com/api/1/databases/alerticdb/collections/denuncias?f={%22num_denunciado%22:%201}&apiKey=VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0");
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
	
	public static boolean Verify(String number) throws IOException { 
	    
		String querypath =
		"https://api.mlab.com/api/1/databases/alerticdb/collections/denuncias?q={\"num_denunciado\":%20" + "\""+number+"\"}&apiKey=VOTHuAwVOkg3D6nVW3SLuGAIMC-dzUd0";
		System.out.println(querypath);
		
		URL urlForGetRequest = new URL(querypath);
	    String readLine = null;
	    HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
	    conection.setRequestMethod("GET");
	    //conection.setRequestProperty("num_denunciado", "\""+number+"\"");
	   
	    int responseCode = conection.getResponseCode();
	    
	    if (responseCode == HttpURLConnection.HTTP_OK) {
	        BufferedReader in = new BufferedReader(
	            new InputStreamReader(conection.getInputStream()));
	        StringBuffer response = new StringBuffer();
	        
	        while ((readLine = in .readLine()) != null) {
	            response.append(readLine);
	        } in .close();
	        
	        System.out.println(response.toString());
	        //GetAndPost.POSTRequest(response.toString());
	        //Ver si response contiene el número a verificar
	        String temp = response.toString();
	        if(temp.contains("\"num_denunciado\" : "+"\""+number+"\"")) {
	        	System.out.println("CUIDADO!! El número " + number + " es sospechoso");
	        	return true;
	        }else {
	        	System.out.println("El número " + number + " NO es sospechoso");
	        	return false;
	        }
	        
	    } else {
	        System.out.println("GET REQUEST FAILED");
	    }
	    System.out.println("No se pudo realizar la consulta: " + responseCode);
		return false;
		 	
	}
	
	//Un Main común y corriente para probar
	public static void main(String[] args) throws IOException {
		
		//cedula, numD, nombreD, fecha, descripcion, numSospechoso
		
		denuncia test = new denuncia("cc12344", "300123456", "Denunciante Nombre", "31,enero,2019", "desc", "12345");
		//Guardar denuncia en DB
			//test.Save();
		//Verificar número
			//System.out.println(Verify("666"));
		//Traer todas las denuncias de la DB a un String
			//System.out.println(GetDenuncias());
		//Traer Solo los números denunciados a un String:
			System.out.println(GetDenunciados());
	}
}
