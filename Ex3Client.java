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
import java.nio.ByteBuffer;

public class Ex3Client {
	public static void main(String[] args) {
		try(Socket socket = new Socket("18.221.102.182", 38103)) {
			// Setting up input and output streams
			InputStream in = socket.getInputStream();
			OutputStream out = socket.getOutputStream();
			
			// Checking connection
			String address = socket.getInetAddress().getHostAddress();
			System.out.println("Connected to " + address);
			
			// Reading in number of bytes to be read
			int numBytes = 0 | in.read();
			System.out.printf("Reading %d bytes:%n", numBytes);
			
			// Byte array set up to hold byte data
			byte[] bytes = new byte[numBytes];
			System.out.printf("Data received:%n");
			// Reading in bytes, saving them into array, and printing
			for(int i = 0; i < numBytes; i++) {
				if(i % 10 == 0){System.out.print("   ");}
				bytes[i] = (byte)in.read();
				System.out.printf("%02X",bytes[i]);
				if((i + 1) % 10 == 0){System.out.println();}
			}
			System.out.println();
			
			// Calculating checkSum
			short checkSum = checksum(bytes);
			
			// Setting up a ByteBuffer to help in sending raw bytes
			// to server
			ByteBuffer sumBytes = ByteBuffer.allocate(2);
			sumBytes.putShort(checkSum);
			
			// Sending byte array to server
			out.write(sumBytes.array());
			
			// Checking response
			if(in.read() == 1) {
				System.out.println("Response good!");
			} else {
				System.out.println("Response bad...");
			}
			
		} catch(Exception e){
			System.out.println("It broke...");
			e.printStackTrace();
		}
		
	}// End of main
	
	public static short checksum(byte[] b) {
		int total = 0; // Integers are 32 bits in Java
		short sum = 0; // Shorts are 16 bits in Java
		byte zeroByte = 0;
		
		for(int i = 0; i < b.length; i += 2) {
			// Taking in 2 sets of 8 bits in order to create a 
			// 16 bit number
			short intTemp = b[i];
			intTemp = (short)(intTemp << 8);
			intTemp += b[i];
			
			// If b has an odd number of elements and is at 
			// the last element
			if(b.length % 2 == 1 && i + 1 == b.length){
				System.out.println("Odd number detected");//delete
				
				if(total + intTemp < 0) {
					System.out.println("Overflow detected?? Works???");
					//total = total
				}
				
				sum += (b[i] + zeroByte);
				break; // Last thing to do in this case is break loop
			} 
			
			/**
			 * If adding a 16 bit number to the current total triggers 
			 * an overflow, then a negative number should be generated
			 * since primitives are signed in Java
			 */
			if(total + intTemp < 0) {
				System.out.println("Overflow detected?? Works???");
			}
			
			// Otherwise just add it to the total
			total += intTemp;
			
			// if (triggers overflow)
			// take upper 8 bits 
			// lower 8 bits
			//+upper 8 bits
			// = sum
		}
		return sum;
	}// End of checksum
}