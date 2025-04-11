package renko.jiang.campus_life_guide.mapper;

import org.apache.ibatis.annotations.*;
import renko.jiang.campus_life_guide.pojo.dto.LoginDTO;
import renko.jiang.campus_life_guide.pojo.dto.UserInfoDTO;
import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.vo.UserInfoVO;

import java.util.List;

@Mapper
public interface UserMapper {
    //根据用户名查询用户信息
    @Select("select * from user where username = #{username}")
    User queryUser(String username);

    //添加用户
    @Insert("insert into user (username, password, nickname,email) values (#{username}, #{password}, #{nickname},#{email})")
    void addUser(LoginDTO loginDTO);

    //根据uerId查询用户信息
    @Select("select * from user where id = #{userId}")
    User queryUserById(Integer userId);

    //修改用户信息
    void updateUser(User user);

    @Select("select id,username,gender,age,nickname,avatar,school from user where id = #{userId}")
    UserInfoVO getUserInfoById(Integer userId);

    // 修改用户信息
    void updateUserInfo(UserInfoDTO userInfoDTO);

    @Update("update user set avatar = #{avatar} where id = #{id}")
    void updateAvatar(Integer id, String avatar);

    @Update("update user set password = #{newPassword} where id = #{id} and password = #{currentPassword}")
    int updatePassword(Integer id, String currentPassword, String newPassword);

    @Delete("delete from user where id = #{id}")
    int deleteUser(Integer id);

    int countByCondition(User user);

    List<User> pageQueryByCondition(User user, int start, Integer pageSize);


    List<User> queryUserInfoByIds(List<Integer> userIds);

    @Select("select count(1) from user where email = (#{email})")
    int existUserByEmail(String email);
}
