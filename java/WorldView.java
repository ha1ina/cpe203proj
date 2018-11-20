import processing.core.PApplet;
import processing.core.PImage;
import java.util.Optional;

final class WorldView
{
   public PApplet screen;
   public WorldModel world;
   public int tileWidth;
   public int tileHeight;
   public Viewport viewport;

   public WorldView(int numRows, int numCols, PApplet screen, WorldModel world,
                    int tileWidth, int tileHeight)
   {
      this.screen = screen;
      this.world = world;
      this.tileWidth = tileWidth;
      this.tileHeight = tileHeight;
      this.viewport = new Viewport(numRows, numCols);
   }


   public void shiftView(int colDelta, int rowDelta, WorldModel world)
   {
      int newCol = clamp(this.viewport.getCol() + colDelta, 0,
              world.getNumCols() - this.viewport.getNumCols());
      int newRow = clamp(this.viewport.getRow() + rowDelta, 0,
              world.getNumRows() - this.viewport.getNumRows());

      this.viewport.shift(newCol, newRow);
   }

   public void drawBackground(WorldModel world, Viewport view)
   {
      for (int row = 0; row < world.getNumRows(); row++)
      {
         for (int col = 0; col < world.getNumCols(); col++)
         {
            Point worldPoint = view.viewportToWorld(view, col, row);
            Optional<PImage> image = world.getBackgroundImage(worldPoint);
            if (image.isPresent())
            {
               screen.image(image.get(), col * tileWidth,
                       row * tileHeight);
            }
         }

      }
   }
   public void drawEntities(Viewport view)
   {
      for (Entity entity : world.getEntities())
      {
         Point pos = entity.getPosition();

         if (view.contains( pos))
         {
            Point viewPoint = view.worldToViewport(pos.x, pos.y);
            screen.image(entity.getCurrentImage(),
                    viewPoint.x * tileWidth, viewPoint.y * tileHeight);
         }
      }
   }


   public static int clamp(int value, int low, int high) {

      return Math.min(high, Math.max(value, low));
   }





}
