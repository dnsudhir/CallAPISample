# CallAPISample
This library helps to integrate API's with just one line of code.
The library uses retrofit 2.3 for making web service calls.
The following libraries are the prerequistes for this library.

    compile 'com.squareup.retrofit2:retrofit:2.3.0'
    compile 'com.squareup.retrofit2:converter-gson:2.3.0'
    compile 'com.squareup.okhttp3:logging-interceptor:3.8.0'
    
    
And the app should have the following permissions

   INTERNET
  ACCESS_NETWORK_STATE
  
in the AndroidManifest.xml.

You can integrate the library by adding the following line in dependencies to the build.gradle of your app:module,

 compile 'dnsudhir.com.callapilib:callapilib:1.1'
  

Following are the steps to use the library:

Step 1:
Create a POJO of response you received from the web service.

Step 2:
Initialize the ServiceGenerator

Eg:
 ServiceGenerator.build("https://api.github.com/");


Step 3:
Declare a method in interface which returns the Call with parameterized type of POJO created.

Eg:
public interface ApiEndPoint{

@GET("/users/{user}/repos") Call<List<POJO>> getAllRepos(@Path("user") String username);

}

Step 4:
Create a Call instance.

Call<List<POJO>> call = ServiceGenerator.createService(ApiEndPoint.class);
new CallAPI.Builder(this,call,true)
           .setOnCallCompleteListener(new CallAPI.OnCallComplete<List<POHO>>(){
             @Override public void CallCompleted(boolean b, List<POJO> pojo) {
            // Code to be executed after Web Service call complettion
           
           }).execute();


