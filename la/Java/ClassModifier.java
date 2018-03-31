import static java.lang.System.out;
import java.lang.Class;
import java.util.Scanner;
import java.lang.StringBuilder;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class ClassModifier{
	public static void main(String[] args) throws Exception{
		Scanner in = new Scanner(System.in);
		String classname = in.next();

		Class cl = Class.forName(classname);
		Class supercl = cl.getSuperclass();
		out.println(Modifier.toString(cl.getModifiers())+" Class"+cl.getName()+"\n{\n");
		out.println(getDeclaredConstructors(cl, " "));
		out.println(getDeclaredMethods(cl, " "));
		out.println(getDeclaredFields(cl, " "));
		out.println("}");
	}
	public static String getDeclaredConstructors(Class cl, String s){
		StringBuilder sb = new StringBuilder();
		Constructor[] c = cl.getDeclaredConstructors();
		for(Constructor ct: c){
			sb.append(s);
				String modifiers = Modifier.toString(ct.getModifiers());
			sb.append(modifiers+" ");
				String name = cl.getName();
			sb.append(name+"(");
				Class[] paramTypes = ct.getParameterTypes();
			if(paramTypes.length>0)
				sb.append(paramTypes[0].getName());
			for(int i=1;i<paramTypes.length;i++){
				sb.append(", ");
				sb.append(paramTypes[i].getName());
			}
			sb.append(");\n");
		}
		return sb.toString();
	}
	public static String getDeclaredMethods(Class cl, String s){
		StringBuilder sb = new StringBuilder();
		Method[] m = cl.getDeclaredMethods();
		for(Method mt: m){
			sb.append(s);
				String modify = Modifier.toString(mt.getModifiers());
			sb.append(modify+" ");
				Class returnType = mt.getReturnType();
			sb.append(returnType.getName()+" ");
				String name = mt.getName();
			sb.append(name+"(");
				Class[] paramTypes = mt.getParameterTypes();
			if(paramTypes.length>0)
				sb.append(paramTypes[0].getName());
			for(int i=1;i<paramTypes.length;i++)
				sb.append(", "+paramTypes[i].getName());
			sb.append(");\n");
		}
		return sb.toString();
	}
	public static String getDeclaredFields(Class cl, String s){
		StringBuilder sb = new StringBuilder();
		Field[] f = cl.getDeclaredFields();
		for(Field ft: f){
			sb.append(s);
				String modify = Modifier.toString(ft.getModifiers());
			sb.append(modify+" ");
			sb.append(ft.getType().getName()+" "+ft.getName()+";\n");
		}
		return sb.toString();
	}
}
