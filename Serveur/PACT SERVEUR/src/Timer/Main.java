package Timer;
public class Main {

   public static void main(String[] args) {
    
      String host = "10.50.2.94";
      int port = 2345;
      
      TimeServer ts = new TimeServer(host, port);
      ts.open();
      
      System.out.println("Serveur initialisé.");
      
      
   }
}