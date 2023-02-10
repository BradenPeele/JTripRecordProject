import javax.swing.*;
import java.util.*;
import java.text.*;

class DateVerifier extends InputVerifier
{
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    
    public boolean verify(JComponent input)
    {
        JTextField tf = (JTextField)input;
        String dateStr = tf.getText().trim();
        
        if(dateStr.equals(""))
            return true;

        Date date = new Date();
        ParsePosition pos = new ParsePosition(0);
        dateFormat.setLenient(false);
        date = dateFormat.parse(dateStr, pos);
    
        if(pos.getIndex() == dateStr.length())
            return true;
        
        return false;
    }
}
