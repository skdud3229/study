package com.example.web_ide.service;

import com.example.web_ide.domain.dao.Board;
import com.example.web_ide.exception.RedisDataAccessException;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RedisViewLikesServiceImpl implements ViewLikesService {
    private final StringRedisTemplate redisTemplate;
    private final ValueOperations<String,String> valueOps;
    private final SetOperations<String,String> setOps;
    private static final String BOARD="board:";
    private static final String VIEW_COUNT=":viewCount";
    private static final String VIEW_IPS=":viewIPs";


    public RedisViewLikesServiceImpl(StringRedisTemplate redisTemplate){
        this.redisTemplate=redisTemplate;
        this.valueOps=redisTemplate.opsForValue();
        this.setOps=redisTemplate.opsForSet();
    }

    @Override
    public long addViewCount(Board board, String ip) {
        String viewCountKey=BOARD+board.getId()+VIEW_COUNT;
        String viewIPKey=BOARD+board.getId()+VIEW_IPS;

        loadIfAbsent(viewCountKey,Long.toString(board.getViews()));

        if(addIfExist(viewIPKey,ip)){
            return increment(viewCountKey);
        }
        return Long.parseLong(get(viewCountKey));
    }

    @Override
    public Optional<Long> getViewCount(Long boardId){
        return Optional.ofNullable(valueOps.get(BOARD+boardId+VIEW_COUNT)).map(Long::parseLong);
    }

    private void loadIfAbsent(String key,String value){
        Boolean result=valueOps.setIfAbsent(key,value);
        if(result==null)    throw new RedisDataAccessException();
    }

    private boolean addIfExist(String key,String value){
        Long result=setOps.add(key,value);
        if(result==null)    throw new RedisDataAccessException();
        return result==1;
    }

    private Long increment(String key){
        Long result=valueOps.increment(key);
        if(result==null)    throw new RedisDataAccessException();
        return result;
    }

    private String get(String value){
        String result=valueOps.get(value);
        if(result==null)    throw new RedisDataAccessException();
        return result;
    }
}
