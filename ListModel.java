import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;


class ListModel extends DefaultListModel<TripRecord>
{
    DataInputStream dis;
    DataOutputStream dos;
    
    //=================================
    
    public void addRecord(TripRecord record)
    {
        add(0, record);
    }
    
    //=================================
    
    public void editRecord(TripRecord record, int index)
    {
        remove(index);
        add(0, record);
    }
    
    //=================================
    
    void delete(int index)
    {
        remove(index);
    }
    
    //=================================
    
    // gets file from actionPerformed in TripRecordFrame and creates a DataInputStream (dis)
    // calls 2nd constructor for Triprecord (TripRecord(dis)) repeatedly to add TripRecords to the list
    void load(File file)
    {
        try
        {
            dis = new DataInputStream(new FileInputStream(file));
            int numRecords = dis.readInt();
                
            for(int n = 0; n < numRecords; n++)
                addElement(new TripRecord(dis));
            
            dis.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "IO exception", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //=================================
    
    // gets file from actionPerformed in TripRecordFrame and creates a DataOutputStream (dos)
    // calls store method of TripRecord (TripRecord(dis)) repeatedly to store TripRecords in the file
    void save(File file)
    {
        try
        {
            dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeInt(size()); // storing number of bytes at beginning of file
                    
            for(int n = 0; n < size(); n++)
                elementAt(n).store(dos);
            
            dos.close();
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "IO exception", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    //=================================
    // not currently used
    
    void debugDisplay()
    {
        for(int n = 0; n < size(); n++)
        {
            elementAt(n).displayRecord();
        }
    }
}
