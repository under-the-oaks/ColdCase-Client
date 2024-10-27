package tech.underoaks.coldcase;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.lang.reflect.InvocationTargetException;

public record Map(
    Tile[][] tileArray
) {

    static float tileSize = 16;

    /**
     * Generates a new map without any tiles placed
     *
     * @param height Height of the map
     * @param width  Width of the map
     * @return Empty map
     */
    public static Map getEmptyMap(int height, int width) {
        return getMap(EmptyTile.class, height, width);
    }

    public static Map getPlainMap(int height, int width) {
        return getMap(GroundTile.class, height, width);
    }

    /**
     * Generates a new map that is filled with a specified type of tile
     *
     * @param tile Class that will fill the map
     * @param height Height of the map
     * @param width Width of the map
     * @return New Map
     */
    private static Map getMap(Class<? extends  Tile> tile, int height, int width) {
        Tile[][] tileArray = new Tile[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                try {
                    tileArray[y][x] = tile.getDeclaredConstructor().newInstance();
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new Map(tileArray);
    }

    public void render(SpriteBatch batch) {
        for (int y = 0; y < tileArray.length; y++) {
            for (int x = 0; x < tileArray[y].length; x++) {
                float tempX = x * tileSize * -1;
                float tempY = y * tileSize * -1;
                Vector2 tempPt = twoDToIso(new Vector2(tempX, tempY));
                tileArray[y][x].render(batch, tempPt.x, tempPt.y);
            }
        }
    }

    public Vector2 isoTo2D(Vector2 pt){
        Vector2 tempPt = new Vector2(0, 0);
        tempPt.x = (2 * pt.y + pt.x) / 2;
        tempPt.y = (2 * pt.y - pt.x) / 2;
        return(tempPt);
    }

    public Vector2 twoDToIso(Vector2 pt){
        Vector2 tempPt = new Vector2(0,0);
        tempPt.x = pt.x - pt.y;
        tempPt.y = (pt.x + pt.y) / 2;
        return(tempPt);
    }


}
