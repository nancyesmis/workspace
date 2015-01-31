import java.util.*;

class KMP
{
    String s; //���ַ���
    String p; //ƥ���ַ���
    int[] next; //ƥ���ַ�����next����
   
    int times; //��¼ƥ��Ĵ���
    int index; //��¼���ҵ���λ��
   
    KMP(String s,String p) //���캯������ʼ��������Ա����
    {
        this.s=s;
        this.p=p;
        this.next=new int[p.length()];
        for(int i=0;i<p.length();i++)
        {
            if(i==0)
            {
                this.next[i]=-1;
            }else if(i==1)
            {
                this.next[i]=0;
            }
            else
            {
                this.next[i]=next(p.substring(0,i)); //��ĳ��λ��֮ǰ���ַ��������俪ʼ���ֺͽ�β�����Ƿ���ͬ
            }
        }
       
        this.times=0;
        this.index=-1;
    }
   
    private int next(String p)  //�������ַ�������ʼ���ֺͽ�β������ͬ������µĿ�ʼ���ֵ���һ��λ�ã���nextֵ
    {
        int length=p.length()/2;
       
        while(length>0)
        {
            if(p.substring(0,length).compareTo(p.substring((p.length()-length),p.length()))==0)
            {
                return length;
            }
            length--;
        }
        return length;
    }
   
//�����ַ������бȽϣ�������ƥ�䣬��ѯnext���飻�����Ԫ�غ͵�ǰ��ƥ��Ԫ����ͬ�������next
    boolean match()
    {
        int i=0;
        int j=0;
        int index=-1; //index��¼ƥ���λ��
       
        while(i<this.s.length()&&j<this.p.length())
        {
            if(this.s.charAt(i)==this.p.charAt(j))
            {
                if(j==0){
                    index=i;
                }
               
                i++;
                j++;
            }
            else
            {
                //һֱѰ��next��֪���͵�ǰԪ�ز���ȵ�Ԫ�أ������±긳ֵ��j
                int newj=this.next[j];
                while((newj!=-1)&&(this.p.charAt(newj)==this.p.charAt(j)))
                {
                    newj=this.next[newj];
                }
                j=newj;
               
                if(j==-1)
                {
                    i++;
                    j=0;
                }
                else
                {
                    index=i-j;
                }
            }
            this.times++;
        }
       
        if(j==this.p.length())
        {
            this.index=index;
            return true;
        }
        else
        {
            return false;
        }
    }
    public static void main(String[] args)
    {
        String s="ababbcacabacsbaaababbcacsababbcacjkababbcacjkjkjkababbcacjkiiooababbcackjkjababbcac";
        String p="ababbcac";
        KMP m=new KMP(s, p);
        
        //��¼����ƥ����ֵ�λ��
        Vector position = new Vector();
       
        //˳���ӡnext���飻
        for(int i=0;i<m.next.length;i++)
        {
            System.out.println(m.next[i]+" ");
        }
       
        while(m.match())
        {
        	int index = m.index;
        	System.out.println("Match index: " + index);
        	s = s.substring(index + p.length());
        	m = new KMP(s, p);
        }
       
    }
}