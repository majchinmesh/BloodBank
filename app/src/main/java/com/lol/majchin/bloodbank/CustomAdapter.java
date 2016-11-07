
package com.lol.majchin.bloodbank;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by majch on 08-10-2016.
 */

public class CustomAdapter extends BaseAdapter implements View.OnClickListener {

    /*********** Declare Used Variables *********/
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModel tempValues=null;
    int i=0;

    /*************  CustomAdapter Constructor *****************/
    public CustomAdapter(Activity a, ArrayList d,Resources resLocal) {

        /********** Take passed values **********/
        activity = a;
        data=d;
        res = resLocal;

        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView txt_BloodType;
        public TextView txt_Email;
        public TextView txt_Contact ;
        public TextView txt_Description;
        //public ImageView img_phoneicon ;

    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.txt_BloodType = (TextView) vi.findViewById(R.id.txt_t_bloodtype);
            holder.txt_Email=(TextView)vi.findViewById(R.id.txt_t_email);
            holder.txt_Contact=(TextView)vi.findViewById(R.id.txt_t_contact);
            holder.txt_Description=(TextView)vi.findViewById(R.id.txt_t_description);
            //holder.img_phoneicon=(ImageView)vi.findViewById(R.id.img_t_phoneicon);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.txt_BloodType.setText("No Data");
            holder.txt_Email.setText("No Data");
            holder.txt_Contact.setText("No Data");
            holder.txt_Description.setText("No Data");
        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListModel ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.txt_BloodType.setText( tempValues.getBloodType() );
            holder.txt_Email.setText( tempValues.getEmail() );
            holder.txt_Contact.setText(tempValues.getContact());
            holder.txt_Description.setText(tempValues.getDescription());

            /*
            holder.image.setImageResource(
                    res.getIdentifier("com.lol.majchin.findphones:drawable/"+tempValues.getImage(),null,null) );

            */
            /******** Set Item Click Listner for LayoutInflater for each row *******/

            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

    @Override
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {


            HomeActivity sct = (HomeActivity)activity;

            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/

            sct.onItemClick(mPosition);
        }
    }
}