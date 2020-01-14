package com.example.createmd5txt;

import com.example.createmd5txt.service.BashService;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import sun.rmi.runtime.Log;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@SpringBootTest
@Slf4j
class CreateMd5txtApplicationTests {
    @Resource
    private BashService bashService;

    @Test
    public void contextLoads() throws InterruptedException, ExecutionException, IOException {
        String plainPath = "C:\\tmp/phone-sha256.txt";

        String sourcePath = "C:\\tmp/phone-sha256 - 副本.txt";

        String newPath = "C:\\tmp/phone.txt";
        Long total = 3000000L;

        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total);

//        bashService.resolve(sourcePath, 5000, lines -> {
//            for (int i = 0; i < lines.size(); i++) {
//                bf.put(lines.get(i));
//            }
//        });
//
//        List<String> phones = new ArrayList<>();
//        bashService.resolve(plainPath, 5000, lines -> {
//            for (String line : lines) {
//                if (bf.mightContain(line)) {
//                    phones.add(line);
//                }
//            }
//            log.info("over!");
//            bashService.write(newPath, phones);
//            phones.clear();
//        });

        // 初始化 1000000 条数据到过滤器中

        // 判断值是否存在过滤器中


    }

}
