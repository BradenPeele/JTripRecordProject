// used to stop unwanted editing of TableModel in Dialog

interface DataManager
{
    void addRecord(TripRecord record);
    
    void editRecord(TripRecord record, int index);
}
