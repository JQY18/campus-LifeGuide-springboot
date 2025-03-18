package renko.jiang.campus_life_guide.controller.statistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import renko.jiang.campus_life_guide.mapper.StatisticsMapper;
import renko.jiang.campus_life_guide.pojo.result.Result;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 86132
 */

@CrossOrigin("*")
@RestController
@RequestMapping("/visit")
public class StatisticsController {
    @Autowired
    private StatisticsMapper statisticsMapper;

    /**
     * 获取访问量
     *
     * @return 访问量
     */
    @GetMapping("/all")
    public Result<Map<String, Long>> getAllVisit() {
        Long allCount = statisticsMapper.getAllCount();

        LocalDate to = LocalDate.now();

        int value = to.getDayOfWeek().getValue();

        LocalDate from = to.minusDays(value - 1);
        Long weekCount = statisticsMapper.getWeekCount(from);
        Map<String, Long> map = Map.of("visitCount", allCount, "visitTrend", weekCount);
        return Result.success(map);
    }

    @GetMapping("/week")
    public Result<List<Long>> getWeekVisit() {
        LocalDate to = LocalDate.now();

        int value = to.getDayOfWeek().getValue();

        LocalDate from = to.minusDays(value - 1);

        List<Long> yData = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            Long count = statisticsMapper.getCount(from.plusDays(i));
            if(count == null){
                yData.add(0L);
            }else{
                yData.add(count);
            }
        }
        return Result.success(yData);
    }
    @GetMapping("/month")
    public Result<List<Long>> getMonthVisit() {
        LocalDate to = LocalDate.now();

        int value = to.getDayOfMonth();

        LocalDate from = to.minusDays(value - 1);

        List<Long> yData = new ArrayList<>();
        for (int i = 0; i < value; i++) {
            Long count = statisticsMapper.getCount(from.plusDays(i));
            if(count == null){
                yData.add(0L);
            }else{
                yData.add(count);
            }
        }
        return Result.success(yData);
    }

}
