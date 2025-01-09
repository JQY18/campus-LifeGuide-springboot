package renko.jiang.campus_trade.pojo.result;

import lombok.Data;

import java.io.Serializable;


@Data
public class Result<T> implements Serializable {
    //编码：1成功，0和其它数字为失败,2用户已存在注册失败
    private Integer code;
    //错误信息
    private String msg;
    //数据
    private T data;

    public static <T> Result<T> success() {
        Result<T> result = new Result<T>();
        result.msg = "success";
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
