package renko.jiang.campus_trade.service;


import renko.jiang.campus_trade.controller.admin.pojo.dto.DetailCommentDTO;
import renko.jiang.campus_trade.controller.admin.pojo.dto.LocationDTO;
import renko.jiang.campus_trade.controller.admin.pojo.entity.DetailComment;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationDetailVO;
import renko.jiang.campus_trade.controller.admin.pojo.vo.LocationVO;
import renko.jiang.campus_trade.pojo.result.Result;

import java.util.List;

/**
 * @author 86132
 */
public interface LocationService {
    Result<List<LocationVO>> getAllLocations();

    Result<LocationDetailVO> getDetailById(String detailId);

    LocationVO getLocationById(Long id);

    LocationVO createLocation(LocationDTO locationDTO);

    Result updateLocation(LocationDTO locationDTO);

    Result deleteLocation(Long id);

    Result<List<DetailComment>> getCommentsById(String detailId);

    Result<String> submitComment(DetailCommentDTO detailCommentDTO);

    Result addLocation(LocationDTO locationDTO);
}