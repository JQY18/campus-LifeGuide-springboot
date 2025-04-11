package renko.jiang.campus_life_guide.pojo.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    //编码：1成功，0和其它数字为失败,2用户已存在注册失败
    @Schema(description = "编码：1成功，0和其它数字为失败,2用户已存在注册失败",example = "1")
    private Integer code;
    //错误信息
    @Schema(description = "返回信息", example = "success")
    private String msg;
    //数据
    @Schema(description = "返回数据", example = "{}")
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.msg = "success";
        result.code = 1;
        return result;
    }

    public static Result<String> success(String msg) {
        Result<String> result = new Result<String>();
        result.msg = msg;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<T>();
        result.msg = "success";
        result.data = object;
        result.code = 1;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<T>();
        result.msg = msg;
        result.code = 0;
        return result;
    }
}
