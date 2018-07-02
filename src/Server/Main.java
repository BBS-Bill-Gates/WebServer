package Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Main {
	private final static int TCP_PORT = 9090;
	public static void main(String[] args) throws IOException, SQLException{
	       ServerSocket ss = new ServerSocket(TCP_PORT);
	       Request request = new Request();
	       Response response = new Response();
	       Operation operation = new Operation();
	       String fileName, fileType;
	       byte[] b = null;
	       while (true) {
		        Socket socket = ss.accept();
		        request.s = socket;
		        
		        operation.fileName = request.fileName();
		        operation.br = request.br;
		        
		        if(request.isPost) {
		        	b = operation.doPost();
		        	
		        } else {
		        	b = operation.doGet();
		        	
		        }
		        response.responseHeader = operation.responseHeader;
		        response.s = socket;
		        response.response(socket, b);
		        socket.close();
	       }
	}
	
}
