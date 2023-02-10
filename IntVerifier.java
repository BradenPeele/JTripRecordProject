import javax.swing.*;

// check range also

class IntVerifier extends InputVerifier
{
    static final int upperLimit = 500000;
    static final int lowerLimit = 0;
    int num;
    
    public boolean verify(JComponent input)
    {
        JTextField tf = (JTextField)input;
        String intStr = tf.getText().trim();
        
        if(intStr.equals(""))
            return true;
        
        try
        {
            num = Integer.parseInt(intStr);
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        
        if(num <= upperLimit && num >= lowerLimit)
            return true;
        
        return false;
    }
}
