import static java.lang.System.out;
import java.util.Set;
import javax.swing.Timer;
import java.util.Scanner;
import java.util.List;
import java.util.LinkedList;
import java.util.Arrays;
import java.awt.Toolkit;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.lang.Thread;

public class FindJavaDoc{
	static boolean flag_all = false;
	public static void main(String[] args) throws Exception{
		if(args[0].equals("all"))
			Runtime.getRuntime().exec("opera --new-window file:///home/x/Desktop/jdk8/api/allclasses-noframe.html");
		if(args.length==2)
			if("all".equals(args[1]))
				flag_all = true;
		String content = new String(Files.readAllBytes(Paths.get("/home/x/Desktop/jdk8/api/allclasses-frame.html")), StandardCharsets.UTF_8);
		List<String> words1 = Arrays.asList(content.split("<li><a href=\"|\" title=|classFrame\">|</a></li>"));
		List<String> words2 = Arrays.asList(content.split("<li><a href=\"|\" title=|interfaceName\">|</span></a></li>"));
		List<String> words = new LinkedList();
			words.addAll(words1);
			words.addAll(words2);
		String s1="null",s2="null";
		for(String s: words){
			if(s.equals(args[0])){
				final String path = "opera "+"file:///home/x/Desktop/jdk8/api/"+s1;
				out.printf("\033[1m"+path+"\n\033[0m");
				// new Thread(()->{
				// 	try{
						Process r = Runtime.getRuntime().exec(path);
						// r.wait();
				// 	}
				// 	catch(Exception e){}
				// }).start();
				if(!flag_all){
					out.println("tt");
					return;
				}
			}
			s1 = s2;
			s2 = s;
		}
	}
}