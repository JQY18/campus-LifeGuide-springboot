package renko.jiang.campus_life_guide.service;

import org.springframework.web.multipart.MultipartFile;
import renko.jiang.campus_life_guide.pojo.dto.LoginDTO;
import renko.jiang.campus_life_guide.pojo.dto.UserInfoDTO;
import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.result.PageResult;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.UserInfoVO;

public interface UserService {
    User login(LoginDTO loginDTO);

    void register(LoginDTO loginDTO);

    UserInfoVO getUserInfoById(Integer userId);

    void updateUserInfo(UserInfoDTO userInfoDTO);

    void updateAvatar(Integer id, MultipartFile avatar);

    void updatePassword(Integer id, String currentPassword, String newPassword);

    Result deleteUser(Integer id);

    Result<PageResult<UserInfoVO>> page(UserInfoDTO userInfoDTO);
}
