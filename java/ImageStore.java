
import java.util.*;
import processing.core.PImage;

final class ImageStore
{
   public Map<String, List<PImage>> images;
   public List<PImage> defaultImages;

   public ImageStore(PImage defaultImage)
   {
      this.images = new HashMap<>();
      defaultImages = new LinkedList<>();
      defaultImages.add(defaultImage);
   }


   public List<PImage> getImageList(String key)
   {

      return this.images.getOrDefault(key, this.defaultImages);
   }








}
