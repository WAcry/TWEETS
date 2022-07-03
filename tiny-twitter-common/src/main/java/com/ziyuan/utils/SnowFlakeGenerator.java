package com.ziyuan.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Snowflake ID generator
 **/
@Component
@Slf4j
public class SnowFlakeGenerator {

    /**
     * time start
     */
    private final long START_STAMP = 1641013200000L; // 2022-01-01 00:00:00

    /**
     * bits of each part
     * seq_bit + mac_bit + dat_bit + timestamp_bit_diff(=40) < 63 bit (2^63 - 1 = LONG_MAX)
     */
    @Value("${snowflake.sequenceBit}")
    private long SEQUENCE_BIT; // bits of sequence
    /**
     * shift of each part
     */
    private final long MACHINE_LEFT = SEQUENCE_BIT;
    @Value("${snowflake.machineBit}")
    private long MACHINE_BIT;   // bits of machine

    private final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);
    private final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    @Value("${snowflake.datacenterBit}")
    private long DATACENTER_BIT;// bits of datacenter
    private final long TIMESTAMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    @Value("${snowflake.datacenterId}")
    private long datacenterId;  // datacenter id
    @Value("${snowflake.machineId}")
    private long machineId;     // machine id
    private long sequence = 0L; // init seq number
    private long lastStmp = -1L;// last timestamp

    public final static String SID_MAX = "zzzzzzzz";
    public final static String SID_MIN = "00000000";


    private SnowFlakeGenerator() {
    }

    /**
     * next UUID
     *
     * @return
     */
    public synchronized String nextUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public SnowFlakeGenerator(long datacenterId, long machineId) {
        /*
         * max value of each part
         */
        long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    public static void main(String[] args) throws ParseException {
        System.out.println(System.currentTimeMillis());
        System.out.println(new Date().getTime());
        String dateTime = "2021-01-01 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        System.out.println(sdf.parse(dateTime).getTime());


        SnowFlakeGenerator snowflake = new SnowFlakeGenerator(1, 1);
        snowflake.SEQUENCE_BIT = 10;
        snowflake.MACHINE_BIT = 5;
        snowflake.DATACENTER_BIT = 5;
        for (int i = 0; i < 100; i++) {
            System.out.println(snowflake.nextSID());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    /**
     * next snowflake id has length of 8
     *
     * @return
     */
    public synchronized String nextSID() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            // seq += 1 in the same timestamp
            sequence = (sequence + 1) & MAX_SEQUENCE;
            // seq reach the max value in the same millis, so we borrow from the next millis
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //reset sequence for new millisecond
            sequence = 0L;
        }

        lastStmp = currStmp;

        long sid = (currStmp - START_STAMP) << TIMESTAMP_LEFT // time stamp
                | datacenterId << DATACENTER_LEFT       // datacenter id
                | machineId << MACHINE_LEFT             // machine id
                | sequence;                             // sequence

        String sidStr = new BigInteger(String.valueOf(sid)).toString(36);
        while (sidStr.length() < 8) {
            sidStr = "0" + sidStr;
        }

        if (sidStr.length() > 8) {
            log.error("sidStr length is greater than 8");
            return null;
        }
        return sidStr;
    }

}