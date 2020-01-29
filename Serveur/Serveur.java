import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/*
 * www.codeurjava.com
 */
public class Serveur {
 
   public static void main(String[] test) {
  
     final ServerSocket serveurSocket  ;
     final Socket clientSocket ;
     final BufferedReader in;
     final PrintWriter out;
     final Scanner sc=new Scanner(System.in); //pour lire à partir du clavier
  
     try {
    	//créer un socket serveur avec un numéro de port 5000
       serveurSocket = new ServerSocket(5000);
     //écoute pour une connexion à apporter avec ce socket et l'accepte
       clientSocket = serveurSocket.accept();
     //flux pour envoyer
       out = new PrintWriter(clientSocket.getOutputStream());
     //flux pour recevoir
       in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
       /* il faut créer 2 processus (threads parce que l'envoi et la réception se font simulatnément
    		 cela nous permet d'envoyer et de recevoir des messages en même temps */  
       Thread envoi= new Thread(new Runnable() {
          String msg;
          @Override
          public void run() {
             while(true){
                msg = sc.nextLine();
                out.println(msg);
                out.flush();
             }
          }
       });
       envoi.start();
   
       Thread recevoir= new Thread(new Runnable() {
          String msg ;
          @Override
          public void run() {
             try {
                msg = in.readLine();
                //tant que le client est connecté
                while(msg!=null){
                   System.out.println("Client : "+msg);
                   msg = in.readLine();
                }
                //sortir de la boucle si le client a déconecté
                System.out.println("Client déconecté");
                //fermer le flux et la session socket
                out.close();
                clientSocket.close();
                serveurSocket.close();
             } catch (IOException e) {
                  e.printStackTrace();
             }
         }
      });
      recevoir.start();
      }catch (IOException e) {
         e.printStackTrace();
      }
   }
}