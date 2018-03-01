package dnsudhir.com.webservicesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import dnsudhir.com.callapilib.CallAPI;
import dnsudhir.com.callapilib.ServiceGenerator;
import dnsudhir.com.webservicesample.util.ApiServiceInterface;
import java.util.List;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ServiceGenerator.build("https://api.github.com/");

    nGetAllRepos();
  }

  private void nGetAllRepos() {
    Call<List<RepoBO>> call =
        ServiceGenerator.createService(ApiServiceInterface.class).getAllRepos("dnsudhir");
    new CallAPI.Builder(this, call, true).setOnCallCompleteListner(
        new CallAPI.OnCallComplete<List<RepoBO>>() {
          @Override public void CallCompleted(boolean b, List<RepoBO> repoBOS) {
            if (repoBOS != null) {
              RepoBO repoBO = repoBOS.get(0);
              Toast.makeText(MainActivity.this, repoBO.getFull_name(), Toast.LENGTH_SHORT).show();
            }
          }
        }).execute();
  }
}
