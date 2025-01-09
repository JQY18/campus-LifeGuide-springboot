package renko.jiang.campus_trade.pojo.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 86132
 */
@Data
public class UserInfoDTO {
    private Integer id;
    private String nickname;
    private String gender;
    private Integer age;
    private MultipartFile avatar;
    private String imageUrl;
}
// 请求体：FormData 包含以下字段
// - avatar: File (可选)
// - nickname: string
// - gender: string
// - age: number
// 返回格式：
// {
//   code: 1,
//   msg: "success",
//   data: {
//     id: number,
//     username: string,
//     nickname: string,
//     gender: string,
//     age: number,
//     avatar: string
//   }
// }