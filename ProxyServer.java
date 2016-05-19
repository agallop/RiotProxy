import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.Inet6Address;
import java.lang.Thread;
import java.lang.Runnable;

public class ProxyServer {

    public static void main(String[] args) throws IOException{
		
		//New Server on LocalHost
        ServerSocket listener = new ServerSocket(9090, 0, Inet6Address.getByName(null));
		
        try {
            while (true) {
                final Socket socket = listener.accept();
				
				//Thread to handle request
				new Thread(new Runnable() {
					public void run(){
						try {
							//Prints request to stderr
							BufferedReader in =
								new BufferedReader(
								new InputStreamReader(socket.getInputStream()));
							System.err.println("Connection Established: " + in.readLine());
							
							//Returns "Hello World" to the client
							PrintWriter out =
							new PrintWriter(socket.getOutputStream(), true);
							out.println("Hello World!");
						} catch (Exception ex) {
								System.err.println(ex.getMessage());
						} finally {
							try{
								socket.close();
							} catch (Exception ex) {
								System.err.println(ex.getMessage());
							}
						}		
					}
				}).start();
				
            }
        }
        finally {
            listener.close();
        }
    }
}