import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import junit.framework.TestCase;

public class TestPermutations extends TestCase
{
    String[] s1 = new String[] { "a", "b"};
    String[] s2 = new String[] { "d", "e"};
    String[] s3 = new String[] { "c" };           
    
//    public void testTest()
//    {        
//        List list = new ArrayList();
//        for (int i = 0; i < s1.length; i++)
//        {
//            for (int j = 0; j < s2.length; j++)
//            {
//                for (int k = 0; k < s3.length; k++)
//                {
//                    for (int l = 0; l < s4.length; l++)
//                    {
//                        List p = new ArrayList();                
//                        p.add( s1[i] );
//                        p.add( s2[j] );
//                        p.add( s3[k] );
//                        p.add( s4[l] );
//                        list.add(p);
//                    }                                       
//                }                                
//            }
//        }
//        System.out.println( list );
//    }
    
    public void test2()
    {
        String[][] list = new String[][] { s1, s2, s3};
        List results = new ArrayList();
                
        calulatePermutations(0, list, null, results);
        
        System.out.print("[");
        for ( Iterator it = results.iterator(); it.hasNext(); )
        {
            System.out.print("[");
            String[] entry = (String[]) it.next();
            for (int i=0; i < entry.length-1; i++)
            {
                System.out.print(entry[i] + ", ");
            }
            System.out.print(entry[entry.length-1] + "]");
            if ( it.hasNext() )
            {
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }
   
    
    private void calulatePermutations(int currentLevel, String[][] list, String[] resultEntry, List results)
    {
        String[] entry = list[currentLevel];        
        for (int i = 0; i < entry.length; i++)
        {                        
            if (currentLevel < list.length-1)
            {
                resultEntry[currentLevel] = entry[i];
                if  (currentLevel == 0) 
                {
                    resultEntry = new String[ list.length-1];
                }                
                calulatePermutations(currentLevel + 1, list, resultEntry, results );
            }
            else
            {                
                String[] result = new String[ list.length];
                System.arraycopy(resultEntry, 0, result, 0, resultEntry.length);
                result[currentLevel] = entry[i]; 
                results.add(result);
            }
        }
    }
   
}
