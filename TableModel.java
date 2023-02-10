import javax.swing.table.*;
import javax.swing.*;
import java.io.*;

public class TableModel extends AbstractTableModel implements DataManager
{
    ListModel list;
    static boolean isChanged;
    
    
    TableModel()
    {
        list = new ListModel();
        isChanged = false;
    }
    
    boolean getIsChanged()
    {
        return isChanged;
    }
    
    static void setIsChanged(boolean changed)
    {
        isChanged = changed;
    }
    
    
    //=================================
    //=================================
    
    // DataManager
    public void addRecord(TripRecord record)
    {
        list.addRecord(record);
        fireTableDataChanged();
    }
    
    //DataManager
    public void editRecord(TripRecord record, int index)
    {
        list.editRecord(record, index);
        fireTableDataChanged();
    }
    
    TripRecord getRecord(int index)
    {
        return list.get(index);
    }
    
    void delete(int index)
    {
        list.delete(index);
        fireTableDataChanged();
    }
    
    void load(File file)
    {
        list.load(file);
        fireTableDataChanged();
    }
    
    void save(File file)
    {
        list.save(file);
        fireTableDataChanged();
    }
    
    void clear()
    {
        list.clear();
        fireTableDataChanged();
    }
    
    //=================================
    //=================================

    public int getRowCount()
    {
        return list.getSize();
    }

    
    public int getColumnCount()
    {
        return TripRecord.getSize();
    }

    public Object getValueAt(int row, int col)
    {
        TripRecord record = list.getElementAt(row);
        
        if(col == 0)
            return record.getDate();
        else if(col == 1)
            return record.getName();
        else if(col == 2)
            return record.getServiceCode();
        else if(col == 3)
            return record.getInitialMileage();
        else if(col == 4)
            return record.getMileageOnReturn();
        else if(col == 5)
            return record.getBillingRate();
        else if(col == 6)
            return record.getComments();
        else
        {
            System.out.println("error");
            System.exit(1);
            return null;
        }
    }
    
    public Class getColumnClass(int col)
    {
        if(col == 0)
            return String.class;
        else if(col == 1)
            return String.class;
        else if(col == 2)
            return String.class;
        else if(col == 3)
            return Integer.class;
        else if(col == 4)
            return Integer.class;
        else if(col == 5)
            return Double.class;
        else // if(col == 6)
            return String.class;
    }
}
