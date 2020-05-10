package Timer;

import java.sql.*;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;

import java.util.ArrayList;

public class ClientProcessor implements Runnable {
	public static final String sep = "!@@!!";
	private Socket sock;
	private PrintWriter writer = null;
	private BufferedInputStream reader = null;
	private int code = 0000;
	final String server = "localhost:3306/";
	final String db_name = "green_guy";
	final String userName = "root";
	final String password = "";
	Statement stmt = null;
	Statement statement = null;
	ArrayList<String> itemList = null;
	Connection myConn;
	ResultSet rs;
	public ClientProcessor(Socket pSock) {
		sock = pSock;
	}
	public void run() {
		System.err.println("Lancement du traitement de la connexion cliente");
		boolean closeConnexion = false;
		while (!sock.isClosed()) {
			try {
				writer = new PrintWriter(sock.getOutputStream());
				reader = new BufferedInputStream(sock.getInputStream());
				String response = read();
				InetSocketAddress remote = (InetSocketAddress) sock.getRemoteSocketAddress();
				String debug = "";
				debug = "Thread : " + Thread.currentThread().getName() + ". ";
				debug += "Demande de l'adresse : " + remote.getAddress().getHostAddress() + ".";
				debug += " Sur le port : " + remote.getPort() + ".\n";
				debug += "\t -> Commande reçue : " + response + "\n";
				System.err.println("\n" + debug);
				String toSend = "";
				String[] responseList = response.split(sep, -1);
				System.out.println("number of intem: " + responseList.length);
				for (String str : responseList) {
					System.out.println(str);
				}
				if (responseList.length != 0) {
					code = Integer.parseInt(responseList[0]);
				}
				itemList =new ArrayList<String>(Arrays.asList(Arrays.copyOfRange(responseList, 1, responseList.length)));
						;
				switch (code) {
				case 0001:
					toSend = nouveauProfile();
					break;
				case 0002:
					toSend = login();
					break;
				case 0003:
					toSend= addFriend();
				case 0000:
					closeConnexion = true;
					break;
				default:
					System.out.println("Commande inconnu !");
					closeConnexion = true;
					break;
				}
				writer.write(toSend);
				writer.flush();

				if (closeConnexion) {
					System.err.println("FINI !");
					writer = null;
					reader = null;
					sock.close();
					break;
				}

			} catch (SocketException e) {
				System.err.println("LA CONNEXION A ETE INTERROMPUE ! ");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}


	private String addFriend() {
		try {
			System.out.println("SELECT * FROM user_"+itemList.get(0)+" WHERE friend_id = "+itemList.get(1));
			ask("SELECT * FROM user_"+itemList.get(0)+" WHERE friend_id = "+itemList.get(1)); // check if mail used
			
			if (rs.next()) {
				System.out.println("already friend");
				closeSQL();
				return "already friend";
			}
			else {
				add("user_"+itemList.get(0),new ArrayList<String>(itemList.subList(1,2)));
				add("user_"+itemList.get(1),new ArrayList<String>(itemList.subList(0,1)));
				return "true";
			}
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String login() {
		try {
			ask("SELECT * FROM users WHERE mail = "+itemList.get(0));// take all account info
			if (!rs.next()) {
				return "err";
			}
			else {
				if(itemList.get(1).equals(rs.getString("pwd"))){
					String id_str=String.valueOf(rs.getInt("id"));
					System.out.print("Login id : "+id_str);
					return id_str+sep+rs.getString("name");
				}
				else {
					System.out.print("entered: "+itemList.get(1)+" pws: "+"'"+rs.getString("pwd")+"'");
					return "false";
				}
				
			}
		} catch (ClassNotFoundException e) {
			return "err";
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return "err";
	}

	private String nouveauProfile() {
		try {
			ask("SELECT * FROM users WHERE mail = "+itemList.get(1)); // check if mail used
			if (rs.next()) {
				System.out.println("mail already used");
				closeSQL();
				return "mail already used";
			}
			else {
				ask("SELECT MAX(id) FROM users");
				rs.next();
				int id=rs.getInt(1)+1;					//get new id and add user
				itemList.add(0, String.valueOf(id));
				if (add("users",itemList)) {
					closeSQL();
					String query ="CREATE TABLE `"+"user_"+String.valueOf(id)+"` (`friend_id` int(11) NOT NULL) ";
					createTable(query);
					return "true";
				}
				else {
					closeSQL();
					return "champs invalides";
				}
				
				
		  
			}

			

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "true";
	}
	private void closeSQL() {
		if (stmt != null) {
			try {
				stmt.close();
				if (rs != null) {
					rs.close();

				}
				if (myConn != null) {
					myConn.close();

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
	// "SELECT * FROM table_test" updates rs how gets results
	private void createTable(String request) throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);
			try {
				stmt = myConn.createStatement();
				stmt.executeUpdate(request);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void ask(String request) throws ClassNotFoundException {

		try {

			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);

			try {
				stmt = myConn.createStatement();
				rs = stmt.executeQuery(request);

			} catch (SQLException e) {
				e.printStackTrace();
			} finally {

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

//[int,int,'str','str']
	private boolean add(String table,ArrayList<String> request) throws ClassNotFoundException {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			myConn = DriverManager.getConnection("jdbc:mysql://" + server + db_name, userName, password);
			statement = myConn.createStatement();

			String send = "";
			int len = request.size();
			for (int i = 0; i < len - 1; i++) {
				send += request.get(i) + ",";
			}
			send += request.get(len-1);
			System.out.println("INSERT INTO "+table+ " VALUES (" + send + ")");
			statement.executeUpdate("INSERT INTO "+table+ " VALUES (" + send + ")");
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("SQL error CANNOT ADD !");
			return false;

		}

	}

	private String read() throws IOException {
		String response = "";
		int stream;
		byte[] b = new byte[4096];
		stream = reader.read(b);
		response = new String(b, 0, stream);
		return response;
	}

}