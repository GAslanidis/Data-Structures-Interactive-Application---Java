package data;

// Instead of a specific data type (e.g. String) we declare the class with 
// the generic type placeholder T. Because we don't know the data type 
// of the items in a generic class, we use a Java array of Object.

// [OBJECT ORIENTED]: Generics. Class and methods.
public class CArray<T>
{
    public static String ITEM_SEPARATOR = "\r\n";
    public static boolean SHOW_INDEXES = true;
    
    //--------------------------------------------------------------------------
    protected int itemCount = 0;
    // ................................................
    public int getItemCount()  {
        return this.itemCount;
    }
    //--------------------------------------------------------------------------
    // For a generic data structure the items are of the most basic type (here: Object)
    protected Object[] items = null;
    // ................................................
    public T getItem(int index) {
        // We cast the generic object to a specific type T that is given with the generic class
        return (T)this.items[index];
    }
    // ................................................
    public void setItem(int index, T items) {
        this.items[index] = items;
    }
    //--------------------------------------------------------------------------
    
    
    //--------------------------------------------------------------------------
    private int pages;
    private int pageSize;
    private int capacity; 
    // ................................................
    public int getCapacity() 
    {
        return capacity;
    }    
    //--------------------------------------------------------------------------
    
    // This field holds an object that will help use with comparison.
    // It is initialized with a dummy implementation of the comparison.
    protected CComparison cmp = new CComparison(); 
    
    //--------------------------------------------------------------------------
    public CArray(int p_nPageSize) 
    {
        this.pageSize = p_nPageSize;
        
        // We use expand to create the initial array of items
        this.pages    = 1;
        this.expand();
        System.out.println("Created array with capacity to hold " + capacity + " items");
    }
    //--------------------------------------------------------------------------
    private void expand()
    {
        // Increase the pages (thus the capacity) and create a new array object
        this.pages ++;
        this.capacity = pages * pageSize;
        Object[] newArray = new Object[this.capacity];
        
        // Copy all the items of an existing array object to the new array object. Costs O(n)
        if (this.items != null)        
            System.arraycopy(this.items, 0, newArray, 0, this.items.length);
        
        // The reference is replaced to point to the new array object, the old array object is flagged as garbage.
        this.items = newArray;
        
        System.out.println("Expanding array capacity to hold " + this.capacity + " items");
    }
    //--------------------------------------------------------------------------
    public void Clear()
    {
        // Reset the item count to zero, the page count to 0, nullify the array object reference and expand
        this.itemCount = 0;
        this.pages     = 0;
        this.items = null;
        
        expand();
        
        System.out.println("Emptied the array. The new capacity can hold " + capacity + " items");
    }    
    //--------------------------------------------------------------------------
    protected void appendItem(T p_oItem)
    {
        if (this.itemCount >= this.capacity)
            expand();          
        
        this.items[itemCount] = p_oItem;
        this.itemCount++;        
    }
    //--------------------------------------------------------------------------
    private void moveItemsForInsert(int p_nInsertPosition)
    {
        for(int i = this.itemCount - 1; i >= p_nInsertPosition; i--)
            this.items[i+1] = this.items[i];

        this.items[p_nInsertPosition] = null;
    }    
    //--------------------------------------------------------------------------
    protected void insertItem(int p_nIndex, T p_oItem)
    {
        if (itemCount >= capacity)
            expand();          

        // Move items to insert at p_nIndex with O(n) cost
        this.moveItemsForInsert(p_nIndex);
        items[p_nIndex] = p_oItem;
        this.itemCount++;        
    }
    //--------------------------------------------------------------------------
    protected void deleteLastItem()
    {
        this.items[itemCount - 1] = null;
        this.itemCount--;
    }
    //--------------------------------------------------------------------------
    protected void deleteFirstItem()
    {
        this.Delete(0);
    }
    //--------------------------------------------------------------------------            
    public void DeleteLastItem()
    {
        this.deleteLastItem();
    }
    //--------------------------------------------------------------------------
    public void Delete(int p_nIndex)  
    {
        if (p_nIndex == (this.itemCount - 1))
            // For the last item runs a simple logic with O(1) cost
            this.deleteLastItem();
        else if((p_nIndex >= 0) && (p_nIndex < this.itemCount))
        {
            // For any item runs a for loop with O(n) cost
            for(int i = p_nIndex; i < (this.itemCount - 1); i++)
                this.items[i] = this.items[i+1];            
            this.items[itemCount - 1] = null;
            this.itemCount--;
        }
    }
    //--------------------------------------------------------------------------
    // Exhaustive search algorithm
    public int Search(T p_oSearchValue)
    {
        int nFoundIndex = -1;
        for(int i = 0; i < this.itemCount; i++)
        {
            if (this.cmp.Equals(this.items[i], p_oSearchValue))
            {
                nFoundIndex = i;
                break;
            }
        }
        return nFoundIndex;
    }
    //--------------------------------------------------------------------------
    // This method will help us display the data structure in the UI
    @Override
    public String toString()
    {
        String sResult = "";
        for(int i=0; i < this.itemCount; i++)
        {
            if(i>0)
            {
                sResult += ITEM_SEPARATOR;
            }
            
            if (SHOW_INDEXES)
                sResult += Integer.toString(i) + ": " + this.items[i];
            else
                sResult += this.items[i];
        }
        return sResult;
    }
    //--------------------------------------------------------------------------
}
