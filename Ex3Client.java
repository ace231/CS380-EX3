/*****************************************
*	Alfredo Ceballos
*	CS 380 - Computer Networks
*	Excercise 3
*	Professor Nima Davarpanah
*****************************************/
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
public class Ex3Client {
	public static void main(String[] args) {
		try(Socket socket = new Socket("18.221.102.182", 38103)) {
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			String address = socket.getInetAddress().getHostAddress();
			System.out.println("Connected to " + address);
			
			int numBytes = 0 | in.read();
			System.out.printf("Reading %d bytes:%n", numBytes);
			
			byte[] bytes = new byte[numBytes];
			System.out.printf("Data received:%n");
			for(int i = 0; i < numBytes; i++) {
				if(i % 10 == 0){System.out.print("   ");}
				bytes[i] = (byte)in.read();
				System.out.printf("%02X",bytes[i]);
				if((i + 1) % 10 == 0){System.out.println();}
			}
			
		} catch(Exception e){
			System.out.println("It broke...");
			e.printStackTrace();
		}
		
	}// End of main
	
	public static short checksum(byte[] b) {
		short sum = 0;
		for(int i = 0; i < b.length; i++) {
			sum += (b[i] + b[i+1]);
			//if((sum & 0xffff0000)) {
			//	sum = (short)(sum & 0xffff);
			//}
		}
		return sum;
	}// End of checksum
}