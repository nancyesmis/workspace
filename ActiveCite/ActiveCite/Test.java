import java.io.*;

class Test {

public static void main(String[] args) {

BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
boolean b = true;
try {
while (b) {
System.out.print("������һ��int���ȵ�������");
String s = (String) br.readLine();
for (int j = 0; j < s.length(); j++) {
if (!(s.charAt(j) >= 48 && s.charAt(j) <= 57)) {
System.out.println("������Ĳ��Ǵ�����!����������.");
b = true;
break;
} else {
b = false;
}
}
if (!b) {
int i = Integer.parseInt(s);
System.out.println("�������������:" + i);
}
}
} catch (Exception e) {
e.printStackTrace();
}

}
} 