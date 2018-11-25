/**
 * Created by Vedant on 11/24/18.
 */
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;

import org.graphstream.graph.*;
import org.graphstream.stream.file.FileSinkImages;
import org.graphstream.stream.file.FileSinkImages.*;

import javax.imageio.ImageIO;
import java.io.IOException;

public class GraphImageManager {
    public void saveGraph(Graph graph, String imageName) {
        FileSinkImages fsi = new FileSinkImages("simples", OutputType.JPG, Resolutions.HD720, OutputPolicy.BY_STEP);
        fsi.setLayoutPolicy(LayoutPolicy.COMPUTED_FULLY_AT_NEW_IMAGE);

        fsi.setResolution(200, 200);
        graph.addSink(fsi);

        try {
            fsi.writeAll(graph, "graphImages/" + imageName + ".png");
        } catch (IOException ioe) {
            System.out.println("Error in Export-Saving!");
        }
    }

    public void concatenateImages(int columnMax) {
        BufferedImage concatImage;

        Graphics g2d;

        ArrayList<BufferedImage> graphImages;
        graphImages = new ArrayList<BufferedImage>();

        int fileCount;
        fileCount = 0;

        for (File file : new File("graphImages/").listFiles()) {
            BufferedImage currentGraphImage;
            currentGraphImage = new BufferedImage(2, 2, BufferedImage.TYPE_3BYTE_BGR);

            try {
                currentGraphImage = ImageIO.read(new File("graphImages/" + file.getName()));
            }catch (IOException ioe) {

            }

            new File("graphImages/" + file.getName()).delete();

            graphImages.add(currentGraphImage);
        }

        int xBound;
        int yBound;

        xBound = (graphImages.size() > columnMax)? 800: graphImages.size() * 200;
        yBound = 200 * (int)Math.ceil((double)graphImages.size() / columnMax);

        concatImage = new BufferedImage(xBound, yBound, BufferedImage.TYPE_INT_RGB);

        g2d = concatImage.createGraphics();

        g2d.setColor(Color.WHITE);

        g2d.fillRect(0,0, xBound, yBound);

        g2d.setColor(Color.BLACK);

        for(int i = 0; i < graphImages.size(); i++) {
            int currentX;
            int currentY;

            currentX = 200 * ((i) % columnMax);
            currentY = (int)(i / columnMax) * 200;

            g2d.drawImage(graphImages.get(i), currentX, currentY, null);

            if(currentX != 0) {
                g2d.drawLine(currentX, currentY + 20, currentX, currentY + 180);
            }

            if(currentY != 0) {
                g2d.drawLine(currentX + 20, currentY, currentX + 180, currentY);
            }
        }

        g2d.dispose();

        try {
            ImageIO.write(concatImage, "png", new File("graphs.png"));
        }catch (IOException ioe) {
            System.out.println("Error in Concatenating the Graphs Together!");
        }
    }
}
