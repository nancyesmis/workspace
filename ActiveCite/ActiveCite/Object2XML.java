import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Object2XML {
/**
* ���������XML�ļ�
*
* @param obj
*          ������Ķ���
* @param outFileName
*          Ŀ��XML�ļ����ļ���
* @return �������XML�ļ���·��
* @throws FileNotFoundException
*/
public static String object2XML(Object obj, String outFileName) throws FileNotFoundException {
// �������XML�ļ����ֽ������
File outFile = new File(outFileName);
BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile));
// ����һ��XML������
XMLEncoder xmlEncoder = new XMLEncoder(bos);
// ʹ��XML������д����
xmlEncoder.writeObject(obj);
// �رձ�����
xmlEncoder.close();

return outFile.getAbsolutePath();
}

/**
* ��XML�ļ�����ɶ���
*
* @param inFileName
*          �����XML�ļ�
* @return �������ɵĶ���
* @throws FileNotFoundException
*/
public static Object xml2Object(String inFileName) throws FileNotFoundException {
// ���������XML�ļ����ֽ�������
BufferedInputStream bis = new BufferedInputStream(new FileInputStream(inFileName));
// ����һ��XML������
XMLDecoder xmlDecoder = new XMLDecoder(bis);
// ʹ��XML������������
Object obj = xmlDecoder.readObject();
// �رս�����
xmlDecoder.close();

return obj;
}

public static void main(String[] args) throws IOException {
// ����һ��StudentBean����,ʵ�ʿ����е�ֵ�����ݿ��ѯ������
User user = new User();
//user.setAddr("NUS");


// ��StudentBean����д��XML�ļ�,xml�ļ�·����������ָ��
String fileName = "User.xml";
Object2XML.object2XML(user, fileName);
// ��XML�ļ���StudentBean����
User newUser = (User) Object2XML.xml2Object(fileName);
// ��������Ķ���
System.out.println(newUser.toString());
}
}