package dnsudhir.com.webservicesample.util;

import dnsudhir.com.webservicesample.RepoBO;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiServiceInterface {

  @GET("/users/{user}/repos") Call<List<RepoBO>> getAllRepos(@Path("user") String username);
}
