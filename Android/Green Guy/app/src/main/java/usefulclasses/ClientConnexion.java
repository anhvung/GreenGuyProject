package usefulclasses;
//!  se charge des communication avec le serveur.
/*!
  A remplir
*/

import android.graphics.Bitmap;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
;

public class ClientConnexion {
    String response="-1";
    byte[] buffer = new byte[4096];
    private Socket connexion = null;
    private PrintWriter writer = null;
    BufferedOutputStream out=null;
    private BufferedInputStream reader = null;
    private String commande;
    //Notre liste de commandes. Le serveur nous répondra différemment selon la commande utilisée.
    int code=-1;
    private static int count = 0;
    private String name = "Client-";
    DataOutputStream bufOut;
    public static final String sep="!@@!!";
    String EOF="[eNdEnD]";
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


    public String[] magicSauce(){



        try {


            commande+=EOF;
            if(connexion!=null)
                reader = new BufferedInputStream(connexion.getInputStream());
            else
                return null;
            //On envoie la commande au serveur

            bufOut = new DataOutputStream(connexion.getOutputStream());

            InputStream in = new ByteArrayInputStream(commande.getBytes(StandardCharsets.UTF_8));
            int count;
            while ((count = in.read(buffer)) >   0)
            {
                bufOut.write(buffer, 0, count);
                bufOut.flush();
            }

            //TOUJOURS UTILISER flush() POUR ENVOYER RÉELLEMENT DES INFOS AU SERVEUR


            System.out.println("Commande " + commande + " envoyée au serveur");

            //On attend la réponse
            response = read();


        } catch (IOException e1) {
            e1.printStackTrace();
        }





        try {
            String close="0000"+EOF;
            InputStream in2 = new ByteArrayInputStream(close.getBytes(StandardCharsets.UTF_8));
            int count;
            while ((count = in2.read(buffer)) >   0)
            {
                bufOut.write(buffer, 0, count);
                bufOut.flush();
            }
            bufOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] ret=response.split(sep);
        return ret;
    }

    public String magicImage(Bitmap image){
        String imgString = Base64.encodeToString(getBytesFromBitmap(image),
                Base64.NO_WRAP);


        try {


            commande+=sep+imgString+EOF;
            reader = new BufferedInputStream(connexion.getInputStream());
            //On envoie la commande au serveur

            bufOut = new DataOutputStream(connexion.getOutputStream());

            InputStream in = new ByteArrayInputStream(commande.getBytes(StandardCharsets.UTF_8));
            int count;
            while ((count = in.read(buffer)) >   0)
            {
                bufOut.write(buffer, 0, count);
                bufOut.flush();
            }

            //TOUJOURS UTILISER flush() POUR ENVOYER RÉELLEMENT DES INFOS AU SERVEUR


            System.out.println("Commande " + commande + " envoyée au serveur");

            //On attend la réponse
            response = read();
            System.out.println("\t * " + name + " : Réponse reçue " + response);

        } catch (IOException e1) {
            e1.printStackTrace();
        }





        try {
            InputStream in = new ByteArrayInputStream(commande.getBytes(StandardCharsets.UTF_8));
            bufOut.write(in.read(buffer));
            bufOut.flush();
            bufOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return response;
    }


    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    //Méthode pour lire les réponses du serveur
    private String read() throws IOException{
        String re = "";
        int stream;
        byte[] b = new byte[4096];
        while ((stream = reader.read(b)) > 0) {
            re += new String(b, 0, stream);
            if (re.endsWith(EOF)) {
                break;
            }
        }
        return re.substring(0, re.length() - EOF.length());
    }

}