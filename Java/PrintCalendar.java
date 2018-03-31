import java.util.*;	//Scanner��,Date��
import java.time.*;//LocalDate��
import java.io.*;
import java.nio.file.*;	//Paths��
import java.math.*;

class PrintCalendar
{
	public static final String row_left = "\u001b";
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		LocalDate localdate = LocalDate.now();
		int nowday = localdate.getDayOfMonth();
		localdate = localdate.minusDays(nowday-1);
		//��ӡ�ܼ�
		DayOfWeek week = DayOfWeek.MONDAY;
		for(int i=0; i<7; i++)
		{
			System.out.print(String.format(week+"").substring(0, 3)+"\t");
			week = week.plus(1);
		}
		System.out.println();
		//��ӡ����
		for(int i=1; i<localdate.getDayOfWeek().getValue(); i++)
			System.out.print("\t");
		int month = localdate.getMonthValue();
		for(int i=localdate.getDayOfWeek().getValue(); month==localdate.getMonthValue(); i++,localdate = localdate.plusDays(1))
		{
			System.out.print(localdate.getDayOfMonth());
			if(localdate.getDayOfMonth()==nowday)
				System.out.print(row_left);
			System.out.print("\t");
			if(i%7==0)
				System.out.println();
		}
	}
}