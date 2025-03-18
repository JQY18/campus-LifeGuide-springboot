package renko.jiang.campus_life_guide.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import renko.jiang.campus_life_guide.controller.admin.pojo.dto.LocationDetailDTO;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.service.LocationService;

import java.util.List;

/**
 * @author 86132
 */
@CrossOrigin("*")
@RestController
@RequestMapping("/locationDetails")
public class LocationDetailController {
    @Autowired
    private LocationService locationService;

    /**
     * 上传地点详情,修改和新增地点详情，不包括图片和视频
     * @param locationDetailDTO
     * @return
     */
    @PostMapping("/update")
    public Result uploadDetails(LocationDetailDTO locationDetailDTO){
        if (locationDetailDTO == null){
            return Result.error("上传失败");
        }
        return locationService.updloadDetails(locationDetailDTO);
    }

    /**
     * 添加图片
     * @param detailId
     * @return
     */
    @PostMapping("/images/add")
    public Result addImages(@RequestParam("detailId") String detailId, @RequestParam("images")List<MultipartFile> images){
        if (detailId == null || images == null){
            return Result.error("上传失败");
        }
        return locationService.addImages(detailId, images);
    }
    /***
     * 添加视频
     * @param detailId
     */
    @PostMapping("/videos/add")
    public Result addVideos(@RequestParam("detailId") String detailId, @RequestParam("videos")List<MultipartFile> videos){
        if (detailId == null || videos == null){
            return Result.error("上传失败");
        }
        return locationService.addVideos(detailId, videos);
    }
    /**
     * 删除图片
     * @param imageUrl
     */
    @PostMapping("/images/delete")
    public Result deleteImage(String imageUrl){
        if (imageUrl == null){
            return Result.error("删除失败");
        }
        return locationService.deleteImage(imageUrl);
    }

    @PostMapping("/videos/delete")
    public Result deleteVideo(String videoUrl){
        if (videoUrl == null){
            return Result.error("删除失败");
        }
        return locationService.deleteVideo(videoUrl);
    }
}
