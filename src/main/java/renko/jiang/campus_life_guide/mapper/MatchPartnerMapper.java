package renko.jiang.campus_life_guide.mapper;

import org.apache.ibatis.annotations.*;
import renko.jiang.campus_life_guide.pojo.entity.Interest;
import renko.jiang.campus_life_guide.pojo.entity.MatchPartner;
import renko.jiang.campus_life_guide.pojo.entity.UserMatch;
import renko.jiang.campus_life_guide.pojo.vo.InterestVO;

import java.util.List;

/**
 * @author 86132
 */

@Mapper
public interface MatchPartnerMapper {

    @Select("select id,name from interest")
    List<InterestVO> getInterests();

    List<MatchPartner> getList(Integer interestId);

    @Insert("insert into match_partner(publisher_id,interest_id,description,limited,current_num) " +
            "values(#{publisherId},#{interestId},#{description},#{limited},#{currentNum})")
    void publish(MatchPartner matchPartner);

    @Select("select * from match_partner where publisher_id = #{currentUserId}")
    List<MatchPartner> getMyList(Integer currentUserId);

    @Select("select * from match_partner where id in (select match_id from user_match where user_id = #{currentUserId})")
    List<MatchPartner> getMyJoinList(Integer currentUserId);

    @Select("select name from interest where id = #{id}")
    String getInterestById(Integer id);

    List<Interest> getInterestsByIds(List<Integer> interestIds);

    List<UserMatch> judgeJoined(Integer currentUserId, List<Integer> matchIds);

    @Insert("insert into user_match(user_id,match_id) values(#{currentUserId},#{matchId})")
    int join(Integer currentUserId, Integer matchId);

    @Delete("delete from match_partner where id = #{matchId}")
    int unpublish(Integer matchId);

    @Delete("delete from user_match where match_id = #{matchId}")
    int deleteUserMatch(Integer matchId);

    @Update("update match_partner set current_num = current_num + 1 where id = #{matchId} and current_num < limited")
    int addCurrentNum(Integer matchId);

    @Update("update match_partner set current_num = current_num - 1 where id = #{matchId}")
    int subCurrentNum(Integer matchId);

    @Delete("delete from user_match where user_id = #{currentUserId} and match_id = #{matchId}")
    int quit(Integer currentUserId, Integer matchId);

    @Select("select current_num >= limited as full from match_partner where id = #{matchId}")
    Boolean judgeFull(Integer matchId);

    @Select("select user_id from user_match where match_id = #{matchId}")
    List<Integer> getUserIdsByMatchId(Integer matchId);

    @Select("select publisher_id,interest_id from match_partner where id = #{matchId}")
    MatchPartner getOwnerId(Integer matchId);
}
