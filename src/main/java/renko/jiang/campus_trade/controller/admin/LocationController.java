package renko.jiang.campus_trade.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import renko.jiang.campus_trade.controller.admin.pojo.dto.DetailCommentDTO;
import renko.jiang.campus_trade.controller.admin.pojo.dto.LocationDTO;
import renko.jiang.campus_trade.controller.admin.pojo.entity.DetailComment;
import renko.jiang.campus_trade.controller.admin.pojo.entity.Location;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationDetailVO;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationVO;
import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.service.LocationService;

import java.util.List;

/**
 * @author 86132
 */

@CrossOrigin("*")
@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    /***
     * 获取所有位置信息
     * @return
     */
    @GetMapping
    public Result<List<LocationVO>> getAllLocations(Integer locationId) {
        return locationService.getAllLocations(locationId);
    }

    /***
     * 根据detailId获取位置的详细描述信息
     * @param detailId
     * @return
     */
    @GetMapping("/detail/{detailId}")
    public Result<LocationDetailVO> getDetailById(@PathVariable String detailId) {
        return locationService.getDetailById(detailId);
    }

    /***
     * 根据detailId获取详细页中的评论
     * @param detailId
     * @return
     */
    @GetMapping("/comments/{detailId}")
    public Result<List<DetailComment>> getCommentsById(@PathVariable String detailId) {
        return locationService.getCommentsById(detailId);
    }

    /***
     * 根据评论的id提交评论
     * @param detailCommentDTO
     * @return
     */
    @PostMapping("/comments/add")
    public Result<String> submitComment(@RequestBody DetailCommentDTO detailCommentDTO) {
        return locationService.submitComment(detailCommentDTO);
    }


    /***
     * 添加位置
     * @param locationDTO
     * @return
     */
    @PostMapping("/add")
    public Result addLocation(LocationDTO locationDTO) {
        return locationService.addLocation(locationDTO);
    }

    /***
     * 修改位置
     * @param locationDTO
     * @return
     */
    @PostMapping("/update")
    public Result updateLocation(LocationDTO locationDTO) {
        return locationService.updateLocation(locationDTO);
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteLocation(@PathVariable Long id) {
        return locationService.deleteLocation(id);
    }
}