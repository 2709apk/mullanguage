import static java.lang.System.out;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.Queue;
import java.util.LinkedList;

public class MsServer
{
	static DatagramSocket receiver;
	static byte[] mosu = new byte[]{(byte)0xff, (byte)0xff, (byte)0xa5, (byte)0xa5};
	public static final int rcvLength = 1016;
	public static final int revPort = 6666;
	static
	{
		try
		{
			receiver = new DatagramSocket(revPort);
		}
		catch(Exception e)
		{
			out.println("打开串口失败！");
		}
	}
	public static void main(String[] args)throws IOException
	{
		new MsServer(new LinkedList<MsgArray>());
	}
	public MsServer(Queue<MsgArray> queue)
	{
		new Thread(()->
			{
				try
				{
					this.run(queue);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					out.println("MsServer.MsServer()");
				}
			}).start();
	}
	private void run(Queue<MsgArray> queue)throws IOException
	{
		HashMap<String,MsgArray> map = new HashMap<String,MsgArray>();
		Kit kit = new Kit();
		while(true)
		{ 
			byte[] buffer = new byte[rcvLength];
			DatagramPacket dp = new DatagramPacket(buffer, rcvLength);
			receiver.receive(dp);
			int name = kit.getInt(buffer, 4);
			String address = dp.getAddress().getHostAddress().toString();
			int port = dp.getPort();
			String key = name+address+port;
			MsgArray ma ;
			if((ma=map.get(key))==null)
			{
				ma = new MsgArray(address, port, kit.getInt(buffer, 8));
				map.put(key, ma);
			}
			ma.addMsg(Arrays.copyOfRange(buffer, 16, rcvLength), kit.getInt(buffer, 12));
			if(ma.isFull())
			{
				queue.offer(ma);
				map.remove(key);
				out.println(name+"进入队列");
			}
		}
	}
}