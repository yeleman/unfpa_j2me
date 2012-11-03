
package unfpa;

import javax.microedition.rms.*;
import java.io.*;
import java.util.Vector;
import unfpa.Constants.*;

/*
 * Usage:
 *
 * import unfpa.SMSStore.*;
 * import unfpa.StoredSMS.*;
 *
 * SMSStore store = new SMSStore();
 * store.add("Aminata Diallo", "fnuap dpw 23 animanata_diallo 4 5");
 * StoredSMS[] all_sms = store.getAll();
 * StoredSMS sms = all_sms[i];
 * System.out.println(i + ": " + sms.name + " -- " + sms.sms);
 * 
 * store.delete(2);
 * store.get(1);
 */

/*
 * Stores Name/SMS pairs into a delimited string.
 * Used for storing SMS for latter sending.
 */
public class SMSStore {

    // index 0 is invalid
    private int sms_index = 1;
    private int name_index = 2;
    private static final String SEPARATOR = "|-|";

    private static final String database = "storedsms";
    private RecordStore recordstore = null;

    public SMSStore() {

        RecordEnumeration recordEnumeration = null;
        try {
            recordstore = RecordStore.openRecordStore(SMSStore.database, true);
            recordEnumeration = recordstore.enumerateRecords(null, null, false);
            if (recordEnumeration.numRecords() < 1) {
                //recordstore.addRecord(null, 0, 0);
            }
            recordstore.closeRecordStore();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Number of records.
     */
    public int count() {
        StoredSMS[] all_sms = this.getAll();
        return all_sms.length;
    }

    /*
     * Returns an <code>array</code> of all StoredSMS
     */
    public StoredSMS[] getAll() {

        StoredSMS[] all_sms = new StoredSMS[0];

        RecordEnumeration recordEnumeration = null;
        try {
            recordstore = RecordStore.openRecordStore(SMSStore.database, true);
            recordEnumeration = recordstore.enumerateRecords(null, null, false);
            int num_recors = recordEnumeration.numRecords();
            all_sms = new StoredSMS[num_recors];
            String stored_value = "";
            int index = -2;
            int i = 0;
            if (num_recors > 0) {
                while( recordEnumeration.hasNextElement() ) {

                    // record is internally a byte array
                    byte[] byteInputData = new byte[1024];
                    index = recordEnumeration.nextRecordId();

                    StoredSMS record = this.get(index);
                    record.storage_index = index;
                    all_sms[i] = record;
                    i++;

                }
            } else {
                System.out.println("no records");
            }
            recordstore.closeRecordStore();
        } catch (Exception ex) {
            System.out.println(ex);
            return all_sms;
        }
        return all_sms;
    }

    public boolean isValidRecord(int id) {

        try {
            recordstore = RecordStore.openRecordStore(SMSStore.database, true);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        try {
            recordstore.getRecordSize(id);
            try { recordstore.closeRecordStore(); } catch (Exception ex) {}
            return true;
        } catch (RecordStoreNotOpenException e1) {
            e1.printStackTrace();
            try { recordstore.closeRecordStore(); } catch (Exception ex) {}
            return false;
        } catch (InvalidRecordIDException e1) {
            //e1.printStackTrace(); //this printStackTrace is hidden on purpose: the error is in fact used to find out if the record id is valid or not.
            try { recordstore.closeRecordStore(); } catch (Exception ex) {}
            return false;
        } catch (RecordStoreException e1) {
            e1.printStackTrace();
            try { recordstore.closeRecordStore(); } catch (Exception ex) {}
            return false;
        }
        catch (Exception ex2) {
            try { recordstore.closeRecordStore(); } catch (Exception ex) {}
            return false;
        }
    }


    /*
     * Delete one record from its index.
     */
    public boolean delete(int index) {

        if (index < 0) {
            return false;
        }

        try {
            // open record store
            recordstore = RecordStore.openRecordStore(SMSStore.database, true );

            recordstore.deleteRecord( index );

            // close connection
            recordstore.closeRecordStore();
        }
        catch (Exception error)
        {
          return false;
        }

        return true;
    }

    /*
     * Converts a stored string into a StoredSMS
     */
    private StoredSMS valueToSMS(String stored_value) {

        StoredSMS ssms = new StoredSMS();
        try {
            String [] tmp = this.split(stored_value, this.SEPARATOR);
            ssms.name = tmp[0];
            ssms.sms = tmp[1];
        } catch (Exception error) {

        }
        return ssms;
    }

    /*
     * Retrieve one record from its index.
     */
    public StoredSMS get(int index) {

        String stored_value = "";
        StoredSMS blank_sms = new StoredSMS();

        if (index < 0) {
            return blank_sms;
        }
        
        try
        {
        // open record store
        recordstore = RecordStore.openRecordStore(SMSStore.database, true );

        // record is internally a byte array
        byte[] byteInputData = new byte[1024];

        // we'll retrieve data in a stream
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteInputData);
        DataInputStream inputDataStream = new DataInputStream(inputStream);

        // actually retrieve data
        recordstore.getRecord(index, byteInputData, 0);

        stored_value = inputDataStream.readUTF();

        // close streams
        inputStream.reset();
        inputStream.close();
        inputDataStream.close();

        // close connection
        recordstore.closeRecordStore();
        }
        catch (Exception error)
        {
          return blank_sms;
        }

        return this.valueToSMS(stored_value);
    }

    /*
     * Add a StoredSMS to the record.
     */
    public boolean add(StoredSMS sms) {

        String stored_value = sms.name + this.SEPARATOR + sms.sms;

        try
        {
        // open record store
        recordstore = RecordStore.openRecordStore(SMSStore.database, true );

        // record is internaly a byte array
        byte[] outputRecord;

        // store all data in a stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream outputDataStream = new DataOutputStream(outputStream);

        // add all fields to the stream
        outputDataStream.writeUTF(stored_value);

        // finish preparing stream
        outputDataStream.flush();
        outputRecord = outputStream.toByteArray();

        int index = recordstore.addRecord(outputRecord, 0, outputRecord.length);

        // close stream
        outputStream.reset();
        outputStream.close();
        outputDataStream.close();

        // close connection
        recordstore.closeRecordStore();
      }
      catch ( Exception error)
      {
          return false;
      }
      return true;
    }

    /*
     * Add a name/sms strings couple into the record
     */
    public boolean add(String name, String sms) {
        StoredSMS ssms = new StoredSMS();
        ssms.name = name;
        ssms.sms = sms;
        return this.add(ssms);
    }

    /*
     * String split function.
     */
    private String[] split(String original, String separator) {
        Vector nodes = new Vector();
        
        // Parse nodes into vector
        int index = original.indexOf(separator);
        while(index >= 0) {
            nodes.addElement( original.substring(0, index) );
            original = original.substring(index + separator.length());
            index = original.indexOf(separator);
        }

        // Get the last node
        nodes.addElement( original );

        // Create splitted string array
        String[] result = new String[ nodes.size() ];
        if( nodes.size()>0 ) {
            for(int loop=0; loop<nodes.size(); loop++)
            {
                result[loop] = (String)nodes.elementAt(loop);
            }
        }

    return result;
    }

}