import static java.lang.System.out;

/*MsgArray由数组组成，空的时候有一个节点*/
public class MsgArray
{
	private String name;
	private int port;
	private int length;
	private int has;
	private static int rcvLength = MsServer.rcvLength;
	private Msg top=null,tail=null;
	private int[] number;//100 ge
	private int base=0;//ji chu
	private Msg indicate=null;//ji chu
	public MsgArray(String name, int port, int l)
	{
		this.name = name;
		this.port = port;
		number = new int[100];//zui da jie shou 100 ge shu ju bao
		for(int i=0; i<100; i++)
			number[i]=-1;
		length = l;
		has = 0;
		indicate=top=tail=new Msg(new byte[1]);
	}
	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	public MsgArray addMsg(byte[] b, int in)
	{
		if(in<0||in>=length)
			return null;
		if(base+99<in)
		{
			for(int i=0;i<100;i++)
				if(number[i]<0)
				{
					for(int j=0;j<100;j++)
					{
						if((j+i)<100)
							number[j]=number[j+i];
						else
							number[j]=-1;
					}
					for(int j=0;j<i;j++)
						indicate = indicate.next();
					base += i;
					i=101;
				}
		}
		if(base+99<in)
			return null;
		int d = in-base;
		if(number[d]>=0)
			return this;
		number[d] = in;
		Msg temp = indicate;
		for(int i=0; i<d; i++)
			if(number[i]>0)
				temp=temp.next();
		tail=temp.addItem(b);
		has++;
		return this;
	}
	public boolean isFull()
	{
		return has==length;
	}
	public int[] getIndix()
	{
		int[] buf = new int[100];
		int i=0;
		for(int j=0;(j<100)&&(j+base<=length);j++)
			if(number[j]<0)
				buf[i++]=base+j;
		if(i<1)
			return null;
		int[] buf1 = new int[i];
		for(;i>0;i--)
			buf1[i-1]=buf[i-1];
		return buf1;
	}
	class Msg
	{
		private Msg pr=null, next=null;
		private byte[] buf = null;
		public Msg()
		{
			buf = new byte[1000];
		}
		public Msg(byte[] b)
		{
			buf = b;
		}
		public Msg addItem(byte[] b)
		{
			Msg m = new Msg(b);
			m.next = this.next;
			m.pr = this;
			this.next = m;
			return m;
		}
		public Msg next()
		{
			return this.next;
		}
		public Msg pr()
		{
			return this.pr;
		}
	}
}