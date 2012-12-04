package proj.android2.androidDB;

public class Contact{
	 
  //private variables
  int contact_id;
  String contact_name;
  String contact_number;
 
  // Empty constructor
  public Contact(){}
  
  // constructor
  public Contact(int id, String name, String phone_number){
	  this.contact_id = id;
      this.contact_name = name;
      this.contact_number = phone_number;
    }
 
  // constructor
  public Contact(String name, String phone_number){
      this.contact_name = name;
      this.contact_number = phone_number;
  }
  
  // getting ID
  public int getID(){
	  return this.contact_id;
  }
 
  // setting id
  public void setID(int id){
      this.contact_id = id;
  }
 
  // getting name
  public String getName(){
      return this.contact_name;
  }
 
  // setting name
  public void setName(String name){
      this.contact_name = name;
  }
 
  // getting phone number
  public String getPhoneNumber(){
      return this.contact_number;
  }
 
  // setting phone number
  public void setPhoneNumber(String phone_number){
        this.contact_number = phone_number;
    }
}