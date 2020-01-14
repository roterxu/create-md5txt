package com.example.createmd5txt.controller;

import com.example.createmd5txt.service.BashService;
import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author xujie
 */
@Controller
@Slf4j
public class CreateController {

    @Resource
    private BashService bashService;


    @RequestMapping("/uniq")
    @ResponseBody
    public String uniq() throws InterruptedException, ExecutionException, IOException {
        long total = 3000000000L;
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total);

        String plainPath = "/mnt/d6/phone-sha256/result.txt";

        String resultPath = "/mnt/d6/phone-sha256/result3.txt";

        AtomicLong count = new AtomicLong(0L);

        long size = 1585070105L;
        log.info("开始读取要解密文件");
        bashService.resolve(plainPath, 5000, lines -> {
            for (String line : lines) {
                bf.put(line);
            }
            count.addAndGet(5000);
            if (Long.parseLong(count.toString()) >= size) {
                return;
            }
            bashService.write(resultPath, lines);
            lines.clear();
        });

        return null;
    }

    @RequestMapping("/testBloom")
    @ResponseBody
    public void testBloom() {
        log.info("testBloom");
    }


    @RequestMapping("/initTxt")
    @ResponseBody
    public void createTxt() throws IOException {
        List<Integer> phones = Arrays.asList(
                130, 131, 132, 133, 134, 135,
                136, 137, 138, 139, 141, 145,
                146, 147, 149, 150, 151, 152,
                153, 155, 156, 157, 158, 159,
                166, 170, 171, 172, 173, 174,
                175, 176, 177, 178, 180, 181,
                182, 183, 184, 185, 186, 187,
                188, 189, 198, 199, 191, 162);
//        String path = "/mnt/d6/phone-sha256/phone-sha256.txt";

        String path = "C:\\tmp/phone-sha256.txt";
        for (Integer phone : phones) {
            String head = String.valueOf(phone);
            long start = Long.parseLong(head + "00000000");
            long end = Long.parseLong(head + "99999999");
            List<String> phoneStr = new ArrayList<>();
            for (long i = start; i <= end; i++) {
                phoneStr.add(String.valueOf(i));
                if (i % 5000 == 0 || i == end) {
                    bashService.write(path, phoneStr, DigestUtils::sha256Hex);
                    phoneStr.clear();
                }
            }
        }
    }


    @RequestMapping("/decryptFile")
    @ResponseBody
    public String decryptFile() throws InterruptedException, ExecutionException, IOException {

        //待解密文件  15E
        String sourcePath = "/mnt/d6/result.log";
        //明文 + 密文   47E
        String plainPath = "/mnt/d6/phone-sha256/final-result.txt";
        //结果文件
        final String resultPath = "/mnt/d6/phone-sha256/final-result2.txt";
        long total = 3000000000L;
        //初始化布隆过滤器  长度30E
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total);

        //读待解密文件result.log
        bashService.resolve(sourcePath, 5000, lines -> {
            for (String line : lines) {
                bf.put(line);
            }
        });
        log.info("布隆过滤器初始化完成!!");
        List<String> phones = new ArrayList<>();
        AtomicLong atomicLong=new AtomicLong();
        bashService.resolve(plainPath, 5000, lines -> {
            for (String line : lines) {
                //读取phone-sha256.txt  匹配成功加入数组
                if (bf.mightContain(line.split(",")[1])) {
                    phones.add(line);
                }
            }

            //匹配成功写入文件final-result.txt
            bashService.write(resultPath, phones);
            atomicLong.addAndGet(5000);
            log.info("匹配进度:{}",atomicLong.get());
            phones.clear();
        });

        return "文件解密成功";
    }

}
