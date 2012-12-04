package proj.android2.androidDB;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ContactAdapter extends ArrayAdapter<Contact>{

    Context context;
    int layoutResourceId;   
    Contact data[] = null;
   
    public ContactAdapter(Context context, int layoutResourceId, Contact[] phonebookdata) {
        super(context, layoutResourceId, phonebookdata);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = phonebookdata;
    }

	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ContactHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new ContactHolder();
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.number = (TextView)row.findViewById(R.id.number);
            row.setTag(holder);
        }
        else
        {
            holder = (ContactHolder)row.getTag();
        }
       
        Contact contact = data[position];
        holder.name.setText(contact.contact_name);
        holder.number.setText(contact.contact_number);
       
        return row;
    }
   
    static class ContactHolder
    {
        TextView name, number;
    }
  }
