import java.util.*;	//Scanner,Date,ArrayList
import java.util.function.*;//Predicate
import java.util.Calendar;
import java.time.*;//LocalDate
import java.io.*;
import java.net.URL;
import java.nio.file.*;	//Paths
import java.math.*;
import java.lang.Object.*;//Interger
import java.lang.reflect.*;//Reflect 
import java.lang.StringBuilder;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.swing.Timer;
import static java.lang.System.*;
import javafx.scene.*;
import java.lang.Math;

/**
HiWorld
*/

public class HiWorld
{
		static double v_x[]=new double[1];
		static double v_y[]=new double[1];
		static double s_x[]=new double[1];
		static double s_y[]=new double[1];
		static long elevent_pritime[];
		static double point_history[][][];//number,history, x and y
		static int point_history_indix[];
		static private int num=0;
		static Ellipse2D e[];
		static int width = 1000,height = 800;
		static double zuni = 0.1;
		static Toolkit tool = Toolkit.getDefaultToolkit();
		static int flag = 0;
		static int iterator = 0;
		static boolean run = false;
		static int point_length = 100;
		static int every_point_length = 100;
		static int point_x[]=new int[point_length], point_y[]=new int[point_length];
		static int pointindix=0;
		static Suo suo = new Suo();

	public static void main(String[] args) throws Exception
	{
		{//chuang jian xian cheng
			for(int i=0; i<16; i++)
			{
				new Thread(()->
				{
					while(true)
					{
						if(num==0)
						{
							try
							{
								Thread.sleep(1);
							}
							catch(Exception e)
							{}
							continue;
						}
						int in = suo.getIndix(num);
						double tempdouble2 = v_x[in]*v_x[in]+v_y[in]*v_y[in];
						double pix = Math.sqrt(tempdouble2);
						long temp = System.currentTimeMillis();
						long millins = temp-elevent_pritime[in];
							elevent_pritime[in] = temp;
							double tempdouble = millins/20.0;
							jisuan(in, tempdouble);
							pengzhuang(in, tempdouble);
						// try
						// {
						// 	Thread.currentThread().yield();
						// }
						// catch(Exception e)
						// {}
					}
				}).start();
			}
		}
		while(true)
		{
			if(run==false)
			{
				run=true;
				new Thread(()->
				{
					runitem(args);
				}).start();
			}
			try
			{
				Thread.sleep(300);
			}
			catch(Exception e)
			{}
		}
	}
	private static void runitem(String[] args)
	{
		int numt;
		num = 0;
		if(args.length==0)
			numt=10;
		else if(args.length==1)
			numt = new Integer(args[0]);
		else
		{
			numt = new Integer(args[0]);
			zuni = new Double(args[1]);
		}

		v_x = new double[numt];
		v_y = new double[numt];
		s_x = new double[numt];
		s_y = new double[numt];
		elevent_pritime = new long[numt];
		point_history = new double[numt][every_point_length][2];
		point_history_indix = new int[numt];
		e = new Ellipse2D.Double[numt];

		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();
		JFrame jf = new JFrame();
		for(int i=0; i<numt; i++)
		{
			s_y[i] = s_x[i] = 0;
			elevent_pritime[i] = System.currentTimeMillis();
			e[i] = new Ellipse2D.Double(s_x[i], s_y[i], s_x[i]+20, s_y[i]+20);
			v_x[i] = Math.random()*2+0.05*i;
			v_y[i] = Math.random()*2;
		}
		num = numt;

		EventQueue.invokeLater(()->
			{
				jf.setSize(width, height+30);
				jf.setDefaultCloseOperation((jf.EXIT_ON_CLOSE));
				jf.setTitle("Hi");
				// jf.setLocation((screenSize.width-300)/2, (screenSize.height-200)/2);
				jf.setLocation(0, 0);
				Image pic = new ImageIcon("timg.jpeg").getImage();
				jf.setIconImage(pic);
				jf.add(new JComponent()
				{
					public void paintComponent(Graphics g)
					{
						Graphics2D g2 = (Graphics2D)g;

						g2.fill(new Rectangle2D.Double(width/2, height/2, 2, 2));
						{
							// point_x[pointindix] = (int)s_x[0];
							// 	point_y[pointindix] = (int)s_y[0];
							// 	pointindix = (pointindix+1)%point_length;
							// 	for(int i=0; i<point_length; i++)
							// 		g2.fill(new Rectangle2D.Double(point_x[i], point_y[i], 1, 1));

							g2.fill(e[0]);
							// g2.fill(new Rectangle2D.Double(s_x[0], s_y[0], 2, 2));
						}
						for(int i=1; i<num; i+=1)
						{
							g2.draw(e[i]);

							// g2.fill(new Rectangle2D.Double(s_x[i], s_y[i], 2, 2));
							// 	point_history[i][point_history_indix[i]][0] = s_x[i];
							// 	point_history[i][point_history_indix[i]][1] = s_y[i];
							// 	point_history_indix[i] = (point_history_indix[i]+1)%every_point_length;
							// 	for(int k =0; k<every_point_length; k++)
							// 		g2.fill(new Rectangle2D.Double((int)point_history[i][k][0], (int)point_history[i][k][1], 1, 1));
	
							Dimension size = jf.getContentPane().getSize();
							width = (int)(size.getWidth());
							height = (int)(size.getHeight());
						}
						g.drawString(""+iterator, 0, 30);
						if(Math.sqrt( (s_x[0]-width/2)*(s_x[0]-width/2) + (s_y[0]-height/2)*(s_y[0]-height/2) )<1)
						{
							iterator++;
							run = false;
							jf.dispose();
							try
							{
								String shpath = "scrot pic/"+iterator+".png";
								Process p = Runtime.getRuntime().exec(shpath);
								p.waitFor();
							}
							catch(Exception e)
							{}
						}
					}
				});
				jf.setVisible(true);
				jf.toBack();
			});
		int integ = iterator;
		long jframenow = System.currentTimeMillis();
		while(integ == iterator)
		{
			out.println(1000.0/(System.currentTimeMillis()-jframenow));
			jframenow = System.currentTimeMillis();

			jf.repaint();
			try 
			{
				Thread.sleep(10);
			}
			catch(Exception e) 
			{}
		}
	}
	private static void jisuan(int i, double tc)
	{
			//geng xin wei zhi
			s_x[i] += tc*v_x[i];
			s_y[i] += tc*v_y[i];
			// geng xin su du
				// jian dan zhong li
					v_y[i] += tc*0.2;
				// //fangu za zhong li
				// 	comGravity(i, width/2, height/2, 1, tc);
			//jian cha yue jie
				//X
				if(s_x[i]>width)
				{
					v_x[i] -= v_x[i]*zuni;
					s_x[i] = 2*width-s_x[i];
					if(v_x[i]>0)
						v_x[i] = -v_x[i];
				}
				if(s_x[i]<0)
				{
					v_x[i] -= v_x[i]*zuni;
					s_x[i] = -s_x[i];
					if(v_x[i]<0)
						v_x[i] = -v_x[i];
				}
				//Y
				if(s_y[i]>height)
				{
					v_y[i] -= v_y[i]*zuni;
					s_y[i] = 2*height-s_y[i];
					if(v_y[i]>0)
						v_y[i] = -v_y[i];
				}
				if(s_y[i]<0)
				{
					v_y[i] -= v_y[i]*zuni;
					s_y[i] = -s_y[i];
					if(v_y[i]<0)
						v_y[i] = -v_y[i];
				}
			// she zhi da xiao
				e[i].setFrameFromDiagonal(
											(s_x[i]<10)?0:(s_x[i]-10), 
											(s_y[i]<10)?0:(s_y[i]-10), 
											(s_x[i]>width-10)?width:(s_x[i]+10), 
											(s_y[i]>height-10)?height:(s_y[i]+10)
											);

			// out.println(i+" Vx:"+v_x[i]+" Vy:"+v_y[i]);
	}
	private static void pengzhuang(int i, double tc)
	{
		double cycle = 20;
		double c = cycle/2;
		for(int j=0; j<num; j++)
		{
			if(i==j)
				continue;
			//peng zhuang
			if(  (s_x[i]-s_x[j]<cycle) && (s_x[j]-s_x[i]<cycle) && (s_y[i]-s_y[j]<cycle) && (s_y[j]-s_y[i]<cycle)  )
			// if( (s_x[i]-s_x[j])*(s_x[i]-s_x[j])+(s_y[i]-s_y[j])*(s_y[i]-s_y[j]) <= cycle*cycle)
			{
				double vix=v_x[i],viy=v_y[i],vjx=v_x[j],vjy=v_y[j];
				double six=s_x[i],siy=s_y[i],sjx=s_x[j],sjy=s_y[j];
				double x=six-sjx, y=siy-sjy;//j2i
				double l=Math.sqrt(x*x+y*y);
				// if((l-0.0<0.1)&&(l-0.0>-0.1))
				// 	continue;
				if((vix*x+viy*y)<(vjx*x+vjy*y))
				{
					x=x/l; y=y/l;
					double d = (vjx-vix)*x+(vjy-viy)*y;
					v_x[i] = vix+d*x;
					v_y[i] = viy+d*y;
						v_x[j] = vjx-d*x;
						v_y[j] = vjy-d*y;
						// out.println("E%= "+((v_x[i]*v_x[i]+v_y[i]*v_y[i]+v_x[j]*v_x[j]+v_y[j]*v_y[j])/(vix*vix+viy*viy+vjx*vjx+vjy*vjy)));
				}
			}
			else
			{
				//Gravity
				// comGravity(i, s_x[j], s_y[j], 10000, tc);
			}
		}
	}
	private static void comGravity(int i, double px, double py, double gL, double tc)
	{
		final double L = 12;//e12
		double MG = 6*6.674e1/gL; 

		double dx = (px-s_x[i]),dy = (py-s_y[i]);
		double l2 = dx*dx+dy*dy;
		double l = Math.sqrt(l2);
		double xy = MG/l2;
		//fu za zhong li
			v_x[i] += tc*xy*dx/l;//yin li he ju li ping fang cheng fa bi
			v_y[i] += tc*xy*dy/l;
	}
}
class Suo
{
	private int thread_indix=0;//32 thread

	public synchronized int getIndix(int num)
	{
		thread_indix = (thread_indix+1)%num;
		return thread_indix;
	}
}