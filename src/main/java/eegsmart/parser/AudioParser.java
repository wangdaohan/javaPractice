package eegsmart.parser;

/**
 * Created by patrick on 2016/3/10.
 */

import eegsmart.Track;
import eegsmart.utils.ExportUtils;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AudioParser {

    /**
     * @param args
     */
    public static void main(String[] args) {

        String fileLocation = "D:/Age of Innocence.mp3";
        File dir = new File("D:/relax");
        String URI = "http://7xp15a.com2.z0.glb.qiniucdn.com/meditation%2F";
        //http://7xp15a.com2.z0.glb.qiniucdn.com/meditation%2F
        List<Track> trackList = new ArrayList<Track>();
        try {
            for (File file : dir.listFiles()) {
                InputStream input = new FileInputStream(file);
                ContentHandler handler = new DefaultHandler();
                Metadata metadata = new Metadata();
                Parser parser = new Mp3Parser();
                ParseContext parseCtx = new ParseContext();
                parser.parse(input, handler, metadata, parseCtx);
                input.close();

                // List all metadata
//                String[] metadataNames = metadata.names();
////
//                for (String name : metadataNames) {
//                    System.out.println(name + ": " + metadata.get(name));
//                }

                // Retrieve the necessary info from metadata
                // Names - title, xmpDM:artist etc. - mentioned below may differ based
                // System.out.println("----------------------------------------------");
                Track track = new Track();
                track.setName(metadata.get("title"));
                track.setSinger(metadata.get("xmpDM:artist"));
                track.setGenre(metadata.get("xmpDM:genre"));
                track.setAlbum(metadata.get("xmpDM:album"));
                track.setName(metadata.get("title"));
                Long duration = Double.valueOf(metadata.get("xmpDM:duration")).longValue();
                DateFormat formatter = new SimpleDateFormat("mm:ss");
                Date d = new Date(duration);
                track.setDuration(formatter.format(d));
                track.setPlayURL(URI + file.getName() );
                track.setTag("MEDITATION");
                trackList.add(track);
            }

            ExportUtils.exportToExcel(new String[]{"1","2","3","4","5"},trackList,"");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        }
    }
}
