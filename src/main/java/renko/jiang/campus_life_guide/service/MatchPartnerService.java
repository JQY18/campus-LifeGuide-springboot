package renko.jiang.campus_life_guide.service;

import renko.jiang.campus_life_guide.pojo.dto.MatchPartnerDTO;
import renko.jiang.campus_life_guide.pojo.entity.MatchPartner;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.InterestVO;
import renko.jiang.campus_life_guide.pojo.vo.MatchPartnerVO;

import java.util.List;

/**
 * @author 86132
 */
public interface MatchPartnerService {
    Result<List<InterestVO>> getInterests();

    Result publish(MatchPartnerDTO matchPartnerDTO);

    Result<List<MatchPartnerVO>> getList(Integer interestId);

    Result<List<MatchPartnerVO>> getMyList();

    Result<List<MatchPartnerVO>> getMyJoinList();

    Result join(Integer matchId);

    Result unpublish(Integer matchId);

    Result quit(Integer matchId);

    String getInterestNameById(Integer interestId);
}
