// import com.jogamp.opengl.GLAutoDrawable;//import javax.media.opengl.GLAutoDrawable;
// import com.jogamp.opengl.GLCapabilities;//import javax.media.opengl.GLCapabilities;
// import com.jogamp.opengl.GLEventListener;//import javax.media.opengl.GLEventListener;
// import com.jogamp.opengl.GLProfile;//import javax.media.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;//import javax.media.opengl.awt.GLCanvas;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.math.Matrix4;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import javax.swing.JFrame;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.lang.Math;
import java.io.File;
import java.io.FileInputStream;
import static java.lang.System.out;

public class BaseFrame implements GLEventListener{
	private D3D d;
	@Override
	public void display(GLAutoDrawable drawable) {
		d.display(drawable);
	}
	@Override
	public void dispose(GLAutoDrawable drawable) {
    	d.dispose(drawable);
	}
	@Override
	public void init(GLAutoDrawable drawable) {
		d.init(drawable);
   	}
	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3,int arg4) {
		d.reshape(drawable, arg1, arg2, arg3, arg4);
	}
   	public BaseFrame(D3D d){
   		this.d = d;
   	}
	public static void main(String[] args) throws Exception{
		build(new RingLoc(5, 2, 360, 6));
	}
	public static void build(D3DBuffer db){
		final GLProfile profile = GLProfile.get(GLProfile.GL2);
		GLCapabilities capbilities = new GLCapabilities(profile);
   		final GLCanvas canvas = new GLCanvas(capbilities);
   		D3D draw = new D3D(db);
     	canvas.addGLEventListener(new BaseFrame(draw));
     	canvas.setSize(draw.wid, draw.hei);
    	final JFrame frame = new JFrame("'");
    	frame.getContentPane().add(canvas);
    	frame.setSize(frame.getContentPane().getPreferredSize());
    	frame.setVisible(true);
    	final FPSAnimator animator = new FPSAnimator(canvas, 60, true);
    	animator.start();
	}
	static int loadShader(GLAutoDrawable drawable){
		String[] triangles_vert = new String[1];
		triangles_vert[0] = readShader("mc.vp");
		String[] triangles_frag = new String[1];
		triangles_frag[0] = readShader("mc.fp");
		final GL2 gl = drawable.getGL().getGL2();
		int vertexShaderID = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		int fragmentShaderID = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

		boolean bRet = compileShader(vertexShaderID, triangles_vert, drawable);
		bRet = compileShader(fragmentShaderID, triangles_frag, drawable);

		int ProgramID = gl.glCreateProgram();
		gl.glAttachShader(ProgramID, vertexShaderID);
		gl.glAttachShader(ProgramID, fragmentShaderID);
		gl.glBindAttribLocation(ProgramID, 0, "vPosition");
		gl.glBindAttribLocation(ProgramID, 1, "color");
		gl.glBindAttribLocation(ProgramID, 2, "normal");
		gl.glLinkProgram(ProgramID);
		byte[] bu = new byte[1000];
		gl.glGetProgramInfoLog(ProgramID, 1000, null, 0, bu, 0);
		out.println("\033[31m"+"link\n"+new String(bu)+"\033[0m");

		gl.glDeleteShader(vertexShaderID);
		gl.glDeleteShader(fragmentShaderID);

		return ProgramID;
	}
	static boolean compileShader(int id, String[] s, GLAutoDrawable drawable){
		final GL2 gl = drawable.getGL().getGL2();
		gl.glShaderSource(id, 1, s, null);
		gl.glCompileShader(id);

		int[] params = new int[1];
		gl.glGetShaderiv(id, GL2.GL_COMPILE_STATUS, params, 0);
		if(params[0]<=0){
			out.println("!!!shader");
			gl.glGetShaderiv(id, GL2.GL_INFO_LOG_LENGTH, params, 0);
			byte[] bu = new byte[params[0]];
			gl.glGetShaderInfoLog(id, params[0], null , 0, bu, 0);
			out.printf("\033[31m"+new String(bu)+"\033[0m");
			// while(true);
		}
		return true;
	}
	static String readShader(String name){
		File f = new File(name);
		byte[] buf={};
		try{
			FileInputStream in = new FileInputStream(f);
			buf = new byte[in.available()];
			in.read(buf);
		}catch(Exception e){out.println("Shader读取失败!");}
		return new String(buf);
	}
}
interface D3DBuffer{
	public int getLocLength();
	public int getNorLength();
	public int getTexLength();
	public FloatBuffer getLocBuffer();
	public FloatBuffer getNomBuffer();
	public FloatBuffer getTexBuffer();
}
class BlockLoc{
	float[] d = new float[]{
		-1, 1, 1, 1,	//1
		-1, -1, 1, 1,	//2
		1, -1, 1, 1,	//3
		1, -1, 1, 1,	//3
		1, 1, 1, 1,		//4
		-1, 1, 1, 1,	//1

		-1, 1, -1, 1,	//8
		-1, -1, -1, 1,	//7
		1, -1, -1, 1,	//6
		1, -1, -1, 1,	//6
		1, 1, -1, 1,	//5
		-1, 1, -1, 1,	//8

		1, -1, 1, 1,	//3
		1, 1, -1, 1,	//5
		1, 1, 1, 1,		//4
		1, -1, 1, 1,	//3
		1, -1, -1, 1,	//6
		1, 1, -1, 1,	//5

		-1, 1, 1, 1,	//1
		-1, 1, -1, 1,	//8
		-1, -1, 1, 1,	//2
		-1, -1, 1, 1,	//2
		-1, 1, -1, 1,	//8
		-1, -1, -1, 1,	//7

		-1, 1, 1, 1,	//1
		1, 1, 1, 1,		//4
		-1, 1, -1, 1,	//8
		1, 1, 1, 1,		//4
		1, 1, -1, 1,	//5
		-1, 1, -1, 1,	//8

		-1, -1, 1, 1,	//2
		1, -1, 1, 1,	//3
		1, -1, -1, 1,	//6
		-1, -1, 1, 1,	//2
		-1, -1, -1, 1,	//7
		1, -1, -1, 1,	//6
	};
	int Length;
	FloatBuffer bu;
	FloatBuffer bun;
	public BlockLoc(){
		Length = d.length;
		bu = FloatBuffer.wrap(d);
	}
	public BlockLoc(float x, float y, float z){
		Length = d.length;
		for(int i=0; i<d.length; i++){
			if(i%4==0)
				d[i] *= x;
			if(i%4==1)
				d[i] *= y;
			if(i%4==2)
				d[i] *= z;
		}
		bu = FloatBuffer.wrap(d);
	}
	public BlockLoc translate(float x, float y, float z){
		for(int i=0; i<d.length; i++){
			if(i%4==0)
				d[i] += x;
			if(i%4==1)
				d[i] += y;
			if(i%4==2)
				d[i] += z;
		}
		bu = FloatBuffer.wrap(d);
		return this;
	}
	public int getLength(){
		return Length;
	}
	public FloatBuffer getLocBuffer(){
		return bu;
	}
}
class BlockTex{	//hw
	float[] d = new float[]{
		0, 1, //1
		0, 0, //2
		1, 0, //3
		1, 0, //3
		1, 1, //4
		0, 1, //1

		1, 1, //8
		1, 0, //7
		0, 0, //6
		0, 0, //6
		0, 1, //5
		1, 1, //8

		0, 0, //3
		1, 1, //5
		0, 1, //4
		0, 0, //3
		1, 0, //6
		1, 1, //5

		1, 1, //1
		0, 1, //8
		1, 0, //2
		1, 0, //2
		0, 1, //8
		0, 0, //7

		0, 0, //1
		1, 0, //4
		0, 1, //8
		1, 0, //4
		1, 1, //5
		0, 1, //8

		0, 1, //2
		1, 1, //1
		1, 0, //6
		0, 1, //2
		0, 0, //7
		1, 0, //6
	};
	int Length;
	FloatBuffer bu;
	public BlockTex(){
		Length = d.length;
		bu = FloatBuffer.wrap(d);
	}
	public BlockTex(float h, float w, float x, float y){
		Length = d.length;
		for(int i=0; i<d.length; i++){
			if(i%2==0)
				d[i] = d[i]*h+y;
			if(i%2==1)
				d[i] = d[i]*w+x;
		}
		bu = FloatBuffer.wrap(d);
	}
	public BlockTex translate(float x, float y){
		for(int i=0; i<d.length; i++){
			if(i%2==0)
				d[i] += y;
			if(i%2==1)
				d[i] += x;
		}
		bu = FloatBuffer.wrap(d);
		return this;
	}
	public int getLength(){
		return Length;
	}
	public FloatBuffer getLocBuffer(){
		return bu;
	}
}
class MapLoc{
	int Length=0;
	FloatBuffer bu=null;
	public MapLoc removeAll(){
		bu=null;
		return this;
	}
	public MapLoc add(BlockLoc ...args){
		int len=Length;
		for(BlockLoc b: args)
			len += b.getLength();
		float[] fb = new float[len];
		int i=0;
		for(BlockLoc b: args)
			for(float f: b.getLocBuffer().array())
				fb[i++] = f;
		bu = FloatBuffer.wrap(fb);
		Length = len;
		return this;
	}
	public int getLength(){
		return Length;
	}
	public FloatBuffer getLocBuffer(){
		return bu;
	}
}
class RingLoc implements D3DBuffer{	//XY
	float[] d;
	float[] dn;
	float[] dt;
	FloatBuffer bu;
	FloatBuffer bun;
	FloatBuffer but;
	int LocLength=0;
	int NorLength=0;
	int TexLength=0;
	public RingLoc(float rl, float rs, float angle, int midu){	//midu shi ge shu/360
		int r = 0;
		int jie = 0;
		if(angle>=360||angle<0){
			jie = midu;
			r = midu*jie*2;
		}
		else{
			float t = angle/(360.0f/midu);
			jie = (int)Math.ceil(t);
			r = jie*midu*2;
		}
		d = new float[r*3*4];	LocLength = r*3*4;
		dn = new float[r*3*4];	NorLength = r*3*4;
		dt = new float[r*3*4];	NorLength = r*3*4;
		float angle_bit = 360.0f/midu;
		float[][] cycle0 = new float[midu][4];
		for(int i=0;i<midu;i++){
			cycle0[i][0] = 0;
			cycle0[i][1] = rl+(float)Math.sin(Math.PI/180*i*angle_bit)*rs;
			cycle0[i][2] = (float)Math.cos(Math.PI/180*i*angle_bit)*rs;
			cycle0[i][3] = 1;
		}
		float[] dot0 = new float[]{0,rl};
		//cosa -sina
		//sina cosa
		int index = 0;
		float angle_f = angle/jie;
		//texture
		float tex_bit=1.0f/midu;
		for(int j=0; j<jie;j++){
			//dang qian huan
			float[][] cycle1 = new float[midu][4];
			float[] dot1 = new float[2];
			//dot
			for(int i=0;i<midu;i++){
				cycle1[i][0] = cycle0[i][0]*(float)Math.cos(Math.PI/180*angle_f)-cycle0[i][1]*(float)Math.sin(Math.PI/180*angle_f);
				cycle1[i][1] = cycle0[i][0]*(float)Math.sin(Math.PI/180*angle_f)+cycle0[i][1]*(float)Math.cos(Math.PI/180*angle_f);
				cycle1[i][2] = cycle0[i][2];
				cycle1[i][3] = 1;
			}
			//normal
			dot1[0] = dot0[0]*(float)Math.cos(Math.PI/180*angle_f)-dot0[1]*(float)Math.sin(Math.PI/180*angle_f);
			dot1[1] = dot0[0]*(float)Math.sin(Math.PI/180*angle_f)+dot0[1]*(float)Math.cos(Math.PI/180*angle_f);
			//texture

			//jian li wang 
			for(int i=0;i<midu;i++){
				//first triangle
				dt[index] = i*tex_bit;					dn[index] = cycle1[i%midu][0]-dot1[0];			d[index++] = cycle1[i%midu][0];
				dt[index] = (j+1)*tex_bit;				dn[index] = cycle1[i%midu][1]-dot1[1];			d[index++] = cycle1[i%midu][1];
				dt[index] = 0;							dn[index] = cycle1[i%midu][2];					d[index++] = cycle1[i%midu][2];
				dt[index] = 0;							dn[index] = cycle1[i%midu][3];					d[index++] = cycle1[i%midu][3];
				dt[index] = (i+1)*tex_bit;				dn[index] = cycle0[(i+1)%midu][0]-dot0[0];		d[index++] = cycle0[(i+1)%midu][0];
				dt[index] = j*tex_bit;					dn[index] = cycle0[(i+1)%midu][1]-dot0[1];		d[index++] = cycle0[(i+1)%midu][1];
				dt[index] = 0;							dn[index] = cycle0[(i+1)%midu][2];				d[index++] = cycle0[(i+1)%midu][2];
				dt[index] = 0;							dn[index] = cycle0[(i+1)%midu][3];				d[index++] = cycle0[(i+1)%midu][3];
				dt[index] = i*tex_bit;					dn[index] = cycle0[i%midu][0]-dot0[0];			d[index++] = cycle0[i%midu][0];
				dt[index] = j*tex_bit;					dn[index] = cycle0[i%midu][1]-dot0[1];			d[index++] = cycle0[i%midu][1];
				dt[index] = 0;							dn[index] = cycle0[i%midu][2];					d[index++] = cycle0[i%midu][2];
				dt[index] = 0;							dn[index] = cycle0[i%midu][3];					d[index++] = cycle0[i%midu][3];
				//second triangle
				dt[index] = ((i==0)?midu:i)*tex_bit;	dn[index] = cycle1[i%midu][0]-dot1[0];			d[index++] = cycle1[i%midu][0];
				dt[index] = (j+1)*tex_bit;				dn[index] = cycle1[i%midu][1]-dot1[1];			d[index++] = cycle1[i%midu][1];
				dt[index] = 0;							dn[index] = cycle1[i%midu][2];					d[index++] = cycle1[i%midu][2];
				dt[index] = 0;							dn[index] = cycle1[i%midu][3];					d[index++] = cycle1[i%midu][3];
				dt[index] = i*tex_bit;					dn[index] = cycle0[i%midu][0]-dot0[0];			d[index++] = cycle0[i%midu][0];
				dt[index] = j*tex_bit;					dn[index] = cycle0[i%midu][1]-dot0[1];			d[index++] = cycle0[i%midu][1];
				dt[index] = 0;							dn[index] = cycle0[i%midu][2];					d[index++] = cycle0[i%midu][2];
				dt[index] = 0;							dn[index] = cycle0[i%midu][3];					d[index++] = cycle0[i%midu][3];
				dt[index] = ((i+midu-1)%midu)*tex_bit;	dn[index] = cycle1[(i+midu-1)%midu][0]-dot1[0];	d[index++] = cycle1[(i+midu-1)%midu][0];
				dt[index] = (j+1)*tex_bit;				dn[index] = cycle1[(i+midu-1)%midu][1]-dot1[1];	d[index++] = cycle1[(i+midu-1)%midu][1];
				dt[index] = 0;							dn[index] = cycle1[(i+midu-1)%midu][2];			d[index++] = cycle1[(i+midu-1)%midu][2];
				dt[index] = 0;							dn[index] = cycle1[(i+midu-1)%midu][3];			d[index++] = cycle1[(i+midu-1)%midu][3];
			}

			cycle0 = cycle1;
			dot0 = dot1;
		}
		bu = FloatBuffer.wrap(d);
		bun = FloatBuffer.wrap(dn);
		but = FloatBuffer.wrap(dt);
	}
	public int getLocLength(){
		return LocLength;
	}
	public int getNorLength(){
		return NorLength;
	}
	public int getTexLength(){
		return TexLength;
	}
	public FloatBuffer getLocBuffer(){
		return bu;
	}
	public FloatBuffer getNomBuffer(){
		return bun;
	}
	public FloatBuffer getTexBuffer(){
		return but;
	}
}
class D3D{
	int VAOs[] = new int[1];
	int buffers[] = new int[3];
	Matrix4[] matrix=new Matrix4[2];
	int[] program = new int[4];
	int[] framebuffer=new int[2];
	int[] renderbuffer=new int[3];
	int[] texture=new int[3];
	D3DBuffer m;
	int vec=0;
	float time = 90;
	public int wid=600,hei=600;
	public D3D(){
		m = new RingLoc(5, 2, 360, 40);
		vec = m.getLocLength();
	}
	public D3D(D3DBuffer m){
		this.m = m;
		vec = m.getLocLength();
	}
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3,int arg4) {
		out.printf("\033[34m reshape\n\033[0m");
	}
	public void init(GLAutoDrawable drawable) {
		out.printf("\033[34m init\n\033[0m");
		final GL2 gl = drawable.getGL().getGL2();
		{
			int[] si = new int[1];
			gl.glGetIntegerv(GL2.GL_MAX_TEXTURE_UNITS, si, 0);
	   		out.printf("\033[32m"+gl.glGetString(GL2.GL_VERSION)+"\n:"+si[0]+"\033[0m\n");
		}
		gl.glClearColor(0.3f, 0.3f, 0.3f, 0.0f);
		gl.glEnable(GL2.GL_DEPTH_TEST);
		gl.glGenVertexArrays(1, VAOs, 0);
		gl.glBindVertexArray(VAOs[0]);

		gl.glGenBuffers(3, buffers, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[0]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vec*4, m.getLocBuffer(), GL2.GL_STATIC_DRAW);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[1]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vec*4, m.getNomBuffer(), GL2.GL_STATIC_DRAW);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[2]);
		gl.glBufferData(GL2.GL_ARRAY_BUFFER, vec*4, m.getTexBuffer(), GL2.GL_STATIC_DRAW);

		program[0] = loadShader(drawable, new String[]{"mc.vp","mc.fp"}, new int[]{0,1,2}, new String[]{"vPosition","color","normal"});
		program[1] = loadShader(drawable, new String[]{"shadow.vp","shadow.fp"}, new int[]{0}, new String[]{"vPosition"});
		
		// gl.glUseProgram(program[0]);		
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[0]);
		gl.glVertexAttribPointer(0, 4, GL2.GL_FLOAT, false, 0, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[2]);
		gl.glVertexAttribPointer(1, 4, GL2.GL_FLOAT, false, 0, 0);
		gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, buffers[1]);
		gl.glVertexAttribPointer(2, 4, GL2.GL_FLOAT, false, 0, 0);

		//fbo0
		gl.glGenFramebuffers(2, framebuffer, 0);
		gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[0]);
		gl.glGenRenderbuffers(3, renderbuffer, 0);
		gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, renderbuffer[0]);
		gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_RGBA8, wid, hei);
		gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, renderbuffer[1]);
		gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_DEPTH_COMPONENT32, wid, hei);
		gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, renderbuffer[2]);
		gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_RGBA8, wid, hei);
		gl.glFramebufferRenderbuffer(GL2.GL_DRAW_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT2, GL2.GL_RENDERBUFFER, renderbuffer[0]);
		gl.glFramebufferRenderbuffer(GL2.GL_DRAW_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_RENDERBUFFER, renderbuffer[1]);
		gl.glFramebufferRenderbuffer(GL2.GL_DRAW_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_RENDERBUFFER, renderbuffer[2]);
		//fbo1
		gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[1]);
		int[] yy = new int[1];
		gl.glGenRenderbuffers(1, yy, 0);
		gl.glBindRenderbuffer(GL2.GL_RENDERBUFFER, yy[0]);
		gl.glRenderbufferStorage(GL2.GL_RENDERBUFFER, GL2.GL_DEPTH_COMPONENT32, wid*2, hei*2);
		gl.glFramebufferRenderbuffer(GL2.GL_DRAW_FRAMEBUFFER, GL2.GL_DEPTH_ATTACHMENT, GL2.GL_RENDERBUFFER, yy[0]);
		//Matrix
		matrix[0] = new Matrix4();
		matrix[0].loadIdentity();
		matrix[1] = new Matrix4();
		matrix[1].loadIdentity();
		//texture
		try{
			gl.glActiveTexture(GL2.GL_TEXTURE0);
			File fin = new File("stone.tga");
			Texture t1 = TextureIO.newTexture(fin, true);
			texture[0] = t1.getTextureObject(gl);

			gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[1]);
			gl.glActiveTexture(GL2.GL_TEXTURE1);
			gl.glGenTextures(1, texture, 1);
			gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[1]);
			gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA8, wid*2, hei*2, 0, GL2.GL_RGBA, GL2.GL_FLOAT, null);
			gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, GL2.GL_DECAL);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
			gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
			gl.glFramebufferTexture2D(GL2.GL_DRAW_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT1, GL2.GL_TEXTURE_2D, texture[1], 0);
		}
		catch(Exception e){}
		int[] route = new int[]{GL2.GL_COLOR_ATTACHMENT0,GL2.GL_COLOR_ATTACHMENT1,GL2.GL_COLOR_ATTACHMENT2};
		//fbo[1]
		gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[0]);
		gl.glDrawBuffers(3,route,0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
		//fbo[2]
		route = new int[]{GL2.GL_COLOR_ATTACHMENT0,GL2.GL_COLOR_ATTACHMENT1};
		gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[1]);
		gl.glDrawBuffers(2,route,0);
		gl.glEnable(GL2.GL_TEXTURE_2D);
   	}
   	public void dispose(GLAutoDrawable drawable) {
      //method body
	}
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glViewport(0,0,wid*2,hei*2);
		time += 0.3;
		//draw shadow
		gl.glUseProgram(program[1]);
		int world2light = gl.glGetUniformLocation(program[1], "world2light");

		matrix[1].loadIdentity();
		matrix[1].rotate(3.14f/180*(time+90), 0, 1, 0);
		matrix[1].scale(0.1f, 0.1f, 0.1f);
		gl.glUniformMatrix4fv(world2light, 1, false, matrix[1].getMatrix(), 0);

		gl.glBindVertexArray(VAOs[0]);
			gl.glEnableVertexAttribArray(0);
				gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[1]);
				gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
				gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vec);
				gl.glFlush();
			gl.glDisableVertexAttribArray(0);
		gl.glBindVertexArray(0);
		//
		gl.glViewport(0,0,wid,hei);
		gl.glUseProgram(program[0]);
		int world2view = gl.glGetUniformLocation(program[0], "world2view");
		world2light = gl.glGetUniformLocation(program[0], "world2light");
		int texloc1 = gl.glGetUniformLocation(program[0], "tex1");
		int texloc0 = gl.glGetUniformLocation(program[0], "tex0");
		int milli = gl.glGetUniformLocation(program[0], "time");

		matrix[0].loadIdentity();
		matrix[0].rotate(3.14f/180*(time+0), 0, 1, 0);
		matrix[0].scale(0.05f, 0.05f, 0.05f);
		gl.glUniformMatrix4fv(world2view, 1, false, matrix[0].getMatrix(), 0);
		gl.glUniformMatrix4fv(world2light, 1, false, matrix[1].getMatrix(), 0);
		gl.glUniform1i(texloc1, 1);
		gl.glUniform1i(texloc0, 0);
		gl.glUniform1i(milli, (int)time);


		gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, framebuffer[0]);
			gl.glBindVertexArray(VAOs[0]);
				gl.glEnableVertexAttribArray(0);
				gl.glEnableVertexAttribArray(1);	
				gl.glEnableVertexAttribArray(2);
					gl.glClear(GL2.GL_COLOR_BUFFER_BIT|GL2.GL_DEPTH_BUFFER_BIT);
					gl.glDrawArrays(GL2.GL_TRIANGLES, 0, vec);
					gl.glFlush();
				gl.glDisableVertexAttribArray(0);
				gl.glDisableVertexAttribArray(1);	
				gl.glDisableVertexAttribArray(2);
			gl.glBindVertexArray(0);
		//fbo[1] -> fbo[0]
		gl.glBindFramebuffer(GL2.GL_READ_FRAMEBUFFER, framebuffer[0]);
		gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, 0);
		gl.glReadBuffer(GL2.GL_COLOR_ATTACHMENT0);
		gl.glBlitFramebuffer(0, 0, wid-1, hei-1, 0, 0, (wid-1), (hei-1), GL2.GL_COLOR_BUFFER_BIT, GL2.GL_LINEAR);
		//fbo[2] -> fbo[0]
		// gl.glBindFramebuffer(GL2.GL_READ_FRAMEBUFFER, framebuffer[1]);
		// gl.glBindFramebuffer(GL2.GL_DRAW_FRAMEBUFFER, 0);
		// gl.glReadBuffer(GL2.GL_COLOR_ATTACHMENT1);
		// gl.glBlitFramebuffer(0, 0, wid*2, hei*2, wid/2, 0, wid, hei/2, GL2.GL_COLOR_BUFFER_BIT, GL2.GL_LINEAR);
	}
	int loadShader(GLAutoDrawable drawable, String name[], int[] verb, String[] shaderinput){
		String[] triangles_vert = new String[1];
		triangles_vert[0] = readShader(name[0]);
		String[] triangles_frag = new String[1];
		triangles_frag[0] = readShader(name[1]);
		final GL2 gl = drawable.getGL().getGL2();
		int vertexShaderID = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
		int fragmentShaderID = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);

		boolean bRet = compileShader(vertexShaderID, triangles_vert, drawable);
		bRet = compileShader(fragmentShaderID, triangles_frag, drawable);

		int ProgramID = gl.glCreateProgram();
		gl.glAttachShader(ProgramID, vertexShaderID);
		gl.glAttachShader(ProgramID, fragmentShaderID);
		int index=0;
		for(int i: verb)
			gl.glBindAttribLocation(ProgramID, i, shaderinput[index++]);
		gl.glLinkProgram(ProgramID);
		byte[] bu = new byte[1000];
		gl.glGetProgramInfoLog(ProgramID, 1000, null, 0, bu, 0);
		out.println("\033[31m"+"link\n"+new String(bu)+"\033[0m");

		gl.glDeleteShader(vertexShaderID);
		gl.glDeleteShader(fragmentShaderID);

		return ProgramID;
	}
	boolean compileShader(int id, String[] s, GLAutoDrawable drawable){
		final GL2 gl = drawable.getGL().getGL2();
		gl.glShaderSource(id, 1, s, null);
		gl.glCompileShader(id);

		int[] params = new int[1];
		gl.glGetShaderiv(id, GL2.GL_COMPILE_STATUS, params, 0);
		if(params[0]<=0){
			out.println("!!!shader");
			gl.glGetShaderiv(id, GL2.GL_INFO_LOG_LENGTH, params, 0);
			byte[] bu = new byte[params[0]];
			gl.glGetShaderInfoLog(id, params[0], null , 0, bu, 0);
			out.printf("\033[31m"+new String(bu)+"\033[0m");
			// while(true);
		}
		return true;
	}
	String readShader(String name){
		File f = new File(name);
		byte[] buf={};
		try{
			FileInputStream in = new FileInputStream(f);
			buf = new byte[in.available()];
			in.read(buf);
		}catch(Exception e){out.println("Shader读取失败!");}
		return new String(buf);
	}
}
