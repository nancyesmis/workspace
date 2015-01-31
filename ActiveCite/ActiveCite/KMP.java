import java.util.*;

class KMP
{
    String s; //主字符串
    String p; //匹配字符串
    int[] next; //匹配字符串的next数组
   
    int times; //记录匹配的次数
    int index; //记录查找到的位置
   
    KMP(String s,String p) //构造函数，初始化各个成员变量
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
                this.next[i]=next(p.substring(0,i)); //对某个位置之前的字符串考察其开始部分和结尾部分是否相同
            }
        }
       
        this.times=0;
        this.index=-1;
    }
   
    private int next(String p)  //返回子字符串，开始部分和结尾部分相同的情况下的开始部分的下一个位置，即next值
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
   
//对主字符串进行比较，碰到不匹配，查询next数组；如果新元素和当前不匹配元素相同，则继续next
    boolean match()
    {
        int i=0;
        int j=0;
        int index=-1; //index记录匹配的位置
       
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
                //一直寻找next，知道和当前元素不相等的元素，将其下标赋值给j
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
        
        //记录所有匹配出现的位置
        Vector position = new Vector();
       
        //顺便打印next数组；
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