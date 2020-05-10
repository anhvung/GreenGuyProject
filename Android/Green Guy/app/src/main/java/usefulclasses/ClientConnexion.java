package usefulclasses;
//!  se charge des communication avec le serveur.
/*!
  A remplir
*/

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
;

public class ClientConnexion {
    String response="-1";
    private Socket connexion = null;
    private PrintWriter writer = null;
    private BufferedInputStream reader = null;
    private String commande;
    //Notre liste de commandes. Le serveur nous répondra différemment selon la commande utilisée.
    int code=-1;
    private static int count = 0;
    private String name = "Client-";
    public static final String sep="!@@!!";
    public ClientConnexion(String host, int port,String codee,String msg){
        name += ++count;
        code=Integer.parseInt(codee);
        commande = codee+sep+msg;
        try {
            connexion = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String magicSauce(){



        try {


            writer = new PrintWriter(connexion.getOutputStream(), true);
            reader = new BufferedInputStream(connexion.getInputStream());
            //On envoie la commande au serveur
            BufferedWriter bufOut = new BufferedWriter( new OutputStreamWriter( connexion.getOutputStream() ) );

            writer.write(commande);;
            //TOUJOURS UTILISER flush() POUR ENVOYER RÉELLEMENT DES INFOS AU SERVEUR
            writer.flush();

            System.out.println("Commande " + commande + " envoyée au serveur");

            //On attend la réponse
            response = read();
            System.out.println("\t * " + name + " : Réponse reçue " + response);

        } catch (IOException e1) {
            e1.printStackTrace();
        }




        writer.write("0000");
        writer.flush();
        writer.close();

        return response;
    }




    //Méthode pour lire les réponses du serveur
    private String read() throws IOException{
        String response = "";
        int stream;
        byte[] b = new byte[4096];
        stream = reader.read(b);
        response = new String(b, 0, stream);
        return response;
    }
}