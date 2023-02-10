import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.text.*;
import java.util.*;

class TripRecord
{
    static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
    static Random random = new Random();
    
    Date date;
    String name;
    String serviceCode;
    int initialMileage;
    int mileageOnReturn;
    double billingRate;
    String comments;
    
    TripRecord()
    {
    }
    
    //=================================
    //=================================
    
    // methods to return data to TableModel
    
    static int getSize()
    {
        return 7;
    }
    
    String getDate()
    {
        return dateFormat.format(date);
    }
    
    String getName()
    {
        return name;
    }
    
    String getServiceCode()
    {
        return serviceCode;
    }
    
    int getInitialMileage()
    {
        return initialMileage;
    }
    
    int getMileageOnReturn()
    {
        return mileageOnReturn;
    }
    
    double getBillingRate()
    {
        return billingRate;
    }
    
    String getComments()
    {
        return comments;
    }
    
    
    //=================================
    //=================================
    
    // loading from file
    TripRecord(DataInputStream dis) throws IOException
    {
        date = new Date(dis.readLong());
        name = dis.readUTF();
        serviceCode = dis.readUTF();
        initialMileage = dis.readInt();
        mileageOnReturn = dis.readInt();
        billingRate = dis.readDouble();
        comments = dis.readUTF();
    }
    
    //=================================
    
    // save to file
    void store(DataOutputStream dos) throws IOException
    {
        dos.writeLong(date.getTime());
        dos.writeUTF(name);
        dos.writeUTF(serviceCode);
        dos.writeInt(initialMileage);
        dos.writeInt(mileageOnReturn);
        dos.writeDouble(billingRate);
        dos.writeUTF(comments);
    }
    
    //=================================
    
    // returns instance of Triprecord with random values
    static TripRecord getRandom()
    {
        String[] nameList = {"George Washington", "Thomas Jefferson", "John Adams", "Benjamin Franklin"};
        String[] serviceCodeList = {"A0428", "A0429", "A0427", "A0434"};
        String[] commentList = {"comment1", "comment2", "comment3", "comment4"};
     
        TripRecord record = new TripRecord();
        
        long longTime = -946771200000L + random.nextLong() % (70L * 365 * 24 * 60 * 60 * 1000);
        
        record.date = new Date(longTime);
        record.name = nameList[random.nextInt(4)];
        record.serviceCode = serviceCodeList[random.nextInt(4)];
        record.mileageOnReturn = random.nextInt(500000);
        record.initialMileage = random.nextInt(record.mileageOnReturn);
        record.billingRate = random.nextInt(5000);
        record.comments = commentList[random.nextInt(4)];
        
        return record;
    }
    
    //=================================
    // not currently used
    
    void displayRecord()
    {
        System.out.printf("%s %s %s %d %d %f %s \n", dateFormat.format(date), name, serviceCode, mileageOnReturn, initialMileage, billingRate, comments);
    }
    
    //=================================
    
    @Override
    public String toString()
    {
        return String.format("%s %s %s %d %d %.2f %s \n", dateFormat.format(date), name, serviceCode, mileageOnReturn, initialMileage, billingRate, comments);
    }
}
