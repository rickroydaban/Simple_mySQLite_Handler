package proj.android2.androidDB;

import java.util.List;

import android.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
 
public class DatabaseActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(proj.android2.androidDB.R.layout.main_view);
        final DatabaseHandler db = new DatabaseHandler(this);
        
        // Inserting Contacts into the database
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        
        System.out.println("Inserting..");
        if(cur.getCount() > 0){ //checks if the database is empty
        	while(cur.moveToNext()){
        		String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        		String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        		
        		if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0){
        			
        			Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
        						           ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" = ?",
        						           new String[]{id}, null);
        			
        			while(pCur.moveToNext()){
        				String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            			System.out.println("name: "+name+",ID: "+id+",phone: "+phone);
            				db.addContact(new Contact(name, phone));
        			}
        			pCur.close();
        		}
        	}
        }
         
        // Reading all contacts from the database
        Log.d("Reading: ", "Reading all contacts..");
        List<Contact> dbcontacts = db.getAllContacts();      

        ListView listView1;
        int arraysize = dbcontacts.size();
        
        final Contact[] phonebookdata = new Contact[arraysize];

        int ctr=0;
        for (Contact cn : dbcontacts) {
            String log = "Id: "+cn.getID()+" ,Name: " + cn.getName() + " ,Phone: " + cn.getPhoneNumber();
            phonebookdata[ctr] = new Contact(cn.getName(),cn.getPhoneNumber());

            // Writing Contacts to log for checking purposes
            Log.d("Name: ", log);
            ctr++;
        }

        ContactAdapter adapter = new ContactAdapter(this,proj.android2.androidDB.R.layout.listview_item_row, phonebookdata);
        listView1 = (ListView)findViewById(proj.android2.androidDB.R.id.listView1);
        View header = (View)getLayoutInflater().inflate(proj.android2.androidDB.R.layout.listview_header_row, null);
        listView1.addHeaderView(header);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				final String selected_name = phonebookdata[position-1].contact_name;
				final String selected_number = phonebookdata[position-1].contact_number;
				
				AlertDialog.Builder builder = new Builder(DatabaseActivity.this);
				builder.setTitle("Manage Contact");

				builder.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder changebuilder = new Builder(DatabaseActivity.this);
						changebuilder.setTitle("Choose Field to Edit");

						changebuilder.setPositiveButton("Name", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								AlertDialog.Builder namebuilder = new Builder(DatabaseActivity.this);
								namebuilder.setTitle("Set Contact Name");
								
								final EditText nameinput = new EditText(DatabaseActivity.this);
								namebuilder.setView(nameinput);

								namebuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										db.updateContactName(nameinput.getText().toString(), selected_name);
										Toast.makeText(DatabaseActivity.this, "Changing contact name", Toast.LENGTH_SHORT).show();
										onRestart();
									}
								});
								
								namebuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
								
								namebuilder.create().show();
							}
						});
						
						changebuilder.setNegativeButton("Number", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								AlertDialog.Builder numberbuilder = new Builder(DatabaseActivity.this);
								numberbuilder.setTitle("Set Contact Number");
								
								final EditText numberinput = new EditText(DatabaseActivity.this);
								numberbuilder.setView(numberinput);

								numberbuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										db.updateContactNumber(numberinput.getText().toString(),selected_number);
										Toast.makeText(DatabaseActivity.this, "Changing contact number", Toast.LENGTH_SHORT).show();
										onRestart();
									}
								});
								
								numberbuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
									}
								});
								
								numberbuilder.create().show();
							}
						});
						changebuilder.create().show();	

					}
				});	

				builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						db.deleteContact(selected_name);
						Toast.makeText(DatabaseActivity.this, "Deleted "+selected_name, Toast.LENGTH_SHORT).show();
						onRestart();
					}
				});	

				builder.create().show();	
			}
        });

	}
	
	@Override
	protected void onRestart() {

	    // TODO Auto-generated method stub
	    super.onRestart();
	    Intent i = new Intent(DatabaseActivity.this, DatabaseActivity.class);  //your class
	    startActivity(i);
	    finish();

	}
}