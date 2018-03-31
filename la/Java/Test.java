import static java.lang.System.out;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import java.nio.charset.Charset;
import java.lang.*;

public class Test{
	static int count = 0;

	public static void main(String[] args) throws Exception{
		ServerSocket a = null;
		try{
			a = new ServerSocket(4555);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		byte[] buffer = new byte[2048];
		int length = 0;
		try(FileInputStream fis = new FileInputStream(new File("../t.html"))){
			length = fis.read(buffer);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		String respond = "HTTP/1.1 200 OK\r\nContent-Type: text/html; charset=utf-8\r\n"+"Content-Length: "+length+"\r\n\r\n";
		byte[] res = respond.getBytes();
		while(true){
			Socket s = a.accept();
			try{
				out.println("+");
				InputStream bi = new DataInputStream(s.getInputStream());
				byte[] buf = new byte[1024];
				int len;
				Thread.sleep(10);
				while(bi.available()>0)
					if((len=bi.read(buf))>0)
						out.print(new String(buf,0,len));
				out.println();

				OutputStream os = s.getOutputStream();
				os.write(res);
				os.write(buffer, 0, length);
				s.close();
				out.println("-");
			}
			catch(Exception e){
				out.println("连接断开");
			}
		}
	}
	static class ClassIndex{
		ClassIndex a;
		public ClassIndex(){

		}
		public ClassIndex(ClassIndex a){
			this.a = a;
		}
		public void set(ClassIndex a){
			this.a = a;
		}
	}
}