package data;

public class CPythonLine 
{
    private static int identationSpaces = 4;
            
    public int IdentationLevel;
    public String Text;
    public String[] Tokens;
    
    //--------------------------------------------------------------------------
    public CPythonLine(String p_sText)
    {
        int nLen  = p_sText.length();
        this.Text = p_sText.replaceAll("^\\s+", "");
        int nTrimmedLen =  this.Text.length();
        
        this.IdentationLevel = 1 +((nLen - nTrimmedLen) / identationSpaces);
        
        //TODO: Tokenize the source code line.
        Tokens = this.Text.split(" ");
    }
    //--------------------------------------------------------------------------

    //TODO: Create a toString() method that returns the indentation level and first token of the source code line.
    @Override
    public String toString(){
        
        return this.IdentationLevel +" "+ Tokens[0];
    }
    
}
