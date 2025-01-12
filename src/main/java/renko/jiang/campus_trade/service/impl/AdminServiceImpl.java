package renko.jiang.campus_trade.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renko.jiang.campus_trade.controller.admin.pojo.dto.AdminDTO;
import renko.jiang.campus_trade.controller.admin.pojo.entity.Admin;
import renko.jiang.campus_trade.controller.admin.pojo.vo.AdminVO;
import renko.jiang.campus_trade.mapper.AdminMapper;
import renko.jiang.campus_trade.pojo.entity.User;
import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.service.AdminService;
import renko.jiang.campus_trade.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserService userService;

    @Override
    public Result<AdminVO> login(AdminDTO adminDTO) {
        String username = adminDTO.getUsername();
        String password = adminDTO.getPassword();
        Admin admin = adminMapper.login(username, password);
        if (admin == null) {
            return Result.error("账号或密码错误!");
        }
        AdminVO adminVO = new AdminVO();
        // 将admin转换为adminVO
        adminVO.setId(admin.getId());
        adminVO.setUsername(admin.getUsername());
        adminVO.setLocationId(admin.getLocationId());
        return Result.success(adminVO);
    }

    @Override
    public List<User> getList() {
        List<User> list = adminMapper.getList();
        return list;
    }

    @Override
    public void delete(Integer id) {
        adminMapper.delete(id);
    }

    @Override
    public Result<String> updatePassword(Integer id, String currentPassword, String newPassword) {
        Admin admin = adminMapper.getAdminById(id);
        if (admin == null) {
            return Result.error("用户不存在");
        }
        if (admin.getPassword().equals(currentPassword)) {
            adminMapper.updatePassword(id, newPassword);
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }

    @Override
    public Result<AdminVO> getAdminById(Integer id) {
        Admin admin = adminMapper.getAdminById(id);
        if (admin != null) {
            AdminVO adminVO = new AdminVO();
            adminVO.setId(admin.getId());
            adminVO.setUsername(admin.getUsername());
            adminVO.setLocationId(admin.getLocationId());
            return Result.success(adminVO);
        }
        return Result.error("获取失败");
    }

    @Override
    public Result<List<AdminVO>> getAll() {
        List<Admin> list = adminMapper.getAllAdmin();
        if (list != null) {
            List<AdminVO> adminVOS = new ArrayList<>();
            for (Admin admin : list) {
                AdminVO adminVO = new AdminVO();
                String locationName = adminMapper.getLocationNameById(admin.getLocationId());
                if(locationName != null){
                    adminVO.setLocationName(locationName);
                }else{
                    adminVO.setLocationName("全部地点");
                }
                adminVO.setId(admin.getId());
                adminVO.setUsername(admin.getUsername());
                adminVO.setLocationId(admin.getLocationId());
                adminVO.setCreatedTime(admin.getCreatedTime());
                adminVOS.add(adminVO);
            }
            return Result.success(adminVOS);
        }
        return Result.error("获取失败");
    }

    @Override
    public Result addAdmin(AdminDTO adminDTO) {
        if (adminDTO != null) {
            Admin admin = new Admin();
            admin.setUsername(adminDTO.getUsername());
            admin.setPassword(adminDTO.getPassword());
            admin.setLocationId(adminDTO.getLocationId());
            Integer id = adminMapper.addAdmin(admin);
            return Result.success("添加成功");
        }
        return Result.error("添加失败");
    }

    @Override
    public Result<String> update(AdminDTO adminDTO) {
        if (adminDTO != null) {
            Admin admin = new Admin();
            admin.setId(adminDTO.getId());
            admin.setUsername(adminDTO.getUsername());
            admin.setPassword(adminDTO.getPassword());
            admin.setLocationId(adminDTO.getLocationId());
            adminMapper.update(admin);
            return Result.success("修改成功");
        }
        return Result.error("修改失败");
    }
}
