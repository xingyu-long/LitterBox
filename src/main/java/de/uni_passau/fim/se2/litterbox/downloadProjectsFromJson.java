package de.uni_passau.fim.se2.litterbox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.uni_passau.fim.se2.litterbox.utils.Downloader;

public class downloadProjectsFromJson {


    public static Map<String, List<String>> readJSON(String filePath) throws IOException{
        byte[] mapData = Files.readAllBytes(Paths.get(filePath));
        Map<String, List<String>> map = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        map = objectMapper.readValue(mapData, HashMap.class);
        return map;
    }

    public static void downloadPRojectByBatch(Map<String, List<String>> map, int batchNum, String outputPath)
            throws IOException, InterruptedException {
        int count = 0;
        for (String category : map.keySet()) {
            List<String> projectIds = map.get(category);
            for (String id : projectIds) {
                if (id == null) continue;
                if (count % batchNum == 0) {
                    // sleep 1000ms;
                    TimeUnit.SECONDS.sleep(1);
                } else {
                    String json = Downloader.downloadProjectJSON(id);
                    Downloader.saveDownloadedProject(json, id, outputPath + "/" + category + "/");
                }
                System.out.println(id);
                count++;
            }
        }
        System.out.println("count = " + count);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        String filePath = "/Users/clarklong/Desktop/Github/test_download/dataset.json";
        String outputPath = "/Users/clarklong/Desktop/Github/test_download/output";
        int batchNum = 10;
        Map<String, List<String>> map = readJSON(filePath);
        downloadPRojectByBatch(map, batchNum, outputPath);
    }    
}
