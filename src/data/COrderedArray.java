package data;

public class COrderedArray<T> extends CArray<T>
{
    //--------------------------------------------------------------------------
    public COrderedArray(int p_nCapacity, CComparison p_oComparison) 
    {
        super(p_nCapacity);
       this.cmp = p_oComparison;
    }
    //--------------------------------------------------------------------------
    public void Add(T p_oItem)
    {
         int nInsertionPos = 0;
         while(nInsertionPos <= this.itemCount -1)
         {  
            // Compare item at current position currentItem > p_sItem
            Object oCurrentItemGeneric = this.items[nInsertionPos];
            T oCurrentItem = (T)oCurrentItemGeneric;
            if (this.cmp.Compare(oCurrentItem, p_oItem) > 0)
                // If there is an item that is higher in alphabetic order we stop the loop to insert here
                break;
            nInsertionPos ++;    
         }
         
         // Instead of writing the same insertion logic again, we re-use it 
         // through inheritance, and the method Insert that was defined in CStringArray
         this.insertItem(nInsertionPos, p_oItem);
    }
    //--------------------------------------------------------------------------
    
    public void Delete(T p_oItem){
        
        this.Delete(FastSearch(p_oItem));
    }
    //--------------------------------------------------------------------------
    // Binary search algorithm
    public int FastSearch(Object p_oSearchValue)
    {   System.out.println(String.format("[>] Searching for {%s}", p_oSearchValue)); 
        int nFoundIndex = -1;
        int nCountSteps = 0;

        int nStartIndex = 0;
        int nEndIndex   = this.itemCount;
        int nMiddleIndex;
        
        // Continue to loop until the is nothing to dichotomize
        while(nEndIndex - nStartIndex >= 0)
        {   nCountSteps++;
            nMiddleIndex = nStartIndex + (nEndIndex - nStartIndex) / 2;  
            int nComparisonResult = this.cmp.Compare(this.items[nMiddleIndex], p_oSearchValue);
            
            System.out.println(String.format("|_ Searching in interval [%d,%d] middle is %d, comparison:%d", 
                            nStartIndex, nEndIndex, nMiddleIndex, nComparisonResult));
            
            if (nComparisonResult == 0) //equals
            {
                nFoundIndex = nMiddleIndex;
                break;                
            }
            else if (nEndIndex - nStartIndex == 0)
            {
                // We reach this case when a single item is searched and then comparison indicates "not equal" -> not found
                nFoundIndex = -1;  
                break;
            }
            else if (nComparisonResult > 0) 
            {   System.out.println("    Search interval end moved just before middle index.");
                nEndIndex = nMiddleIndex - 1;  // In the next step it will check the middle of the left split
            }
            else if (nComparisonResult < 0) 
            {   System.out.println("    Search interval start moved just after middle index.");
                nStartIndex = nMiddleIndex + 1; // In the next step it will check the middle of the right split   
            }
        }
        System.out.println("Search completed in " + nCountSteps + " steps");
        return nFoundIndex;
    }    
    //--------------------------------------------------------------------------    
}
