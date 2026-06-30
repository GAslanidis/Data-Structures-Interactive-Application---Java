package data;

public class CUnorderedArray<T> extends CArray<T> {
    // Constructor
    public CUnorderedArray(int p_nPageSize) {
        super(p_nPageSize);
    }

    // Append method for adding items
    public void Append(T p_item) {
        this.appendItem(p_item);
    }

    // Insert method for inserting items at a specific index
    public void Insert(int p_nIndex, T p_item) {
        // If outside of the upper array bound
        if (p_nIndex >= this.itemCount) {
            // Simple append to the end with O(1) cost
            this.appendItem(p_item);
        } else {
            this.insertItem(p_nIndex, p_item);
        }
    }
}
