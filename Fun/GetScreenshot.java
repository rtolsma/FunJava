

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by ryan on 11/12/14.
 * Takes a screenshot with the specified bounds, and then writes it to the storage folder in the specified format
 */
public class GetScreenshot implements Runnable {
        String picName, picType;
    Dimension screenSize;
    int width, height;
    Rectangle bounds;
    Robot r;
    BufferedImage img;
    File imageFile;
        public GetScreenshot(String name, String imgType) {
             screenSize=Toolkit.getDefaultToolkit().getScreenSize();
           picType=imgType;
            width=(int)screenSize.getWidth();
            height=(int)screenSize.getHeight();
            picName=name;
            bounds=new Rectangle(width, height);
          try {
              r = new Robot();
                 } catch (AWTException e) {
              String[] error=new String[]{"AWTException in: "+getClass().getName() };
              new Thread(new MyException(e, error)).start();
          }

        }
    public GetScreenshot(String name, String imgType, int xOrigin, int yOrigin, int width, int height) {
        screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        picType=imgType;
       // width=(int)screenSize.getWidth();
        //height=(int)screenSize.getHeight();
        picName=name;
        bounds=new Rectangle(xOrigin, yOrigin, width, height);
        try {
            r = new Robot();
        } catch (AWTException e) {
            String[] error=new String[]{"AWTException in: "+getClass().getName() };
            new Thread(new MyException(e, error)).start();
        }

    }
    public GetScreenshot(String name, String imgType, int width, int height) {
        screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        picType=imgType;
       // width=(int)screenSize.getWidth();
        //height=(int)screenSize.getHeight();
        picName=name;
        bounds=new Rectangle(width, height);
        try {
            r = new Robot();
        } catch (AWTException e) {

                String[] error=new String[]{"AWTException in: "+getClass().getName() };
                new Thread(new MyException(e, error)).start();

        }

    }

                public void run() {
                    img=getPic(r, bounds);
                    writeImage(img, picName, picType);


            }

    public BufferedImage getPic(Robot robo, Rectangle rect) {
      return  robo.createScreenCapture(rect);
    }

        public void writeImage(BufferedImage pic, String name, String imgType) {
       try {
           if(Startup.isWindows()) {
           imageFile = new File(Startup.getFolderPath()+"\\" + name + "." + imgType);
           } else  { imageFile=new File(Startup.getFolderPath()+"/"+name+"."+imgType);}

           ImageIO.write(pic, imgType, imageFile);

       } catch (IOException e)  {
           String[] error=new String[]{"IOException in: "+getClass().getName() };
           new Thread(new MyException(e, error)).start();
       }
    }
}
