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
  private static int timeout = 60;

  private static HttpLoggingInterceptor loggingInterceptor =
      new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
  private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

  public static void build(String root_url) {
    ROOT_URL = root_url;
    builder =
        new Retrofit.Builder().baseUrl(ROOT_URL).addConverterFactory(GsonConverterFactory.create());
    retrofit = builder.client(httpClient.readTimeout(ServiceGenerator.timeout, TimeUnit.SECONDS)
        .connectTimeout(ServiceGenerator.timeout, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()).build();
  }

  public static void build(String root_url, int timeout, boolean allowIntercept) {
    ROOT_URL = root_url;
    ServiceGenerator.timeout = timeout;
    if (allowIntercept) {
      builder = new Retrofit.Builder().baseUrl(ROOT_URL)
          .addConverterFactory(GsonConverterFactory.create());
      retrofit = builder.client(httpClient.readTimeout(timeout, TimeUnit.SECONDS)
          .connectTimeout(timeout, TimeUnit.SECONDS)
          .addInterceptor(loggingInterceptor)
          .build()).build();
    } else {
      builder = new Retrofit.Builder().baseUrl(ROOT_URL)
          .addConverterFactory(GsonConverterFactory.create());
      retrofit = builder.client(httpClient.readTimeout(timeout, TimeUnit.SECONDS)
          .connectTimeout(timeout, TimeUnit.SECONDS)
          .build()).build();
    }
  }

  public static <S> S createService(Class<S> serviceClass) {
    return retrofit.create(serviceClass);
  }
}