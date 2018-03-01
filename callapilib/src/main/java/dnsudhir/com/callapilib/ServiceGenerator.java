package dnsudhir.com.callapilib;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {

  private static String ROOT_URL = "";
  private static Retrofit.Builder builder;
  private static Retrofit retrofit;

  private static HttpLoggingInterceptor loggingInterceptor =
      new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  public static void build(String root_url) {
    ROOT_URL = root_url;
    builder =
        new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create());
    retrofit = builder.client(httpClient.readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()).build();
  }

  public static <S> S createService(Class<S> serviceClass) {
    return retrofit.create(serviceClass);
  }
}