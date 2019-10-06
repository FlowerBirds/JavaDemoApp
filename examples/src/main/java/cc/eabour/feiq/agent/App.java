package cc.eabour.feiq.agent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	try {
    		FileReader reader = new FileReader("F:\\workspace\\giteclipse\\feiq.agent\\src\\main\\java\\cc\\eabour\\feiq\\agent\\package.txt");
        	BufferedReader br = new BufferedReader(reader);
        	String line = br.readLine();
        	br.close();
        	System.out.println(line);
        	
            InetAddress group = InetAddress.getByName("226.81.9.8");
            MulticastSocket msr = new MulticastSocket(8888);
            System.out.println(InetAddress.getLocalHost());
            msr.setInterface(InetAddress.getByName("192.168.1.107"));
            msr.joinGroup(group);
            //byte[] sendData = FeiqUtil.getSendData(3033301220L, "1", "Peter", "PC", "D850E6B9F09C");
            byte[] sendData = StringUtil.hexTobytes(line);
            //byte[] sendData = "hello".getBytes();
            System.out.println(StringUtil.bytesTohex(sendData));
            DatagramPacket dp = new DatagramPacket(sendData, sendData.length, group, 2425);
            msr.send(dp);
            msr.close();
            System.out.println("over");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
