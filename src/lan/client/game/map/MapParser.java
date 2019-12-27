package lan.client.game.map;

import com.alibaba.fastjson.JSON;

import java.awt.*;
import java.io.*;

public class MapParser {//µØÍ¼½âÎö
    private String jsonFile;

    public MapParser(String jsonName) {
        jsonFile = jsonName;
    }

    public Image[][] parse() {
        String jsonStr = readJsonData(jsonFile);
        Map map = JSON.parseObject(jsonStr, Map.class);
        String imageName = map.getImage();
        imageName = getImagePath(jsonFile, imageName);
        Tileset tileset = new Tileset();
        tileset.load(imageName, map.getTileWidth(), map.getTileHeight());

        int rows = map.getRows();
        int cols = map.getCols();
        Image[][] images = new Image[rows][];
        int[] data = map.getData();
        for(int i=0;i<rows;i++) {
            images[i] = new Image[cols];
            for(int j=0;j<cols;j++) {
                int index = i*cols + j;
                if(index >= data.length)
                    break;

                int imageId = data[index];
                images[i][j] = tileset.getImage(imageId);
            }
        }
        return images;
    }

    private String getImagePath(String jsonFile, String imageName) {
        int index = jsonFile.lastIndexOf('\\');
        if(index == -1) {
            index = jsonFile.lastIndexOf('/');
            if (index == -1)
                return "";
        }
        return jsonFile.substring(0, index+1) + imageName;
    }

    public String readJsonData(String pactFile) {
        StringBuffer strbuffer = new StringBuffer();
        InputStream inputStream = this.getClass().getResourceAsStream(pactFile);
        if(inputStream == null) {
            System.out.println("File is not found " + pactFile);
            return "";
        }
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        }

        return strbuffer.toString();
    }
}
