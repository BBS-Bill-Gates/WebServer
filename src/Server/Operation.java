package Server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Operation {
	public Socket s;
	public BufferedReader br;
	public String responseHeader;
	public String fileName;
	private ResultSet rs;
	private String posthtml = 	"<html>\n" + 
			"    <head>\n" + 
			"        <title>Add Success</title>\n" + 
			"    </head>\n" + 
			"    <body>\n" + 
			"	 	 <h1>%s</h1>\n" + 
			"	 </body>\n"  +
			" </html>\n";
//	public String firstLine;
	private Mysql ms = new Mysql();
	
 	private void fileType() {
		String[] splitContent = this.fileName.split("\\.");			//
        String extensionName = splitContent[splitContent.length-1];
		switch (extensionName) {
		case "ico":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: image/x-icon; charset=UTF-8\r\n\r\n";
			break;
		case "png":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: image/png; charset=UTF-8\r\n\r\n";
			break;
		case "jpg":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: image/jpeg; charset=UTF-8\r\n\r\n";
			break;
		case "js":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: application/x-javascript; charset=UTF-8\r\n\r\n";
			break;
		case "css":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/css; charset=UTF-8\r\n\r\n";
			break;
		case "svg":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/xml; charset=UTF-8\r\n\r\n";
			break;
		case "gif":
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: image/gif; charset=UTF-8\r\n\r\n";
			break;
		default:
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html; charset=UTF-8\r\n\r\n";
			break;
		}
	}
	public byte[] readFile() throws FileNotFoundException  {
		if(this.fileName.equals("/")) {
			this.fileName = "/index.html";
		}
		try {
			String path = "./htdocs" + fileName;
			File f = new File(path);
			Path p = f.toPath();
		    byte[] con = Files.readAllBytes(p);
			return con;
		} catch (Exception e) {
			String path = "./htdocs" + "/404.html";
			File f = new File(path);
			Path p = f.toPath();
		    byte[] con = null;
			try {
				con = contentReader(p);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		   
			return con;
		}

	}
	
	private byte[] contentReader(Path p) throws IOException{
		return Files.readAllBytes(p);
	}
	public byte[] doGet() throws IOException, SQLException {
	
		if(this.fileName.contains("?")) {     //search
			String[] content = getGetdata(this.fileName);
			String studentID = content[2];
			String result;
			this.rs = ms.searchGrade(studentID);
			if(rs != null && rs.next()) {
				this.fileName = "/result.html";
				String html = new String(readFile());
				result = String.format(html, rs.getString("SID"), rs.getString("NAME"),rs.getString("MATH"), rs.getString("ENGLISH"), rs.getString("MAJOR"));
			} else {
				this.fileName = "/result.html";
				String html = new String(readFile());
				result = String.format(html, "NULL", "NULL", "NULL", "NULL");
			}
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html; charset=UTF-8\r\n\r\n"; 
			System.out.println("GET请求参数: " + studentID);
			return result.getBytes();
		}
		fileType();
		return readFile();
	}
	public byte[] doPost() throws IOException, SQLException {
		
		String[] content = getPostdata();
		String studentID = content[1] ;
		String pass = content[3].replace("\0", "");
		String result;
		ResultSet rs = ms.verify(studentID, pass);
		rs.last();
		int number = rs.getRow();
		System.out.println("POST请求参数: studentID(" + studentID + ") " + "pass(" + pass + ")");
		if (number > 0) {
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html; charset=UTF-8\r\n\r\n";
			this.fileName = "/search.html";
			byte[] b = readFile();
			return b;
		} else {
			this.responseHeader = "HTTP/1.1 200 OK\r\n" + "Content-Type: text/html; charset=UTF-8\r\n\r\n";
			result = String.format(posthtml, "Add Failure");
			return result.getBytes();
		}
		
	}
	
	public String[] getPostdata() throws IOException {
		String buffer;
		char[] postdata = new char[1024];
		while(!((buffer = this.br.readLine()).replace("\r\n","").equals("")));
		
		this.br.read(postdata);
		return (new String(postdata)).split("=|&");
	}
	private String[] getGetdata(String firstLine) {
		return firstLine.split("\\?|&|=");
	}
}
