package renko.jiang.campus_trade.service;

import renko.jiang.campus_trade.controller.admin.pojo.dto.AdminDTO;
import renko.jiang.campus_trade.controller.admin.pojo.vo.AdminVO;
import renko.jiang.campus_trade.pojo.entity.User;
import renko.jiang.campus_trade.pojo.result.Result;

import java.util.List;

public interface AdminService {

    Result<AdminVO> login(AdminDTO adminDTO);

    List<User> getList();

    void delete(Integer id);

    Result<String> updatePassword(Integer id, String currentPassword, String newPassword);

    Result<AdminVO> getAdminById(Integer id);

    Result<List<AdminVO>> getAll();

    Result addAdmin(AdminDTO adminDTO);

    Result<String> update(AdminDTO adminDTO);
}
