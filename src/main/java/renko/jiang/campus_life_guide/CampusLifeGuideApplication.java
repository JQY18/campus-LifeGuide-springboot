package renko.jiang.campus_life_guide;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author 86132
 */

@EnableTransactionManagement
@EnableCaching
@EnableAsync
@SpringBootApplication
public class CampusLifeGuideApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampusLifeGuideApplication.class, args);
    }

}
