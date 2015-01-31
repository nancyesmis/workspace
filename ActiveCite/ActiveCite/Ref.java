import java.net.URL;

import javax.xml.rpc.ServiceFactory;
import javax.xml.rpc.Service;
import javax.xml.rpc.Call;
import javax.xml.rpc.ParameterMode;

import javax.xml.namespace.QName;

public class Ref
{
	public static void main(String[] args) throws Exception
	{
		System.setProperty("javax.xml.soap.MessageFactory","weblogic.webservice.core.soap.MessageFactoryImpl");
		System.setProperty("javax.xml.rpc.ServiceFactory","weblogic.webservice.core.rpc.ServiceFactoryImpl");
		ServiceFactory factory = ServiceFactory.newInstance();
		String targetNamespace = "urn:ForeCite";
		QName serviceName = new QName(targetNamespace,"ForeCite");
		QName portName = new QName("urn:ForeCite","ForeCitePort");
		QName operationName = new QName("urn:ForeCite","getRef");
		URL wsdlLoc = new  URL("http://aye.comp.nus.edu.sg/~forecite/test.wsdl");
		Service service = factory.createService(wsdlLoc,serviceName);
		Call	call = service.createCall(portName,operationName);
		
		//String result = (String)call.invoke(new Object[] {args[0]});
		String url = "http://www.comp.nus.edu.sg/~yangxin/inputdata/in.txt";
		String result = (String)call.invoke(new Object[] {url});
		System.out.println(result);
	}
}
