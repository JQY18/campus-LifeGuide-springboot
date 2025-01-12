package renko.jiang.campus_trade.global;

import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import renko.jiang.campus_trade.pojo.result.Result;

@RestControllerAdvice
public class GlobalAdvice {

    // 注意到你还提到了 ClientAbortException，
    // 这通常是由于客户端在服务器还在发送响应的时候断开了连接所引起的。
    // 这不是一个问题，而是一个正常的网络通信现象，尤其是在处理大文件下载时。
    // 你可以考虑忽略这类异常或者适当地记录它们而不中断服务。
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        if(e instanceof ClientAbortException){
            e.printStackTrace();
            return Result.error("客户端异常");
        }
        e.printStackTrace();
        return Result.error(e.getMessage());
    }


}
