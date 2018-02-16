package util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import model.Book;
import model.BookCategory;
import model.Result;

/**
 * Created by cz on 2017-6-18.
 */

public class GsonUtil {


    public static <T> Result<T> fromJsonObject(String reader, Class<T> clazz) {
        Type type = new ParameterizedTypeImpl(Result.class, new Class[]{clazz});
        return new Gson().fromJson(reader, type);
    }

    public static <T> Result<List<T>> fromJsonList(String reader, Class<T> clazz) {
        // 生成List<T> 中的 List<T>
        Type listType = new ParameterizedTypeImpl(List.class, new Class[]{clazz});
        // 根据List<T>生成完整的Result<List<T>>
        Type type = new ParameterizedTypeImpl(Result.class, new Type[]{listType});
        return new Gson().fromJson(reader, type);
    }
}
