import static java.lang.System.*;
import java.io.*;
import java.util.*;

public class Meituan{
	static List l = new LinkedList();

	static{
		out.println("Meituan");
	}
	public static void main(String[] args) throws IOException{
        System.out.println(Zu.value);
    	Scanner in = new Scanner(System.in);
        String a = in.nextLine();
        String b = in.nextLine();
        int as = a.length();
        int bs = b.length();
        int length = 0;
        for(int i=0;i<as-bs+1;i++){
        	for(int j=0;j<bs;j++){
            	if(!a.substring(i+j,i+j+1).equals(b.substring(j,j+1)))
                    length++;
            }
        }
    }
    public static void va(String...a){
    	out.println(a.length);
    }
}
class Zu extends Zt{
	static{
		out.println("Zu"+Zi.name+new Zu().get());
	}
	public String get(){
		return "HZY";
	}

}
class Zt{
	static int value = 0;

	static{
		out.println("Zt");
	}
}
interface Zi{
	static String name = "红招远";
	default void a(){
		out.println(name);
	}
}