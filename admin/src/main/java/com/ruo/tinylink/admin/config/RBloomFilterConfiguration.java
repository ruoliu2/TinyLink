package com.ruo.tinylink.admin.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "rBloomFilterConfigurationByAdmin")
public class RBloomFilterConfiguration {

  /** User register cache penetration bloom filter */
  @Bean
  public RBloomFilter<String> userRegisterCachePenetrationBloomFilter(
      RedissonClient redissonClient) {
    RBloomFilter<String> cachePenetrationBloomFilter =
        redissonClient.getBloomFilter("userRegisterCachePenetrationBloomFilter");
    cachePenetrationBloomFilter.tryInit(100000000L, 0.001); // expectedInsertions, fpp
    return cachePenetrationBloomFilter;
  }
}
