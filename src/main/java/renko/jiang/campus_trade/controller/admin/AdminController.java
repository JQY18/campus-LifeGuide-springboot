package renko.jiang.campus_trade.controller.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_trade.controller.admin.pojo.dto.AdminDTO;
import renko.jiang.campus_trade.controller.admin.pojo.vo.AdminVO;
import renko.jiang.campus_trade.pojo.entity.Post;
import renko.jiang.campus_trade.pojo.entity.User;
import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.service.AdminService;

import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    /**
     * 登录
     */
    @PostMapping("/login")
    public Result<AdminVO> login(@RequestBody AdminDTO adminDTO) {
        return adminService.login(adminDTO);
    }

    @GetMapping("/info")
    public Result<AdminVO> getInfo(Integer id) {
        return adminService.getAdminById(id);
    }


    @PostMapping("/add")
    public Result addAdmin(@RequestBody AdminDTO adminDTO) {
        return adminService.addAdmin(adminDTO);
    }

    /***
     * 修改密码
     * @return
     */
    @PostMapping("/updatePassword")
    public Result<String> updatePassword(@RequestBody Map<String,String> body){
        Integer id = Integer.parseInt(body.get("id"));
        return adminService.updatePassword(id,body.get("oldPassword"),body.get("newPassword"));
    }


    @GetMapping("/list")
    public Result<List<AdminVO>> getAll() {
        return adminService.getAll();
    }


    @GetMapping("/user/list")
    public Result<List<User>> getList() {
        return Result.success(adminService.getList());
    }
    @DeleteMapping("/delete/{id}")
    public Result<String> delete(@PathVariable Integer id) {
        adminService.delete(id);
        System.out.println(id);
        return Result.success();
    }

    @PostMapping("/update")
    public Result<String> update(@RequestBody AdminDTO adminDTO) {
        return adminService.update(adminDTO);
    }

}
