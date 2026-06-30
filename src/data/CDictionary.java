package data;

import javax.swing.DefaultListModel;

public class CDictionary<K,V> extends CArray<CKeyValueEntry>
{

    private boolean isOrdered = true;
    
    //--------------------------------------------------------------------------
    // [Value] property (indexed by key)
    public V getValue(K p_sKey) 
    {
        // We find the key-value dictionary entry from the given key
        CKeyValueEntry oFoundEntry = this.FindEntry(p_sKey);
        if (oFoundEntry != null)
            return (V)oFoundEntry.Value;
        else
            return null;
    }
    // ................................................
    public void setValue(K p_sKey, V p_oValue) 
    {
        // We ensure the existence of the key-value dictionary entry for the given key
        CKeyValueEntry oEntry = this.EnsureEntry(p_sKey);
        oEntry.Value = p_oValue;
    }
    //--------------------------------------------------------------------------
    
    //--------------------------------------------------------------------------
    // [Entry] read-only property (index by position in array)
    public CKeyValueEntry getEntry(int p_nIndex)
    {
        return (CKeyValueEntry) this.items[p_nIndex];
    }
    //--------------------------------------------------------------------------

    
    
    
    //--------------------------------------------------------------------------
    //POLYMORPHISM: Overloaded constructor
    public CDictionary(CComparison p_oComparison) 
    {
        super(1024);
        this.cmp = p_oComparison;
        this.isOrdered = false;
    }
    //--------------------------------------------------------------------------
    //POLYMORPHISM: Overloaded constructor
    public CDictionary(CComparison p_oComparison, boolean p_bIsOrdered) 
    {
        super(1024);
        this.cmp = p_oComparison;
        this.isOrdered = p_bIsOrdered;
    }    
    //--------------------------------------------------------------------------
    //POLYMORPHISM: Overloaded constructor
    public CDictionary(CComparison p_oComparison, boolean p_bIsOrdered, int p_nPageSize) 
    {
        super(p_nPageSize);
        this.cmp = p_oComparison;
        this.isOrdered = p_bIsOrdered;
    }
    //--------------------------------------------------------------------------
    // Finds the key-value pair (dictionary entry) given its key depending on the setting isOrdered:
    //    For ordered keys, it uses binary search, with O(logN) cost.
    //    For non-ordered keys, it uses exhaustive search, with O(N) cost.
    public CKeyValueEntry FindEntry(K p_sKey) 
    {
        if (this.itemCount == 0)
            return null;
        else if (this.isOrdered)
        {
            System.out.println("===== Starting binary search for key [" + p_sKey + "] ======");
            return recurseBinarySearch(p_sKey, 0, this.itemCount - 1, 1);
        }
        else
            return exhaustiveSearch(p_sKey);
    }
    //--------------------------------------------------------------------------
    // Return the index of the key inside the dictionary. 
    public int IndexOf(K p_sKey)  
    {
        CKeyValueEntry oEntry = this.FindEntry(p_sKey);
        return oEntry.Index;
    }       
    //--------------------------------------------------------------------------
    // Add the new key-value pair (dictionary entry) depending on the setting isOrdered:
    //    For ordered keys, it insert is at the proper index, with O(N) cost.
    //    For non-ordered keys, it appends it at the end, with O(1) cost.
    public void AddEntry(CKeyValueEntry p_oEntry)
    {
        if (this.isOrdered)
        {
            CKeyValueEntry oCurrentEntry = null;
            int nInsertionPos = 0;
            while(nInsertionPos <= this.itemCount -1)
            {
               oCurrentEntry = this.getEntry(nInsertionPos);
               // Compare the key of the current entry with the key of the given one
               // If it is higher in order we stop the loop to insert here
               if (this.cmp.Compare(oCurrentEntry.Key, p_oEntry.Key) > 0)
                   break;
               nInsertionPos ++;    
            }

            p_oEntry.Index = nInsertionPos;
            this.insertItem(nInsertionPos, p_oEntry);            
        }
        else
        {
            p_oEntry.Index = this.itemCount; 
            // Append to the unordered array
            this.appendItem(p_oEntry);   
        }
    }
    //--------------------------------------------------------------------------
    // Ensures the existence of the dictionary entry for the given key and returns it.
    public CKeyValueEntry EnsureEntry(K p_sKey) 
    {
        // Check if there is an existing entry
        CKeyValueEntry oEntry = this.FindEntry(p_sKey);
        
        // If there is no entry for this key, append a new entry to the array
        if (oEntry == null)
        {
            oEntry = new CKeyValueEntry(p_sKey);
            this.AddEntry(oEntry);
        }
        
        // In any case an entry will be returned to the caller of this method
        return oEntry;
    }
    //--------------------------------------------------------------------------
    // Removes the dictionary entry for the given key.
    public boolean RemoveEntry(K p_sKey)
    {
        boolean bHasDeleted = false;
        
        int nFoundIndex = this.IndexOf(p_sKey);
        if (nFoundIndex != -1)
        {    
            this.Delete(nFoundIndex);
            bHasDeleted =true;
        }

        return bHasDeleted;
    }
    //--------------------------------------------------------------------------
    // Simple exhaustive search to find the dictionary entry for a given key
    public CKeyValueEntry exhaustiveSearch(K p_sKey) 
    {
        CKeyValueEntry oFoundEntry = null;
        CKeyValueEntry oEntry;
        for(int i = 0; i < this.itemCount; i++)
        {
            oEntry = this.getEntry(i);
            if(this.cmp.Equals(oEntry.Key, p_sKey))
            {   oFoundEntry = oEntry;
                break;
            }
        }
        return oFoundEntry;
    }
    //--------------------------------------------------------------------------
    // Binary search implementation with recursion. This method calls itself
    public CKeyValueEntry recurseBinarySearch(K p_sKey, 
        int p_nStartIndex, int p_nEndIndex, int p_nAlgorithmStep) 
    {        
        CKeyValueEntry oResult = null;

        int nMiddleIndex = p_nStartIndex + (p_nEndIndex - p_nStartIndex) / 2;  
        CKeyValueEntry oMiddleItem = this.getEntry(nMiddleIndex); 

        System.out.println("Search step " + p_nAlgorithmStep 
                    + " in interval [" + p_nStartIndex + "," + p_nEndIndex + "]"
                    +" middle index " + nMiddleIndex + " key is [" + oMiddleItem.Key + "]");
        
        int nComparisonResult = this.cmp.Compare(oMiddleItem.Key, p_sKey);
        if (nComparisonResult == 0)
        {   oResult = oMiddleItem;  // found -> break the recursion 
            oResult.Index = nMiddleIndex;   // Updates the result with the current index in the ordered array (has been changed since addition)
        }
        else if (nComparisonResult > 0)
        {   // Recurse to search the part that contains lower in order items
            // new search inteval will be [start, middle).
            if ((nMiddleIndex - 1) >= p_nStartIndex)            
                oResult = recurseBinarySearch(p_sKey, p_nStartIndex, nMiddleIndex - 1
                                                , p_nAlgorithmStep + 1);            
            else
                // Invalid interval -> not found -> break the recurison
                oResult = null;            
        }
        else if (nComparisonResult < 0)
        {   // Recurse to search the part that contains higher in order items
            // new search inteval will be (middle, end].
            if ((nMiddleIndex + 1) <= p_nEndIndex)
                oResult = recurseBinarySearch(p_sKey, nMiddleIndex + 1, p_nEndIndex
                                                , p_nAlgorithmStep + 1); 
            else
                // Invalid interval -> not found -> break the recurison
                oResult = null;
        }
        
        return oResult;
    }
    //--------------------------------------------------------------------------
    public void GetKeysInto(DefaultListModel<String> p_oDestArray)
    {
        p_oDestArray.clear();
        for(int i = 0; i < this.itemCount; i++)
        {
            CKeyValueEntry oKeyValueEntry = (CKeyValueEntry)this.items[i];
            String sKey = (String)oKeyValueEntry.Key;
            p_oDestArray.add(i, sKey);
        }
    }
    
    //Overloaded to insert Keys into multiple data Structures at my command
    public void GetKeysInto(COrderedArray<String> p_oDestArray)
    {
        p_oDestArray.Clear();
        for(int i = 0; i < this.itemCount; i++)
        {
            CKeyValueEntry oKeyValueEntry = (CKeyValueEntry)this.items[i];
            String sKey = (String)oKeyValueEntry.Key;
            p_oDestArray.Add(sKey);
        }
    }
    //--------------------------------------------------------------------------
    // This method will help us display the data structure in the UI
    @Override
    public String toString()
    {
        String sResult = "{\r\n    ";
        for(int i=0; i < this.itemCount; i++)
        {
            if(i>0)
            {
                sResult += ",\r\n    ";
            }
            CKeyValueEntry oEntry = this.getEntry(i);
            
            sResult += "\"" + oEntry.Key.toString() + "\" :" + "\"" + oEntry.Value.toString() + "\"";
        }
        sResult += "\r\n}";
        return sResult;
    }    
    //--------------------------------------------------------------------------
    
    
    

    
}
