import java.lang.StringBuilder;
import java.lang.reflect.*;
import static java.lang.System.out;
import java.util.*;

public class AutoString 
{
	public static String toString(Object obj) throws Exception
	{
		StringBuilder s = new StringBuilder();
		Class c = obj.getClass();
		s.append("\u0010"+String.format(c.getName())+"\n");
		if(c.isArray())
		{
			for(int i=0; i<Array.getLength(obj); i++)
			{
				Object ob = Array.get(obj, i);
				//if is primitive use Array.getint(obj, i)
				s.append(toString(ob)+"\n");
			}
			return s.toString();
		}
		do
		{
			s.append("+"+c.getName()+"\n");
			Field[] f = c.getDeclaredFields();
			for(Field fie: f)
			{
				fie.setAccessible(true);
				s.append(String.format(fie.getName()+" :: "+fie.getType()+"\t["+fie.get(obj))+"] \n");
			}

			c = c.getSuperclass();
		}while(c!=null);

		return s.toString(); 
	}
}