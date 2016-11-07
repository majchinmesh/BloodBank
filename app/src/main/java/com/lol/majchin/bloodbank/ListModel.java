package com.lol.majchin.bloodbank;

/**
 * Created by majch on 08-10-2016.
 */

public class ListModel {

    private  String BloodType="";
    private  String Email="";
    private  String Contact="";
    private String Description = ""; // the encoded data of the icon image

    
    /*********** Set Methods ******************/

    public void setBloodType(String BT)
    {
        this.BloodType = BT;
    }

    public void setEmail(String e )
    {
        this.Email = e;
    }

    public void setContact(String Contact)
    {
        this.Contact = Contact;
    }

    public void setDescription(String Description)
    {
        this.Description = Description;
    }

    /*********** Get Methods ****************/

    public String getBloodType()
    {
        return this.BloodType;
    }

    public String getEmail()
    {
        return this.Email;
    }

    public String getContact() {return this.Contact;}

    public String getDescription() {return this.Description;}
}
