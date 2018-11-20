import processing.core.PApplet;
import processing.core.PImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

final class Functions {
   private static final int COLOR_MASK = 0xffffff;
   private static final int KEYED_IMAGE_MIN = 5;
   private static final int KEYED_RED_IDX = 2;
   private static final int KEYED_GREEN_IDX = 3;
   private static final int KEYED_BLUE_IDX = 4;

   private static final String BGND_KEY = "background";


   private  static final String MINER_KEY = "miner";

   private static final String OBSTACLE_KEY = "obstacle";


   private static final String ORE_KEY = "ore";


   private static final String SMITH_KEY = "blacksmith";

   private static final String VEIN_KEY = "vein";

   private static final int PROPERTY_KEY = 0;


   public static void processImageLine(Map<String, List<PImage>> images,
                                       String line, PApplet screen) {
      String[] attrs = line.split("\\s");
      if (attrs.length >= 2) {
         String key = attrs[0];
         PImage img = screen.loadImage(attrs[1]);
         if (img != null && img.width != -1) {
            List<PImage> imgs = getImages(images, key);
            imgs.add(img);

            if (attrs.length >= KEYED_IMAGE_MIN) {
               int r = Integer.parseInt(attrs[KEYED_RED_IDX]);
               int g = Integer.parseInt(attrs[KEYED_GREEN_IDX]);
               int b = Integer.parseInt(attrs[KEYED_BLUE_IDX]);
               setAlpha(img, screen.color(r, g, b), 0);
            }
         }
      }
   }
   private static List<PImage> getImages(Map<String, List<PImage>> images,
                                 String key) {
      List<PImage> imgs = images.get(key);
      if (imgs == null) {
         imgs = new LinkedList<>();
         images.put(key, imgs);
      }
      return imgs;
   }
   private static void setAlpha(PImage img, int maskColor, int alpha)
   {
      int alphaValue = alpha << 24;
      int nonAlpha = maskColor & COLOR_MASK;
      img.format = PApplet.ARGB;
      img.loadPixels();
      for (int i = 0; i < img.pixels.length; i++)
      {
         if ((img.pixels[i] & COLOR_MASK) == nonAlpha)
         {
            img.pixels[i] = alphaValue | nonAlpha;
         }
      }
      img.updatePixels();
   }

   public static boolean processLine(String line, WorldModel world, ImageStore imageStore) {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case BGND_KEY:
               return Parse.parseBackground(properties, imageStore, world);
            case MINER_KEY:
               return Parse.parseMiner(properties,world, imageStore);
            case OBSTACLE_KEY:
               return Parse.parseObstacle(properties, imageStore,world );
            case ORE_KEY:
               return Parse.parseOre(properties, imageStore,world);
            case SMITH_KEY:
               return Parse.parseBlacksmith(properties, imageStore,world);
            case VEIN_KEY:
               return Parse.parseVein(properties, imageStore,world);
         }
      }

      return false;
   }
   public static void loadImages(Scanner in, ImageStore imageStore,
                                 PApplet screen)
   {
      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            Functions.processImageLine(imageStore.images, in.nextLine(), screen);
         }
         catch (NumberFormatException e)
         {
            System.out.println(String.format("Image format error on line %d",
                    lineNumber));
         }
         lineNumber++;
      }
   }





















}

