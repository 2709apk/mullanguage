import static java.lang.System.out;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.InetAddress;
import java.io.IOException;
import java.util.Arrays;

public class Hi
{
	static final int length = MsServer.rcvLength;
	static byte[] mosu = new byte[]{(byte)0xff, (byte)0xff, (byte)0xa5, (byte)0xa5};
	static Kit kit = new Kit();

	public static void main(String[] args)throws IOException,SocketException
	{
		byte[] buffer = new byte[length];
		kit.int2byte(buffer, 12, new Integer(args[0]));
		kit.int2byte(buffer, 4, 5);
		out.println(kit.getInt(kit.int2byte(buffer, 8, 5), 8));
		DatagramPacket dp = new DatagramPacket(buffer, length, InetAddress.getByName("localhost"), 6666);
		out.println(dp.getAddress().getHostAddress());
		DatagramSocket ds = new DatagramSocket(6665);
		ds.send(dp);
	}
}