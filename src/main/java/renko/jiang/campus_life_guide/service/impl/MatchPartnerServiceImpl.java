package renko.jiang.campus_life_guide.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.BooleanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import renko.jiang.campus_life_guide.context.UserContextHolder;
import renko.jiang.campus_life_guide.mapper.MatchPartnerMapper;
import renko.jiang.campus_life_guide.pojo.dto.GroupChatDTO;
import renko.jiang.campus_life_guide.pojo.dto.MatchPartnerDTO;
import renko.jiang.campus_life_guide.pojo.entity.ChatRoom;
import renko.jiang.campus_life_guide.pojo.entity.Interest;
import renko.jiang.campus_life_guide.pojo.entity.MatchPartner;
import renko.jiang.campus_life_guide.pojo.entity.UserMatch;
import renko.jiang.campus_life_guide.pojo.result.Result;
import renko.jiang.campus_life_guide.pojo.vo.InterestVO;
import renko.jiang.campus_life_guide.pojo.vo.MatchPartnerVO;
import renko.jiang.campus_life_guide.service.ChatRoomService;
import renko.jiang.campus_life_guide.service.MatchPartnerService;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author 86132
 */
@Service
public class MatchPartnerServiceImpl implements MatchPartnerService {

    @Autowired
    private MatchPartnerMapper matchPartnerMapper;

    @Autowired
    private ChatRoomService chatRoomService;


    @Override
    public Result<List<InterestVO>> getInterests() {
        List<InterestVO> interests = matchPartnerMapper.getInterests();
        return Result.success(interests);
    }

    @Override
    public Result publish(MatchPartnerDTO matchPartnerDTO) {
        Integer currentUserId = UserContextHolder.getUserId();
        MatchPartner matchPartner = BeanUtil.copyProperties(matchPartnerDTO, MatchPartner.class);
        matchPartner.setPublisherId(currentUserId);
        matchPartner.setCurrentNum(0);
        matchPartnerMapper.publish(matchPartner);
        return Result.success();
    }

    @Override
    public Result<List<MatchPartnerVO>> getList(Integer interestId) {
        Integer currentUserId = UserContextHolder.getUserId();

        List<MatchPartner> list = matchPartnerMapper.getList(interestId);

        if(list.isEmpty()){
            return Result.success(Collections.emptyList());
        }

        //查询是否已经加入
        List<Integer> matchIds = list.stream().map(MatchPartner::getId).toList();
        List<UserMatch> joined = matchPartnerMapper.judgeJoined(currentUserId, matchIds);
        Map<Integer, Boolean> joinedMap = joined.stream()
                .collect(Collectors.toMap(UserMatch::getMatchId, userMatch -> true));

        //查询兴趣名称
        List<Integer> interestIds = list.stream().map(MatchPartner::getInterestId).toList();

        List<Interest> interestsByIds = matchPartnerMapper.getInterestsByIds(interestIds);
        Map<Integer, String> interestMap = interestsByIds.stream().collect(Collectors.toMap(Interest::getId, Interest::getName));


        List<MatchPartnerVO> collect = list.stream().map(matchPartner -> {
            MatchPartnerVO matchPartnerVO = BeanUtil.copyProperties(matchPartner, MatchPartnerVO.class);
            matchPartnerVO.setInterestName(interestMap.get(matchPartner.getInterestId()));
            matchPartnerVO.setJoined(joinedMap.getOrDefault(matchPartner.getId(), false));
            return matchPartnerVO;
        }).toList();
        return Result.success(collect);
    }

    @Override
    public Result<List<MatchPartnerVO>> getMyList() {
        Integer currentUserId = UserContextHolder.getUserId();
        if (currentUserId != null) {
            List<MatchPartner> list = matchPartnerMapper.getMyList(currentUserId);
            List<Integer> interestIds = list.stream().map(MatchPartner::getInterestId).toList();
            //查询兴趣名称
            if (interestIds.isEmpty()) {
                return Result.success(Collections.emptyList());
            }
            List<Interest> interests = matchPartnerMapper.getInterestsByIds(interestIds);

            Map<Integer, String> interestMap = interests.stream().collect(Collectors.toMap(Interest::getId, Interest::getName));

            List<MatchPartnerVO> collect = list.stream().map(matchPartner -> {
                MatchPartnerVO matchPartnerVO = BeanUtil.copyProperties(matchPartner, MatchPartnerVO.class);
                matchPartnerVO.setInterestName(interestMap.get(matchPartner.getInterestId()));
                return matchPartnerVO;
            }).toList();
            return Result.success(collect);
        }
        return Result.error("查询失败");
    }

    @Override
    public Result<List<MatchPartnerVO>> getMyJoinList() {
        Integer currentUserId = UserContextHolder.getUserId();
        if (currentUserId != null) {
            List<MatchPartner> list = matchPartnerMapper.getMyJoinList(currentUserId);
            List<Integer> interestIds = list.stream().map(MatchPartner::getInterestId).toList();
            //查询兴趣名称
            if (interestIds.isEmpty()) {
                return Result.success(Collections.emptyList());
            }
            List<Interest> interests = matchPartnerMapper.getInterestsByIds(interestIds);
            Map<Integer, String> interestMap = interests.stream().collect(Collectors.toMap(Interest::getId, Interest::getName));

            List<MatchPartnerVO> collect = list.stream().map(matchPartner -> {
                MatchPartnerVO matchPartnerVO = BeanUtil.copyProperties(matchPartner, MatchPartnerVO.class);
                matchPartnerVO.setInterestName(interestMap.get(matchPartner.getInterestId()));
                return matchPartnerVO;
            }).toList();
            return Result.success(collect);
        }
        return Result.error("查询失败");
    }

    @Transactional
    @Override
    public Result join(Integer matchId) {
        Integer currentUserId = UserContextHolder.getUserId();
        //修改当前匹配人数
        int update = matchPartnerMapper.addCurrentNum(matchId);
        if (update == 0) {
            throw new RuntimeException("匹配人数已满");
        }
        matchPartnerMapper.join(currentUserId, matchId);
        //判断是否人满，满了则将所有人拉入同一个群聊
        Boolean isFull = matchPartnerMapper.judgeFull(matchId);
        if(BooleanUtil.isTrue(isFull)){
            //从user_match中查出所有成员，建立群聊
            List<Integer> userIds = matchPartnerMapper.getUserIdsByMatchId(matchId);
            //群主
            MatchPartner matchPartner = matchPartnerMapper.getOwnerId(matchId);
            //群聊名字
            String name = matchPartnerMapper.getInterestById(matchPartner.getInterestId());
            chatRoomService.createGroupChatForMatch(matchPartner.getPublisherId(), userIds,name);
        }

        return Result.success();
    }


    @Transactional
    @Override
    public Result unpublish(Integer matchId) {
        matchPartnerMapper.unpublish(matchId);
        matchPartnerMapper.deleteUserMatch(matchId);
        return Result.success();
    }


    @Transactional
    @Override
    public Result quit(Integer matchId) {
        Integer currentUserId = UserContextHolder.getUserId();
        matchPartnerMapper.quit(currentUserId, matchId);
        matchPartnerMapper.subCurrentNum(matchId);
        return Result.success();
    }

    @Override
    public String getInterestNameById(Integer interestId) {
        return matchPartnerMapper.getInterestById(interestId);
    }

}
