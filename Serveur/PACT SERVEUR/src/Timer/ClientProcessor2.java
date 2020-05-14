package Timer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Date;

public class ClientProcessor2 implements Runnable{

   private Socket sock;
   private PrintWriter writer = null;
   private BufferedReader reader = null;
   
   public ClientProcessor2(Socket pSock){
      sock = pSock;
   }
   
   //Le traitement lancé dans un thread séparé
   public void run(){
      System.err.println("Lancement du traitement de la connexion cliente");

      boolean closeConnexion = false;
      //tant que la connexion est active, on traite les demandes
      while(!sock.isClosed()){
         
         try {
            
            //Ici, nous n'utilisons pas les mêmes objets que précédemment
            //Je vous expliquerai pourquoi ensuite
            writer = new PrintWriter(sock.getOutputStream());
            
            reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            
            //On attend la demande du client   
            System.out.println("1 deg");
            String response = read();
            System.out.println("2 deg");
            InetSocketAddress remote = (InetSocketAddress)sock.getRemoteSocketAddress();
            System.out.println("3 deg");
            //On affiche quelques infos, pour le débuggage
            String debug = "";
            debug = "Thread : " + Thread.currentThread().getName() + ". ";
            debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() +".";
            debug += " Sur le port : " + remote.getPort() + ".\n";
            debug += "\t -> Commande reçue : " + response + "\n";
            System.err.println("\n" + debug);
            /////////////////////////////////////////////////////////////////////////////////////////////////////
            String[] responseList = response.split("\\r?\\n");;
            System.out.println(responseList[0]+responseList[1]);
            String code=responseList[0];
            System.out.println("4 deg");
            //On traite la demande du client en fonction de la commande envoyée
            String toSend = "";
            
            switch(code){
               case "0001":
                  toSend = nouveauProfile();
                  break;
              
               default : 
                  toSend = "Commande inconnu !";                     
                  break;
            }
            
            //On envoie la réponse au client
            writer.write(toSend);
            //Il FAUT IMPERATIVEMENT UTILISER flush()
            //Sinon les données ne seront pas transmises au client
            //et il attendra indéfiniment
            writer.flush();
            
            if(closeConnexion){
               System.err.println("COMMANDE CLOSE DETECTEE ! ");
               writer = null;
               reader = null;
               sock.close();
               break;
            }
         }catch(SocketException e){
            System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
            break;
         } catch (IOException e) {
            e.printStackTrace();
         }         
      }
   }
   
   private String nouveauProfile() {
	   
	return "true";
}

//La méthode que nous utilisons pour lire les réponses
   private String read() throws IOException{    
	   StringBuilder responseString = new StringBuilder();
	   String str;
       while ((str = reader.readLine()) != null) {
    	   responseString.append(str);
    	   System.out.println("degadfuf");
       }
       System.out.println("sortie");
       return  responseString.toString();
   }
   
}