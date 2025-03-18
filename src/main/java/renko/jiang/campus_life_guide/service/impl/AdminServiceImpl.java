package renko.jiang.campus_life_guide.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renko.jiang.campus_life_guide.controller.admin.pojo.dto.AdminDTO;
import renko.jiang.campus_life_guide.controller.admin.pojo.entity.Admin;
import renko.jiang.campus_life_guide.controller.admin.pojo.vo.AdminVO;
import renko.jiang.campus_life_guide.mapper.AdminMapper;
import renko.jiang.campus_life_guide.pojo.entity.User;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.service.AdminService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;

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
            adminVO.setCreatedTime(admin.getCreatedTime());
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

    @Override
    public Result<Map<String, Long>> getUserCount() {
        Long count = adminMapper.countUser();
        //本周注册量
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(to.getDayOfWeek().getValue() - 1);
        Long weekCount = adminMapper.countUserByDate(from);
        Map<String, Long> map = Map.of("userCount", count, "userTrend", weekCount);
        return Result.success(map);
    }

    @Override
    public Result<Map<String, Long>> getLocationCount() {
        Long count = adminMapper.countLocation();
        //本周注册量
        LocalDate to = LocalDate.now();
        LocalDate from = to.minusDays(to.getDayOfWeek().getValue() - 1);
        Long weekCount = adminMapper.countLocationByDate(from);
        Map<String, Long> map = Map.of("locationCount", count, "locationTrend", weekCount);
        return Result.success(map);
    }
}
