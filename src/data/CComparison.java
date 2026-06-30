package data;

// This is the base class in the hierarchy of "comparator" classes
// We need to implement a descendand class to allow comparison for a specific data type
// We use "comparator" objects in generic implementations.

// [OBJECT ORIENTED]: Inheritance
// [OBJECT ORIENTED]: Polymorphism with methods that are overriden (virtual methods).
public class CComparison
{
    //--------------------------------------------------------------------------
    // This should be overriden in a descendand class, to implement the comparison
    // with a more specific data type.
    public int Compare(Object p_oItem1, Object p_oItem2)
    {
        // p_oItem1 == p_oItem2:  return 0;
        // p_oItem1 > p_oItem2:  return 1;
        // p_oItem1 < p_oItem2:  return -1;
        return 0;
    }
    //--------------------------------------------------------------------------
    public boolean Equals(Object p_oItem1, Object p_oItem2)
    {
        return this.Compare(p_oItem1, p_oItem2) == 0;
    }
    //--------------------------------------------------------------------------
}