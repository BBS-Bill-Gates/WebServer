package Server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Response {
	public String responseHeader;
	public Socket s;
	
	public void response(Socket s, byte[] b) throws IOException {
		OutputStream os = s.getOutputStream();
		os.write(responseHeader.getBytes());
		os.write(b);
	}
}
