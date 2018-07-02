package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Request {
	public String responseHeader;
	public String[] firstLine;
	public BufferedReader br;
	public String fileName;
	public String fileType;
	public boolean isPost;
	public Socket s;
	
	public String fileName() throws IOException {
        this.br = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
        this.firstLine = br.readLine().split(" ");

        fileName = firstLine[1];
        if(firstLine[0].equals("POST")) {
        	this.isPost = true;
        } else {
        	this.isPost = false;
        }
		return firstLine[1];
	}
}
