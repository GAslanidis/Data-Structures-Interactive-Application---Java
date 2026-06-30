package data;


public class CSort<T>
{
    //--------------------------------------------------------------------------
    public void BubbleSort(CArray<T> p_oArray)
    {   // Iterate on items, starting from the second to the last item. These are the passes of the algorithm.
        for(int nPassNumber = 1; nPassNumber < p_oArray.itemCount; nPassNumber++)
        {
            // The upward inner for loop runs from the start and for the count of items minus the current index.
            Boolean bHasSwapped = false;
            for(int j = 0; j < (p_oArray.itemCount - nPassNumber); j++)
            {
                T sCurrentItem = (T)p_oArray.getItem(j);
                T sNextItem    = (T)p_oArray.getItem(j+1);
                
                // Casting the object of class type T to an interface, is like
                // viewing the object as only having the methods of that interface.
                // We can create any custom class type T here, but our type should implement Comparable<T>
                Comparable<T> iCurrent  = (Comparable<T>)sCurrentItem;
                
                // Comparing items in order
                if (iCurrent.compareTo(sNextItem) > 0)
                {   System.out.print(String.format("Pass #%d: Swapping %s with %s (j=%d)",
                            nPassNumber, sCurrentItem, sNextItem, j));
                    // Swapping item with its next
                    p_oArray.items[j]   = sNextItem;
                    p_oArray.items[j+1] = sCurrentItem;
                    bHasSwapped = true;
                }
            }
            if (!bHasSwapped)
                break;            
        }
    };     
    
}
