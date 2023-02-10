import javax.swing.*;

class DoubleVerifier extends InputVerifier
{
    public boolean verify(JComponent input)
    {
        JTextField tf = (JTextField)input;
        String doubleStr = tf.getText().trim();
        
        if(doubleStr.equals(""))
            return true;
        
        try
        {
            double d = Double.parseDouble(doubleStr);
            double d2 = d * 100;
            int i = (int)d2;
            d2 = i;
            d2 = d2 / 100;
            
            if(d == d2 && d <= 5000) // decimal place check + range check
                return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        
        return false;
    }
}
