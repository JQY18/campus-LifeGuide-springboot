package renko.jiang.campus_life_guide.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

/**
 * @author 86132
 */

@Mapper
public interface StatisticsMapper {

    @Select("select count from statistics where date = #{date}")
    Long getCount(LocalDate date);

    @Insert("insert into statistics (date,count) value (#{date},1)")
    Integer addVisit(LocalDate date);

    @Update("update statistics set count = count + 1 where date = #{date}")
    Integer update(LocalDate date);

    @Select("select sum(count) from statistics where date >= #{from}")
    Long getWeekCount(LocalDate from);

    @Select("select sum(count) from statistics")
    Long getAllCount();

}
