package renko.jiang.campus_life_guide.service;

import renko.jiang.campus_life_guide.controller.admin.pojo.dto.AdminDTO;
import renko.jiang.campus_life_guide.controller.admin.pojo.vo.AdminVO;
import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.result.Result;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Result<AdminVO> login(AdminDTO adminDTO);

    List<User> getList();

    void delete(Integer id);

    Result<String> updatePassword(Integer id, String currentPassword, String newPassword);

    Result<AdminVO> getAdminById(Integer id);

    Result<List<AdminVO>> getAll();

    Result addAdmin(AdminDTO adminDTO);

    Result<String> update(AdminDTO adminDTO);

    Result<Map<String, Long>> getUserCount();

    Result<Map<String, Long>> getLocationCount();
}
