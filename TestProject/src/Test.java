import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

class EvaluatingElections
{
	public int num;
	public int[] topK;
	public static void main(String[] args)
	{
		System.out.println(new EvaluatingElections().evaluate(new int[]{2,2,2,2,2,2,2,2}));

	}
	public int evaluate(int[] districts)
	{
		boolean isEven = districts.length % 2 == 0;
		boolean existsEven = false;
		num = districts.length / 2;		
		topK = new int[num];
		if(num ==0)
			return districts[0]/2 + 1;
		int total = 0;
		for(int i=0;i<num;i++)
		{
			total += districts[i];
			topK[i] = districts[i];
		}
		for(int i=num;i<districts.length;i++)
		{
			int min = findMin();
			if(topK[min] < districts[i])
			{
				total = total + districts[i] - topK[min] + (topK[min] % 2 == 0 ? topK[min] / 2 - 1 : topK[min] /2);
				if (topK[min]%2==0)
					existsEven = true;
				topK[min] = districts[i];
			}
			else
			{
				if (districts[i] % 2 == 0 )
					existsEven = true;
				total += (districts[i] % 2 == 0? districts[i] /2 -1:districts[i] /2);
			}
		}
		if(isEven)
			total += 1;
		else
		{
			if(existsEven)
			{
				total+=2;
			}
			else
			{
				total +=1;
			}
		}
		return total;
	}	
	int findMin()
	{
		int min = 0;
		for(int i=1;i<num;i++)
		{
			if(topK[i] < topK[min])
				min = i;
		}
		return min;
	}
}
