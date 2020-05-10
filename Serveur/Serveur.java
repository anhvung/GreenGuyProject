import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
/*
 *\brief Classe Serveur, il s'agit pour le moment d'un code servant de point de départ
 */
public class Serveur {
 
   public static void main(String[] test) {
  
     final ServerSocket serveurSocket  ;
     final Socket clientSocket ;
     final BufferedReader in;
     final PrintWriter out;
     final Scanner sc=new Scanner(System.in); //pour lire  partir du clavier
  
     try {
    	//crer un socket serveur avec un numro de port 5000
       serveurSocket = new ServerSocket(5000);
     //coute pour une connexion  apporter avec ce socket et l'accepte
       clientSocket = serveurSocket.accept();
     //flux pour envoyer
       out = new PrintWriter(clientSocket.getOutputStream());
     //flux pour recevoir
       in = new BufferedReader (new InputStreamReader (clientSocket.getInputStream()));
       /* il faut crer 2 processus (threads parce que l'envoi et la rception se font simulatnment
    		 cela nous permet d'envoyer et de recevoir des messages en mme temps */  
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
                //tant que le client est connect
                while(msg!=null){
                   System.out.println("Client : "+msg);
                   msg = in.readLine();
                }
                //sortir de la boucle si le client a dconect
                System.out.println("Client dconect");
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