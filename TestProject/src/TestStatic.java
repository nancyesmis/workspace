import java.io.*;
import java.text.DateFormat;
import java.util.*;
public class TestStatic
{
	public static DateFormat df = DateFormat.getDateTimeInstance();
	public static void main(String[] args)
	{
		Date d = new Date();
		System.out.println(formatString(d));
	}
	public static String formatString(Date s)
	{
		return df.format(s);
	}
}