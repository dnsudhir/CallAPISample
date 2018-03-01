package dnsudhir.com.callapilib;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.gson.Gson;
import okhttp3.Headers;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class CallAPI<T> extends AsyncTask<Void, Void, T> {

  private Context context;
  private Call<T> call;
  private boolean dialog;
  private String dialogTitle = "Please Wait";
  private String dialogMessage = "Loading...";
  private ProgressDialog progressDialog;
  private OnCallComplete onCallComplete;
  private AlertDialog.Builder builder;
  private final String TAG = "ServiceOutPut";

  public CallAPI(Context context, Call<T> call) {
    this.context = context;
    this.call = call;
  }

  public CallAPI(Context context, Call<T> call, boolean dialog) {
    this.context = context;
    this.call = call;
    this.dialog = dialog;
  }

  public CallAPI(Context context, Call<T> call, String dialogTitle, String dialogMessage) {
    this.context = context;
    this.call = call;
    this.dialogTitle = dialogTitle;
    this.dialogMessage = dialogMessage;
  }

  public CallAPI(Context context, Call<T> call, boolean dialog, String dialogTitle,
      String dialogMessage, OnCallComplete<T> onCallComplete) {

    this.context = context;
    this.call = call;
    this.dialog = dialog;
    this.dialogTitle = dialogTitle;
    this.dialogMessage = dialogMessage;
    this.onCallComplete = onCallComplete;
    setCheckExecute(this, this.onCallComplete);
  }

  public void setProgressDialog(boolean dialog) {
    this.dialog = dialog;
  }

  public void setProgressDialog(String dialogTitle, String dialogMessage) {
    this.dialog = true;
    this.dialogTitle = dialogTitle;
    this.dialogMessage = dialogMessage;
  }

  @Override protected void onPreExecute() {
    super.onPreExecute();
    if (dialog) {
      progressDialog = ProgressDialog.show(context, dialogTitle, dialogMessage, false, false);
    }
  }

  @Override protected T doInBackground(Void... voids) {
    T t = null;
    Response response;
    try {
      Log.d(TAG + "URL:", call.request().url().toString());
      RequestBody requestBody = call.request().body();

      if (requestBody != null) {
        Log.d(TAG + "BODY:", new Gson().toJson(requestBody));
      }

      Log.d(TAG + "HEADERS:", call.request().headers().toString());
      response = call.execute();
      Headers headers = response.headers();
      t = (T) response.body();

      Log.d(TAG, new Gson().toJson(t).toString());
    } catch (Exception e) {
      e.printStackTrace();
      Log.d(TAG, e.toString());
      builder = new AlertDialog.Builder(context);
      builder.setTitle("Network Error");
      builder.setMessage("Server failed, Please try again after some time");
      builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {

        }
      });
    } finally {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
      }
      ((AppCompatActivity) context).runOnUiThread(new Runnable() {
        @Override public void run() {
          if (builder != null) {
            builder.show();
          }
        }
      });
    }
    return t;
  }

  @Override protected void onPostExecute(T res) {
    super.onPostExecute(res);
    if (dialog && progressDialog.isShowing()) progressDialog.dismiss();
    onCallComplete.CallCompleted(res != null, res);
  }

  public static class Builder<T> {

    private Context context;
    private Call<T> call;
    private boolean dialog;
    private String dialogTitle = "Please Wait";
    private String dialogMessage = "Loading...";
    private ProgressDialog progressDialog;
    private OnCallComplete onCallComplete;
    private AlertDialog.Builder builder;
    private final String TAG = "ServiceOutPut";

    public Builder(Context context, Call<T> call, boolean dialog) {
      this.context = context;
      this.call = call;
      this.dialog = dialog;
    }

    public Builder(Context context, Call<T> call) {
      this.context = context;
      this.call = call;
    }

    public Builder(Context context, Call<T> call, String dialogTitle, String dialogMessage) {
      this.context = context;
      this.call = call;
      this.dialogTitle = dialogTitle;
      this.dialogMessage = dialogMessage;
    }

    public Builder setProgressDialog(boolean dialog) {
      this.dialog = dialog;
      return this;
    }

    public Builder setProgressDialog(String dialogTitle, String dialogMessage) {
      this.dialog = true;
      this.dialogTitle = dialogTitle;
      this.dialogMessage = dialogMessage;
      return this;
    }

    public Builder setOnCallCompleteListner(OnCallComplete<T> onCallComplete) {
      this.onCallComplete = onCallComplete;
      return this;
    }

    public void execute() {
      new CallAPI<T>(context, this.call, this.dialog, this.dialogTitle, this.dialogMessage,
          this.onCallComplete);
    }
  }

  public void setCheckExecute(CallAPI<T> callAPI, OnCallComplete onCallComplete) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    assert cm != null;
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
      this.onCallComplete = onCallComplete;
      callAPI.execute();
    } else {
      AlertDialog.Builder builder = new AlertDialog.Builder(context);
      builder.setTitle("No Internet Connection");
      builder.setMessage("Please Check Your Internet Connection");
      builder.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          ((AppCompatActivity) context).finish();
          context.startActivity((((AppCompatActivity) context).getIntent()));
        }
      });
      builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
        @Override public void onClick(DialogInterface dialog, int which) {
          ((AppCompatActivity) context).finish();
        }
      });
      builder.setCancelable(false);
      builder.show();
    }
  }

  public interface OnCallComplete<T> {
    void CallCompleted(boolean b, T result);
  }
}
