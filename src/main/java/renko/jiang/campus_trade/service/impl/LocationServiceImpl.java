package renko.jiang.campus_trade.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import renko.jiang.campus_trade.controller.admin.pojo.dto.DetailCommentDTO;
import renko.jiang.campus_trade.controller.admin.pojo.dto.LocationDTO;
import renko.jiang.campus_trade.controller.admin.pojo.entity.DetailComment;
import renko.jiang.campus_trade.controller.admin.pojo.entity.Location;
import renko.jiang.campus_trade.controller.admin.pojo.entity.LocationDetail;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationDetailVO;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationVO;
import renko.jiang.campus_trade.mapper.LocationMapper;
import renko.jiang.campus_trade.pojo.result.Result;
import renko.jiang.campus_trade.service.LocationService;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 86132
 */
@Service
public class LocationServiceImpl implements LocationService {
    @Autowired
    private LocationMapper locationMapper;
    @Override
    public Result<List<LocationVO>> getAllLocations() {
        List<Location> locations = locationMapper.getAllLocations();


        List<LocationVO> locationVOS = new ArrayList<>();
        if (locations != null) {
            //将Location对象转换为LocationVO对象
            for (Location location : locations){
                LocationVO locationVO = new LocationVO();
                BeanUtils.copyProperties(location,locationVO);
                locationVO.setCoords(new Double[]{location.getX(),location.getX()});
                locationVOS.add(locationVO);
            }
            return Result.success(locationVOS);
        }
        return Result.error("获取失败");
    }

    @Override
    public Result<LocationDetailVO> getDetailById(String detailId) {
        LocationDetail locationDetail = locationMapper.getDetailById(detailId);
        if (locationDetail == null){
            return Result.error("获取失败");
        }
        List<String> images = locationMapper.getImagesById(locationDetail.getImageId());
        List<String> videos = locationMapper.getVideosById(locationDetail.getVideoId());
        LocationDetailVO locationDetailVO = new LocationDetailVO();
        locationDetailVO.setImages(images);
        locationDetailVO.setVideos(videos);

        return Result.success(locationDetailVO);
    }

    @Override
    public LocationVO getLocationById(Long id) {
        return null;
    }

    @Override
    public LocationVO createLocation(LocationDTO locationDTO) {
        return null;
    }

    @Override
    public LocationVO updateLocation(Long id, LocationDTO locationDTO) {
        return null;
    }

    @Override
    public void deleteLocation(Long id) {

    }

    /***
     * 根据detailId获取评论
     * @param detailId
     * @return
     */
    @Override
    public Result<List<DetailComment>> getCommentsById(String detailId) {
        List<DetailComment> detailComments = locationMapper.getCommentsById(detailId);
        if (detailComments != null){
            //获取评论用户名
            for (DetailComment detailComment : detailComments){
                String username = detailComment.getUsername();
                String nickName = locationMapper.getNickNameByUsername(username);
                detailComment.setUsername(nickName);
            }
            return Result.success(detailComments);
        }
        return Result.error("获取失败");
    }

    @Override
    public Result<String> submitComment(DetailCommentDTO detailCommentDTO) {
        String username = locationMapper.getUsernameByUserId(detailCommentDTO.getUserId());
        String nickName = locationMapper.getNickNameByUsername(username);
        DetailComment detailComment = new DetailComment();
        detailComment.setDetailId(detailCommentDTO.getDetailId());
        detailComment.setUsername(username);
        detailComment.setContent(detailCommentDTO.getContent());

        if (locationMapper.submitComment(detailComment) > 0){
            return Result.success(nickName);
        }
        return Result.error("提交失败");
    }
}
