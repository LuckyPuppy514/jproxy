package com.lckp.jproxy.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于检查间隔时间
 *
 * @author 超级有节操的逆袭
 * @date 2023-04-09
 */
@Component
public class SyncIntervalComponent {

    @Value("${time.sync-interval}")
    private long syncInterval;


    private Map<String, Date> syncDateMap = new HashMap<>();


    //检查间隔时间够不够
    public Boolean checkInterval(String key) {

        Date lastDate = syncDateMap.get(key);
        Date now = new Date();
        if (lastDate == null) {
            syncDateMap.put(key, now);
            return true;
        }
        Long diff = now.getTime() - lastDate.getTime();
        if (diff > syncInterval * 1000 * 60) {
            syncDateMap.put(key, now);
            return true;
        }
        return false;
    }

}
