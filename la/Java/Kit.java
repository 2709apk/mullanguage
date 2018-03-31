import static java.lang.System.out;

public class Kit
{
	public Kit()
	{

	}
	public void printByte(byte[] b, int offset, int len)
	{
		for(int i=offset; i<len; i++)
			out.print(" "+b[i]);
		out.println();
	}
	public void printInt(int[] b, int offset, int len)
	{
		for(int i=offset; i<len; i++)
			out.print(" "+b[i]);
		out.println();
	}
	//高位在前
	//large first
	public int getInt(byte[] b, int offset)
	{
		int in = 0;
			in += (int)(b[0+offset]&0xff);
				in = in<<8;
			in += (int)(b[1+offset]&0xff);
				in = in<<8;
			in += (int)(b[2+offset]&0xff);
				in = in<<8;
			in += (int)(b[3+offset]&0xff);

		return in;
	}
	public byte[] int2byte(byte[] b, int off, int n)
	{
		b[3+off]=(byte)(n&0xff);
			n=n>>8;
		b[2+off]=(byte)(n&0xff);
			n=n>>8;
		b[1+off]=(byte)(n&0xff);
			n=n>>8;
		b[0+n]=(byte)(n&0xff);
		return b;
	}
}
