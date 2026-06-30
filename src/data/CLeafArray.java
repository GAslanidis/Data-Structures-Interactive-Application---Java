package data;

import java.io.File;

/**
 *
 * @author georgiosaslanidis
 */
public class CLeafArray extends CArray<File>
{
    
//------------------------------------------------------------------------------
//Constructor
    
    public CLeafArray(CComparison p_oComparison) 
    {
        super(1024);
        this.cmp = p_oComparison;
    }

//------------------------------------------------------------------------------
//Basic Functions
    
    //Append a file
    public void AppendTxtFile(File oFile){
        
        this.appendItem(oFile);
    }
    
    //--------------------------------------------------------------------------
     //Insert file to certain position
    
    public void InsertTxtFile(int p_nIndex, File oFile){
        
        this.insertItem(p_nIndex, oFile);
    }
    
    //--------------------------------------------------------------------------
     //Delete file by name, recurses to find the position
    
    public void DeleteTxtFile(File p_oFile){
        
        this.Delete(recurseBinarySearch(p_oFile,
                0, this.itemCount - 1, 1));
    }
    
    //--------------------------------------------------------------------------
     //Recursion to find the position of a file
    
    public int recurseBinarySearch(File p_oFile, 
        int p_nStartIndex, int p_nEndIndex, int p_nAlgorithmStep) 
    {        
        int oResult = 0;

        int nMiddleIndex = p_nStartIndex + (p_nEndIndex - p_nStartIndex) / 2;  
        File oMiddleItem = this.getItem(nMiddleIndex); 

        int nComparisonResult = this.cmp.Compare(oMiddleItem.getName(), p_oFile.getName());
        if (nComparisonResult == 0)
        {   oResult = nMiddleIndex;  // found -> break the recursion 
        }
        else if (nComparisonResult > 0)
        {   // Recurse to search the part that contains lower in order items
            // new search inteval will be [start, middle).
            if ((nMiddleIndex - 1) >= p_nStartIndex)            
                oResult = recurseBinarySearch(p_oFile, p_nStartIndex, nMiddleIndex - 1
                                                , p_nAlgorithmStep + 1);            
            else
                // Invalid interval -> not found -> break the recurison
                oResult = 0;            
        }
        else if (nComparisonResult < 0)
        {   // Recurse to search the part that contains higher in order items
            // new search inteval will be (middle, end].
            if ((nMiddleIndex + 1) <= p_nEndIndex)
                oResult = recurseBinarySearch(p_oFile, nMiddleIndex + 1, p_nEndIndex
                                                , p_nAlgorithmStep + 1); 
            else
                // Invalid interval -> not found -> break the recurison
                oResult = 0;
        }
        
        return oResult;
    }
    
    //Overloaded method
    public int recurseBinarySearch(String p_sFile, 
        int p_nStartIndex, int p_nEndIndex, int p_nAlgorithmStep) 
    {        
        int oResult = 0;

        int nMiddleIndex = p_nStartIndex + (p_nEndIndex - p_nStartIndex) / 2;  
        File oMiddleItem = this.getItem(nMiddleIndex); 

        int nComparisonResult = this.cmp.Compare(oMiddleItem.getName(), p_sFile);
        if (nComparisonResult == 0)
        {   oResult = nMiddleIndex;  // found -> break the recursion 
        }
        else if (nComparisonResult > 0)
        {   // Recurse to search the part that contains lower in order items
            // new search inteval will be [start, middle).
            if ((nMiddleIndex - 1) >= p_nStartIndex)            
                oResult = recurseBinarySearch(p_sFile, p_nStartIndex, nMiddleIndex - 1
                                                , p_nAlgorithmStep + 1);            
            else
                // Invalid interval -> not found -> break the recurison
                oResult = 0;            
        }
        else if (nComparisonResult < 0)
        {   // Recurse to search the part that contains higher in order items
            // new search inteval will be (middle, end].
            if ((nMiddleIndex + 1) <= p_nEndIndex)
                oResult = recurseBinarySearch(p_sFile, nMiddleIndex + 1, p_nEndIndex
                                                , p_nAlgorithmStep + 1); 
            else
                // Invalid interval -> not found -> break the recurison
                oResult = 0;
        }
        
        return oResult;
    }
    
//------------------------------------------------------------------------------
//MERGE SORT
    
    // Merges two subarrays of arr[].
    // First subarray is arr[l..m]
    // Second subarray is arr[m+1..r]
    private void merge(CLeafArray p_oLeafArray, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        // Create temp arrays
        CLeafArray L = new CLeafArray(new CComparison());
        CLeafArray R = new CLeafArray(new CComparison());
 
        // Copy data to temp arrays
        for (int i = 0; i < n1; ++i)
            L.InsertTxtFile(i, p_oLeafArray.getItem(l + i)); 
        for (int j = 0; j < n2; ++j)
            R.InsertTxtFile(j, p_oLeafArray.getItem(m + 1 + j));
 
        // Merge the temp arrays
 
        // Initial indices of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarray array
        int k = l;
        while (i < n1 && j < n2) {
            if (L.getItem(i).length() <= R.getItem(j).length()) {
                p_oLeafArray.setItem(k,L.getItem(i));
                i++;
            }
            else {
                p_oLeafArray.setItem(k, R.getItem(j));
                j++;
            }
            k++;
        }
 
        // Copy remaining elements of L[] if any
        while (i < n1) {
            p_oLeafArray.setItem(k,L.getItem(i));;
            i++;
            k++;
        }
 
        // Copy remaining elements of R[] if any
        while (j < n2) {
            p_oLeafArray.setItem(k, R.getItem(j));
            j++;
            k++;
        }
    }
    //--------------------------------------------------------------------------
    // Main function that sorts arr[l..r] using
    // merge()
    public void MergeSort(CLeafArray p_oLeafArary, int l, int r)
    {
        if (l < r) {
 
            // Find the middle point
            int m = l + (r - l) / 2;
 
            // Sort first and second halves
            MergeSort(this, l, m);
            MergeSort(this, m + 1, r);
 
            // Merge the sorted halves
            merge(this, l, m, r);
        }
    }
    
//------------------------------------------------------------------------------
//Your casual toString()

    @Override
    public String toString(){

        String sResult = "";

        for(int i=0; i < this.itemCount; i++){

        sResult = sResult + "Name: " + this.getItem(i).getName()
                       + " - Size: " + this.getItem(i).length()
                          + "bytes\n"; 
         }

        return sResult;
    }
}
