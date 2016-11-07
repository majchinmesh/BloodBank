package com.lol.majchin.bloodbank;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by majch on 02-10-2016.
 */
public class HomeActivity extends AppCompatActivity  {

    int POSTS_COUNT = 50 ;


    Button btnLogout , btnPost ;
    String UserID ;
    String[] StringArray = { " \tYou will use Android Studio IDE to create an Android application and name it as ListDisplay under a package com.example.ListDisplay as explained in the Hello World Example chapter", "Modify the default content of res/layout/activity_main.xml file to include ListView content with the self explanatory attributes.", "No need to change string.xml, Android studio takes care of default string constants." };

    JSONArray posts ;

    ListModel List_row ;
    //ListModel[] postsListDataArray = new  ListModel[POSTS_COUNT] ;

    ListView list;
    CustomAdapter adapter;
    public  HomeActivity CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArr = new ArrayList<ListModel>();


    protected void onCreate(Bundle savedInstanceState) {

        try {
            UserID = SaveSharedPreference.getUser(getApplicationContext());
        }catch (Exception e){

        }

        if(UserID.length() == 0)
        {
            Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
            HomeActivity.this.startActivity(gotoLogin);
            HomeActivity.this.finish();
        }
        else
        {




            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            CustomListView = this;

            /******** Take some data in Arraylist ( CustomListViewValuesArr ) ***********/

            //setListData();

            new PostAsync().execute( Integer.toString(POSTS_COUNT) );


            /*
            Resources res =getResources();
            list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

            */
            /**************** Create Custom Adapter *********/
            //adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );

            /*
            Log.v("Final List","adding the addapter to the listview");
            adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
            list.setAdapter( adapter );

            */


            /*
            // Stay at the current activity.
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);
            */

            Toast.makeText(this,"Welcome "+UserID, Toast.LENGTH_LONG).show();

            /*
            ArrayAdapter listview_adapter = new ArrayAdapter<String>(this,R.layout.activity_listview,StringArray);
            ListView advert_listview = (ListView) findViewById(R.id.listview_h_main);
            advert_listview.setOnItemClickListener(this);
            advert_listview.setAdapter(listview_adapter);
            */

            //ListView yourListView = (ListView) findViewById(R.id.listview_h_main);

            // get data from the table by the ListAdapter
            //ListAdapter customAdapter = new ListAdapter(this, R.layout.itemlistrow, List<advertTable>);

            //yourListView .setAdapter(customAdapter);


        }


        btnLogout = (Button)findViewById(R.id.btn_h_logout);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
            }
        });


        btnPost = (Button)findViewById(R.id.btn_h_post);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gotoPost();
            }
        });

    }

    public void logout(){

        SaveSharedPreference.setUser(getApplicationContext() ,"" );

        Toast.makeText(this, "Goodbye " + UserID, Toast.LENGTH_LONG).show();
        Intent gotoLogin = new Intent(HomeActivity.this, LoginActivity.class);
        HomeActivity.this.startActivity(gotoLogin);
        HomeActivity.this.finish();
    }


    public void gotoPost(){

        Toast.makeText(this, "Ready to donate your blood ?" + UserID, Toast.LENGTH_LONG).show();
        Intent gotoPost = new Intent(HomeActivity.this, PostActivity.class);
        HomeActivity.this.startActivity(gotoPost);
        HomeActivity.this.finish();
    }



    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);

        // SHOW ALERT

        //Toast.makeText(CustomListView,"Phone Company : "+tempValues.getPhoneCompany() +"Phone Name :"+tempValues.getPhoneName()+"Url:"+tempValues.getPhonePrice(),Toast.LENGTH_LONG).show();
    }



    public void inflateListView() // setListData()
    {

        /*
        for (int i = 0; i < POSTS_COUNT; i++) {

            CustomListViewValuesArr.add( postsListDataArray[i] );
            Log.v("ListData"," added 1 to ArrayList");

        }

        */


        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );  // List defined in XML ( See Below )

        Log.v("Final List","adding the addapter to the listview");
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter( adapter );

    }





    class PostAsync extends AsyncTask<String, String, JSONObject> {
        JSONParser jsonParser = new JSONParser();
        private ProgressDialog pDialog;
       // private static final String POSTS_URL = "http://chinmeshmanjrekar.co.nf/AndroidUploadImage/getposts.php";
       private static final String POSTS_URL = Constants.DONORS_URL;

        private static final String TAG_SUCCESS = "success";
        private static final String TAG_MESSAGE = "message";
        private static final String TAG_POSTS = "posts";

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Fetching data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            try {
                HashMap<String, String> params = new HashMap<>();
                params.put("count", args[0]);// how many to fetch
                //params.put("password", args[1]);
                Log.d("request", "starting");
                JSONObject json = jsonParser.makeHttpRequest(
                        POSTS_URL, "POST", params);
                if (json != null) {
                    Log.d("JSON result", json.toString());
                    return json;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(JSONObject json) {
            int success = 0;
            String message = "";
            JSONObject row = new JSONObject() ;

            if (pDialog != null && pDialog.isShowing()) {
                pDialog.dismiss();
            }
            if (json != null) {
                //Toast.makeText(HomeActivity.this, json.toString(), Toast.LENGTH_LONG ).show(); // must be commented finally
                try {
                    success = json.getInt(TAG_SUCCESS);
                    message = json.getString(TAG_MESSAGE);
                    posts = json.getJSONArray(TAG_POSTS) ;


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

                Toast.makeText(HomeActivity.this, "Network Error", Toast.LENGTH_LONG ).show();

            }


            if (success == 1) {

                Log.d("Success!", message);

                for (int i = 0; i < posts.length(); ++i) {

                    //postsListDataArray[i] = new ListModel() ;
                    List_row = new ListModel();

                    try {
                        row = posts.getJSONObject(i);
                        //postsListDataArray[i].setPhoneName( ( row.getString("phonename") ) ) ;
                        List_row.setBloodType( ( row.getString("bloodtype") ) ) ;
                        //postsListDataArray[i].setPhoneCompany( ( row.getString("companyname") ) );
                        List_row.setEmail ( row.getString("email") );
                        //postsListDataArray[i].setPhonePrice( (String)( row.getString("sellingprice") ) );
                        List_row.setContact( (String)( row.getString("contact") ) );

                        List_row.setDescription(row.getString("description"));

                        CustomListViewValuesArr.add(List_row);

                        Log.v("JSON Success","inflated 1 listview");
                    }
                    catch( JSONException je){

                        Log.e("JSON Post","Problem with posts data");
                        je.printStackTrace();
                    }
                }
            }else{
                //Toast.makeText(HomeActivity.this, message , Toast.LENGTH_LONG ).show();
                Log.d("Failure", message);
            }


            //setListData(); // sets data from postsListDataArray into the listsViews
            inflateListView();

        }
    }
}


