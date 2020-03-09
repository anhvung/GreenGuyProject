import java.net.*;
import java.io.*;
import java.sql.*;
import java.util.*;

public class Echoserver {
	public static void main(String[] args) throws IOException {
        



        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }
        String url = "jdbc:mysql://localhost:3306/greenGuyDataBase";
	String utilisateur = "mysql";
	String motDePasse = "Password1";
	Connection connexion = null;
	try{
	     connexion = DriverManager.getConnection( url, utilisateur, motDePasse );
	}catch( Exception e) {} 





        
        int portNumber = Integer.parseInt(args[0]);
        
        try (
            ServerSocket serverSocket =
                new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket = serverSocket.accept();     
            PrintWriter out =
                new PrintWriter(clientSocket.getOutputStream(), true);                   
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        ) {
        	
            String inputLine;
            /*Formatage : 
            1ere ligne : identifiant
            2e ligne : type de requete
            3e ligne : Contenu 1 ligne par info
            */
            String line ;
            int id ;
            int i = 0 ;
            String requete ="default";
            while ((line = in.readLine()) != null ) {
            	out.println(i);
            	switch (i) {
	            case 0 :
	            	id = Integer.parseInt(line);
	            	out.print(id);
	            	break;
	            case 1 :
	            	requete = line;
	            	out.println(requete);
	            	break;
	            default :
	            	switch (requete) {
	            	case "Register" : 
	            		break;
	            	case "Login" :
	            		String mail = line;
	            		out.print(mail);
	            		
	            		String pswd = in.readLine();
	            		out.print(pswd);
	            	
	            		if (mail.equals("test") && pswd.equals( "test")) {
	            			out.println("Connexion réussie");
	            		}else {
	            			out.println("erreur");
	            		}
	            		clientSocket.close();
	            		serverSocket.close();
	            		
	            		break;
	            	}	
	           }

	            i++;
        }
        }catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
