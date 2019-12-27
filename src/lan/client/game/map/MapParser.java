package lan.client.game.map;

import com.alibaba.fastjson.JSON;

import java.awt.*;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;

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
        URL fileUrl = MapParser.class.getClassLoader().getResource(pactFile);
        if(fileUrl == null) {
            fileUrl = this.getClass().getClassLoader().getResource(pactFile);
            if(fileUrl == null) {
                System.out.println("File is not found " + pactFile);
                return "";
            }
        }
        try {
            File file = new File(fileUrl.toURI());
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader in  = new BufferedReader(inputStreamReader);

            String str;
            while ((str = in.readLine()) != null) {
                strbuffer.append(str);
            }
            in.close();
        } catch (IOException e) {
            e.getStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return strbuffer.toString();
    }
}
