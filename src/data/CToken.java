package data;

/**
 *
 * @author georgiosaslanidis
 */
public class CToken implements Comparable<CToken> {
    
    private static int tokensID = 1; //used when adding in ordered array
    private static int inputSequenceID = 1; //used for unordered array
    
    protected int ID = 0;
    
    protected int Occurances = 0;
    protected String Name;
    
    //Used to create and store tokens into ordered array for groupping process
    public CToken(String p_sName) {
        
        this.Name = p_sName;
        this.ID = inputSequenceID;
        
        inputSequenceID++;
    }
    
    //Used to create and store tokens into ordered array
    public CToken(String p_sName, int p_nOccurances) {
        
        this.Name = p_sName;
        this.Occurances = p_nOccurances;
        this.ID = tokensID;
        
        tokensID++;
    }
    
    public int getID() {    //Individual Token ID
        
        return ID;
        
    }
    
    public int getTokenID() {   //Total Count of Tokens
        
        return tokensID;
        
    }
    
    public int getOccurances() {
        return Occurances;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }
    
    public String toString() {
        
        return this.Name;
        
    }
    
    @Override
    public int compareTo(CToken o) {
        if (this.Name.compareTo(o.Name) > 0)
            return 1;
        else if (this.Name.compareTo(o.Name) < 0)                    
            return -1;
        else
            return 0;
    }
    
    
}
