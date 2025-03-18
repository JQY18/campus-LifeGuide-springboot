package renko.jiang.campus_life_guide.mapper;

import org.apache.ibatis.annotations.*;
import renko.jiang.campus_life_guide.controller.admin.pojo.entity.Admin;
import renko.jiang.campus_life_guide.pojo.entity.User;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("select * from admin where username = #{username} and password = #{password}")
    Admin login(String username, String password);

    @Select("select * from user")
    List<User> getList();

    @Select("select * from admin where id = #{id}")
    Admin getAdminById(Integer id);

    @Select("update admin set password = #{newPassword} where id = #{id}")
    void updatePassword(Integer id, String newPassword);

    @Select("select * from admin order by location_id asc, created_time desc")
    List<Admin> getAllAdmin();


    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into admin (username, password, location_id) value (#{username},#{password},#{locationId})")
    Integer addAdmin(Admin admin);

    @Update("update admin set username = #{username},password = #{password},location_id = #{locationId} where id = #{id}")
    Integer update(Admin admin);

    @Delete("delete from admin where id = #{id}")
    void delete(Integer id);

    @Select("select name from location where id = #{locationId}")
    String getLocationNameById(Integer locationId);

    @Select("select count(*) from user")
    Long countUser();

    @Select("select count(*) from user where create_time >= #{from}")
    Long countUserByDate(LocalDate from);

    @Select("select count(*) from location")
    Long countLocation();

    @Select("select count(*) from location where created_time >= #{from}")
    Long countLocationByDate(LocalDate from);

    @Select("select username from admin where id = #{publisher}")
    String queryUsernameById(Integer publisher);
}
