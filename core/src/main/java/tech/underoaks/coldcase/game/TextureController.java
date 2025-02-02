package tech.underoaks.coldcase.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * This class is responsible for providing the textures for the game elements.
 * <p>
 * It provides the textures for the tiles, the player, the items, the walls, etc.
 * The textures are loaded from the assets folder.
 * The textures are loaded only once and are stored in memory.
 * The textures are accessed through the methods provided by this class.
 * <p>
 * The textures are loaded using the {@link TextureFactory} class.
 *
 * @author mabe.edu, Danmyer
 **/
public class TextureController {
    private static TextureController instance;

    private static boolean isDetective = true;

    // Texturen
    private Texture emptyTileTexture;
    private Texture detectiveGroundTileTexture;
    private Texture ghostGroundTileTexture;
    private Texture testItemTexture;
    private Texture testItem02Texture;
    private Texture doorTriggerTexture;
    private Texture testContentTexture;
    private Texture portalObjectTexture;
    private Texture detectiveWallTexture;
    private Texture ghostWallTexture;
    private Texture movableBlockTextureDetective;
    private Texture movableBlockTextureGhost;
    private Texture movableBlockTranscendantTexture;
    private Texture transcendentTestBlockTexture;
    private Texture goalObjectTexture;
    private Texture doorTexture_closed;
    private Texture doorTexture_open;
    private Texture gloveTexture;
    private Texture detectiveTexture;
    private Texture detectiveTextureNorth;
    private Texture detectiveTextureSouth;
    private Texture detectiveTextureEast;
    private Texture detectiveTextureWest;
    private Texture ghostTexture;
    private Texture ghostTextureNorth;
    private Texture ghostTextureSouth;
    private Texture ghostTextureEast;
    private Texture ghostTextureWest;

    private Texture trigger_closed;
    private Texture trigger_opened;


    private TextureController(TextureFactory factory) {
        this.emptyTileTexture = factory.create("./isometric tileset/separated images/tile_101.png");
        this.detectiveGroundTileTexture = factory.create("./sprites/block_detective_2.png");
        this.ghostGroundTileTexture = factory.create("./sprites/block_ghost_4.png");

        this.testItemTexture = factory.create("./isometric tileset/separated images/TEST_ITEM.png");
        this.testItem02Texture = factory.create("./isometric tileset/separated images/TEST_ITEM02.png");
        this.doorTriggerTexture = factory.create("./isometric tileset/separated images/tile_069.png");
        this.testContentTexture = factory.create("./isometric tileset/separated images/tile_050.png");
        this.portalObjectTexture = factory.create("./sprites/portal.png");
        this.detectiveWallTexture = factory.create("./sprites/block_detective.png");
        this.ghostWallTexture = factory.create("./sprites/block_ghost_3.png");
        this.movableBlockTextureDetective = factory.create("./sprites/block_detective_4.png");
        this.movableBlockTextureGhost = factory.create("./sprites/block_ghost.png");
        this.movableBlockTranscendantTexture = factory.create("sprites/block_transcendent_2.png");
        this.transcendentTestBlockTexture = factory.create("sprites/block_transcendent_2.png");
        this.goalObjectTexture = factory.create("./sprites/object_goal_detective_2.png");
        this.doorTexture_closed = factory.create("./sprites/object_spike_detective_2.png");
        this.doorTexture_open = factory.create("./sprites/object_spike_retracted_detective_1.png");

        this.gloveTexture = factory.create("./sprites/item_glove_detective_6.png");

        this.detectiveTexture = factory.create("./sprites/player_detective_right.png");
        this.detectiveTextureNorth = factory.create("./sprites/player_detective_up.png");
        this.detectiveTextureSouth = factory.create("./sprites/player_detective_down.png");
        this.detectiveTextureEast = factory.create("./sprites/player_detective_right.png");
        this.detectiveTextureWest = factory.create("./sprites/player_detective_left.png");

        this.ghostTexture = factory.create("./sprites/Sprite_Ghost_Right.png");
        this.ghostTextureNorth = factory.create("./sprites/Sprite_Ghost_Up.png");
        this.ghostTextureWest = factory.create("./sprites/Sprite_Ghost_Left.png");
        this.ghostTextureSouth = factory.create("./sprites/Sprite_Ghost_Down.png");
        this.ghostTextureEast = factory.create("./sprites/Sprite_Ghost_Right.png");

        this.trigger_closed = factory.create("./sprites/Trigger_closed.png");
        this.trigger_opened = factory.create("./sprites/Trigger_opened.png");

    }

    public static TextureController create(TextureFactory factory) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }

        instance = new TextureController(factory);
        return instance;
    }

    public static void destroy() {
        instance = null;
    }

    public static TextureController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    public static boolean exists() {
        return instance != null;
    }

    public static void setIsDetective(boolean isDetective) {
        TextureController.isDetective = isDetective;
    }

    public static boolean getIsDetective() {
        return isDetective;
    }

    public Texture getEmptyTileTexture() {
        return emptyTileTexture;
    }

    public Texture getGroundTileTexture() {
        return isDetective ? detectiveGroundTileTexture : ghostGroundTileTexture;
    }

    public Texture getTestItemTexture() {
        return testItemTexture;
    }

    public Texture getTestItem02Texture() {
        return testItem02Texture;
    }

    public Texture getDoorTriggerTexture() {
        return doorTriggerTexture;
    }

    public Texture getTestContentTexture() {
        return testContentTexture;
    }

    public Texture getPortalObjectTexture() {
        return portalObjectTexture;
    }

    public Texture getWallTexture() {
        return isDetective ? detectiveWallTexture : ghostWallTexture;
    }

    public Texture getMovableBlockTexture() {
        return isDetective ? movableBlockTextureDetective :movableBlockTextureGhost ;
    }

    public Texture getMovableBlockTranscendantTexture() {
        return movableBlockTranscendantTexture;
    }

    public Texture getTranscendentTestBlockTexture() {
        return transcendentTestBlockTexture;
    }

    public Texture getGoalObjectTexture() {
        return goalObjectTexture;
    }

    public Texture getDoorTexture_closed() {
        return doorTexture_closed;
    }

    public Texture getDoorTexture_open() {
        return doorTexture_open;
    }

    public Texture getGloveTexture() {
        return gloveTexture;
    }

    public Texture getPlayerTexture() {
        return isDetective ? detectiveTexture : ghostTexture;
    }

    public Texture getPlayerTextureNorth() {
        return isDetective ? detectiveTextureNorth : ghostTextureNorth;
    }

    public Texture getPlayerTextureSouth() {
        return isDetective ? detectiveTextureSouth : ghostTextureSouth;
    }

    public Texture getPlayerTextureEast() {
        return isDetective ? detectiveTextureEast : ghostTextureEast;
    }

    public Texture getPlayerTextureWest() {
        return isDetective ? detectiveTextureWest : ghostTextureWest;
    }

    public Texture getGhostTexture() {
        return ghostTexture;
    }

    public Texture getDetectiveTexture() {
        return detectiveTexture;
    }

    public Texture getTrigger_closed() {
        return trigger_closed;
    }

    public Texture getTrigger_opened() {
        return trigger_opened;
    }
}
