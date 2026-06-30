package data;

// "Comparator" for Integers
public class CComparisonCases extends CComparison
{
    private GroupByType compareBy;
    
//------------------------------------------------------------------------------
    
    public CComparisonCases(GroupByType p_nCompareBy) 
    {
        this.compareBy = p_nCompareBy;
    }
    
//------------------------------------------------------------------------------
    @Override
    public int Compare(Object p_oItem1, Object p_oItem2) {
    
        int nResult = 0;
        
        switch(this.compareBy) {
            
            case OCCURANCES: {
                
                int nItem1 = ((CToken) p_oItem1).getOccurances();
                int nItem2 = ((CToken) p_oItem2).getOccurances();

                if (nItem1 > nItem2)
                    nResult = 1;
                else if (nItem1 < nItem2)                    
                    nResult = -1;
                else
                    nResult = 0;
                
                break;
            }
            case ALPHABETICAL_TOKENS: {
                
                System.out.println("\n\n\nMpike edw\n\n\n");
                String sItem1 = ((CToken) p_oItem1).getName();
                String sItem2 = ((CToken) p_oItem2).getName();
                
                nResult = sItem1.compareTo(sItem2);
                
                break;
            }
            case COMPARABLE_DICT: {
                
                Comparable<Object> iItem1 = (Comparable<Object>)p_oItem1;
                Object oItem2 = (Object)p_oItem2;
                if (iItem1 != null)
                    return iItem1.compareTo(oItem2);
                else
                    break; // Class T does not implement Comparable<T> 
            }
            default: {
                
                break;
            }
        }
        
        return nResult;
    }
}



