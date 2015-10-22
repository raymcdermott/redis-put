package com.opengrail.redis_put;

import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ray on 23/10/15.
 */
public class RedisPut {

    public static void main(final String args[]) {

        if (args.length != 1) {
            System.err.println("You must provide the dyno IP");
            System.exit(1);
        }

        final String dynoIpAddress = args[0];

        try {
            final InetAddress inetAddress = InetAddress.getByName(dynoIpAddress);
            if (!inetAddress.isReachable(1000)) {
                System.err.println("Cannot connect to the dyno IP within 1 second");
                System.exit(1);
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        final String redisUrl = System.getenv("REDIS_URL");

        if (redisUrl == null || redisUrl.trim().isEmpty()) {
            System.err.println("REDIS_URL must be set correctly");
            System.exit(1);
        }

        final Jedis jedis = new Jedis(redisUrl);

        jedis.set("datomic-IP", dynoIpAddress);

        final String ip = jedis.get("datomic-IP");

        System.out.println("IP Set in REDIS = " + ip);
    }
}

