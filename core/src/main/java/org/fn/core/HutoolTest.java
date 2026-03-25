package org.fn.core;

import cn.hutool.bloomfilter.BitMapBloomFilter;
import cn.hutool.bloomfilter.BitSetBloomFilter;
import cn.hutool.bloomfilter.BloomFilter;
import cn.hutool.bloomfilter.BloomFilterUtil;

/**
 * @author chenshoufeng
 * @since 2026/3/24 下午4:38
 **/
public class HutoolTest {
    public static void main(String[] args) {
        int c = 160000;
        int n = 10000;
        int k = 8;

        // 创建布隆过滤器
        BloomFilter filter = BloomFilterUtil.createBitSet(c, n, k);

        System.out.println("正在添加 10000 条数据到布隆过滤器...");
        for (int i = 0; i < 10000; i++) {
            filter.add("user-id-" + i);
        }
        System.out.println("10000 条数据添加完成！\n");

        long bitSetSize = (long) c * k;
        long byteSize = bitSetSize / 8;
        long kbSize = byteSize / 1024;


        System.out.println("按照源码计算：");
        System.out.println("bitSetSize = " + bitSetSize + " bit");
        System.out.println("占用内存 = " + kbSize + " KB");
    }
}
