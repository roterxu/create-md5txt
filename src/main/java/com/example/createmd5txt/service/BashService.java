package com.example.createmd5txt.service;

import com.example.createmd5txt.controller.FileResolveTemplate;
import com.example.createmd5txt.controller.LineFormatTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author chenpeng
 * @date 2018/11/2
 */
@Service
@Slf4j
public class BashService {


    public void resolve(String filePath, int batchSize, FileResolveTemplate fileResolveTemplate) throws IOException, ExecutionException, InterruptedException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)))) {
            int count = 0;
            String line;
            List<String> tmpList = new ArrayList<>();
            while ((line = bufferedReader.readLine()) != null) {
                count++;
                tmpList.add(line);
                if (count % batchSize == 0) {
                    fileResolveTemplate.resolve(tmpList);
                    tmpList.clear();
                }
            }
            if (tmpList.size() > 0) {
                fileResolveTemplate.resolve(tmpList);
            }
        }

    }



    public void write(String filePath, List<String> lines, LineFormatTemplate lineFormatTemplate) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath), true))) {
            int count = 0;
            for (String line : lines) {
                String result = lineFormatTemplate.format(line);
                if (result == null) {
                    continue;
                }
                count++;
                bufferedWriter.write(result);
                if (count % 5000 == 0) {
                    bufferedWriter.flush();
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        }
    }


    public void write(String filePath, List<String> lines) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(filePath), true))) {
            int count = 0;
            for (String line : lines) {
                if (line == null) {
                    continue;
                }
                count++;
                bufferedWriter.write(line);
                if (count % 5000 == 0) {
                    bufferedWriter.flush();
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
        }
    }



}
