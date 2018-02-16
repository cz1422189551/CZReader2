package network;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.request.RequestCall;

/**
 * Created by cz on 2017-6-15.
 */

public class RequestData {

    public static RequestCall submit(String paramsKey, String paramsValues, String requestURL) {
        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        postFormBuilder.addParams(paramsKey, paramsValues);
        postFormBuilder.url(requestURL);
        return postFormBuilder.build();
    }
    public static RequestCall submit(String paramsKey, String paramsValues, String p2,String v2,String requestURL) {
        PostFormBuilder postFormBuilder = OkHttpUtils.post();
        postFormBuilder.addParams(paramsKey, paramsValues);
        postFormBuilder.addParams(p2,v2);
        postFormBuilder.url(requestURL);
        return postFormBuilder.build();
    }

    public static RequestCall downLoadfile(String url,String params,String values)
    {
        PostFormBuilder getBuilder = OkHttpUtils.post();;
        getBuilder.url(url);
        getBuilder.addParams(params,values);
        RequestCall build = getBuilder.build();
        return build;
    }

}
