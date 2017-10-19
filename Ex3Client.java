/*****************************************
*	Alfredo Ceballos
*	CS 380 - Computer Networks
*	Excercise 3
*	Professor Nima Davarpanah
*****************************************/

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.InputStream;
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
			
			// Byte array set up to hold byte data. If the number
			// of bytes to be read is odd, then the byte array size is // increased by 1 to even it out.
			byte[] bytes = null;
			if(numBytes % 2 != 0) {
				bytes = new byte[numBytes + 1];
			} else {
				bytes = new byte[numBytes];
			}
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
		}// End of try catch
		
	}// End of main
	
	public static short checksum(byte[] b) {
		int intTotal = 0; // Integers are 32 bits in Java
		byte zeroByte = 0;
		
		for(int i = 0; i < b.length; i += 2) {
			// Taking in 2 sets of 8 bits in order to create a 
			// 16 bit number
			int intTemp = b[i];
			
			// First 8 bits already saved in temp value,
			// shift over 8 bits to make room for next 8
			intTemp = (intTemp << 8) & 0xFF00;
			intTemp += (b[i + 1] & 0xFF);
			
			// Add those 16 bits to the total sum
			intTotal += intTemp;

			/**
			 * If adding a 16 bit number to the current total triggers 
			 * an overflow, wrap around
			 */
			if((intTotal & 0xFFFF0000) > 0) {
				intTotal &= 0xFFFF;
				intTotal++;
			}
		} // End of for loop
		
		// Taking the total, converting it to its 1's complement
		// and casting that value to a short value
		return (short)(~(intTotal & 0xFFFF));
	}// End of checksum

}