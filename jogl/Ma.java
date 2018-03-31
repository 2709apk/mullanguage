import static java.lang.System.out;
import java.lang.String;
import java.util.Scanner;
import java.nio.file.Paths;
import java.io.IOException;
import java.nio.FloatBuffer;

class Ma{
	static float[] tempv=null;
	static int tempvlen=0;
	static float[] tempvn=null;
	static int tempvnlen=0;
	static float[] tempvt=null;
	static int tempvtlen=0;


	static int vlen=0;
	static float[] v=null;
	static float[] vn=null;
	static float[] vt=null;

	static float expand = 1.5f;

	static{
		tempv = new float[100*3];
		tempvn = new float[100*3];
		tempvt = new float[100*3];
		v = new float[100*4*3];
		vt = new float[100*4*3];
		vn = new float[100*4*3];
	}
	public static void main (String[] args) throws Exception{
		Scanner in = new Scanner(Paths.get("./a.obj"), "utf-8");
		while(in.hasNext())
			sp(in.nextLine());
		BaseFrame.build(getBuffer());
		// BaseFrame.build(new RingLoc(5, 2, 360, 40));
	}
	static void sp(String s){
		String[] str = s.trim().split(" +|/");
		if(str[0].equalsIgnoreCase("0")){
			tempvlen = tempvnlen = tempvtlen = 0;
			tempv = new float[100*3];
			tempvn = new float[100*3];
			tempvt = new float[100*3];
		}
		if(str[0].equalsIgnoreCase("v")){
			tempvlen++;
			if(tempvlen*3>tempv.length){
				float[] temp = new float[(int)(tempvlen*expand)*3];
				int index=0;
				for(float t: tempv)
					temp[index++] = t;
				tempv = temp;
			}

			int head = (tempvlen-1)*3;
			tempv[head] = Float.valueOf(str[1]);
			tempv[head+1] = Float.valueOf(str[2]);
			tempv[head+2] = Float.valueOf(str[3]);
		}
		if(str[0].equalsIgnoreCase("vn")){
			tempvnlen++;
			if(tempvnlen*3>tempvn.length){
				float[] temp = new float[(int)(tempvnlen*expand)*3];
				int index=0;
				for(float t: tempvn)
					temp[index++] = t;
				tempvn = temp;
			}

			int head = (tempvnlen-1)*3;
			tempvn[head] = Float.valueOf(str[1]);
			tempvn[head+1] = Float.valueOf(str[2]);
			tempvn[head+2] = Float.valueOf(str[3]);
		}
		if(str[0].equalsIgnoreCase("vt")){
			tempvtlen++;
			if(tempvtlen*3>tempvt.length){
				float[] temp = new float[(int)(tempvtlen*expand)*3];
				int index=0;
				for(float t: tempvt)
					temp[index++] = t;
				tempvt = temp;
			}

			int head = (tempvtlen-1)*3;
			tempvt[head] = Float.valueOf(str[1]);
			tempvt[head+1] = Float.valueOf(str[2]);
			tempvt[head+2] = 1;
		}
		if(str[0].equalsIgnoreCase("f")){
			vlen++;
			if(vlen*12>v.length){
				float[] temp = null;
				int index = 0;
				temp = new float[(int)(vlen*expand)*12];
				for(float t: v)
					temp[index++] = t;
				v = temp;

				index = 0;
				temp = new float[(int)(vlen*expand)*12];
				for(float t: vn)
					temp[index++] = t;
				vn = temp;

				index = 0;
				temp = new float[(int)(vlen*expand)*12];
				for(float t: vt)
					temp[index++] = t;
				vt = temp;
			}

			if(str[1].length()==0|str[4].length()==0|str[7].length()==0)
				return;
			if(str[2].length()==0|str[5].length()==0|str[8].length()==0){
				out.println("无贴图!");
				str[2] = str[5] = str[8] = "null";
			}
			if(str[3].length()==0|str[6].length()==0|str[9].length()==0){
				out.println("无法线!");
				str[3] = str[6] = str[9] = "null";
			}

			int head = (vlen-1)*12;

			int index = head;
			int v1 = Integer.valueOf(str[1]);
			int v2 = Integer.valueOf(str[4]);
			int v3 = Integer.valueOf(str[7]);
			v[index++] = tempv[(v1-1)*3];
			v[index++] = tempv[(v1-1)*3+1];
			v[index++] = tempv[(v1-1)*3+2];
			v[index++] = 1;
			v[index++] = tempv[(v2-1)*3];
			v[index++] = tempv[(v2-1)*3+1];
			v[index++] = tempv[(v2-1)*3+2];
			v[index++] = 1;
			v[index++] = tempv[(v3-1)*3];
			v[index++] = tempv[(v3-1)*3+1];
			v[index++] = tempv[(v3-1)*3+2];
			v[index++] = 1;

			if(!str[2].equals("null")){
				index = head;
				v1 = Integer.valueOf(str[2]);
				v2 = Integer.valueOf(str[5]);
				v3 = Integer.valueOf(str[8]);
				vt[index++] = tempvt[(v1-1)*3];
				vt[index++] = tempvt[(v1-1)*3+1];
				vt[index++] = tempvt[(v1-1)*3+2];
				vt[index++] = 1;
				vt[index++] = tempvt[(v2-1)*3];
				vt[index++] = tempvt[(v2-1)*3+1];
				vt[index++] = tempvt[(v2-1)*3+2];
				vt[index++] = 1;
				vt[index++] = tempvt[(v3-1)*3];
				vt[index++] = tempvt[(v3-1)*3+1];
				vt[index++] = tempvt[(v3-1)*3+2];
				vt[index++] = 1;
			}

			if(!str[3].equals("null")){
				index = head;
				v1 = Integer.valueOf(str[3]);
				v2 = Integer.valueOf(str[6]);
				v3 = Integer.valueOf(str[9]);
				vn[index++] = tempvn[(v1-1)*3];
				vn[index++] = tempvn[(v1-1)*3+1];
				vn[index++] = tempvn[(v1-1)*3+2];
				vn[index++] = 1;
				vn[index++] = tempvn[(v2-1)*3];
				vn[index++] = tempvn[(v2-1)*3+1];
				vn[index++] = tempvn[(v2-1)*3+2];
				vn[index++] = 1;
				vn[index++] = tempvn[(v3-1)*3];
				vn[index++] = tempvn[(v3-1)*3+1];
				vn[index++] = tempvn[(v3-1)*3+2];
				vn[index++] = 1;
			}
		}
	}
	static D3DBuffer getBuffer(){
		slime();
		return new MaBuffer(v, vt, vn);
	}
	static void slime(){
		float[] temp = null;
		temp = new float[vlen*12];
		for(int i=0;i<vlen*12;i++)
			temp[i] = v[i];
		v = temp;

		temp = new float[vlen*12];
		for(int i=0;i<vlen*12;i++)
			temp[i] = vn[i];
		vn = temp;

		temp = new float[vlen*12];
		for(int i=0;i<vlen*12;i++)
			temp[i] = vt[i];
		vt = temp;

		out.println("slime: "+vt.length);
	}
}
class MaBuffer implements D3DBuffer{
	FloatBuffer A,B,C;
	int al,bl,cl;
	public MaBuffer(float[] a, float[] b, float[] c){
		A = FloatBuffer.wrap(a);al=a.length;
		B = FloatBuffer.wrap(b);bl=b.length;
		C = FloatBuffer.wrap(c);cl=c.length;
	}

	public int getLocLength(){
		return al;
	}
	public int getTexLength(){
		return bl;
	}
	public int getNorLength(){
		return cl;
	}
	public FloatBuffer getLocBuffer(){
		return A;
	}
	public FloatBuffer getTexBuffer(){
		return B;
	}
	public FloatBuffer getNomBuffer(){
		return C;
	}
}