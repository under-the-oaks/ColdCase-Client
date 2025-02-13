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
    private Texture holeTexture;
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

        this.holeTexture = factory.create("./sprites/tileContent_hole.png");
    }

    /**
     * Creates a new singleton instance of this controller.
     * @param factory {@link TextureFactory} that will be creating the {@link Texture} objects.
     * @return The singleton
     */
    public static TextureController create(TextureFactory factory) {
        if (instance != null) {
            throw new IllegalStateException("TextureController already initialized");
        }

        instance = new TextureController(factory);
        return instance;
    }

    /**
     * Destroys the current singleton instance
     * @see TextureController#getInstance()
     */
    public static void destroy() {
        instance = null;
    }

    /**
     * Singleton-Caller for this Controller
     * @return {@link TextureController} instance.
     * @throws IllegalStateException if the TextureController has not been initialized yet.
     * @see TextureController#create(TextureFactory)
     */
    public static TextureController getInstance() {
        if (instance == null) {
            throw new IllegalStateException("TextureController not initialized");
        }
        return instance;
    }

    /**
     * Checks if a singleton of this class already exists
     * @return True if it exists; False otherwise
     * @see TextureController#getInstance()
     */
    public static boolean exists() {
        return instance != null;
    }

    /**
     * Sets whether this TextureController is configured to return detective or ghost textures
     * @param isDetective True if detective; False if ghost
     */
    public static void setIsDetective(boolean isDetective) {
        TextureController.isDetective = isDetective;
    }

    /**
     * Whether this TextureController is configured to return detective or ghost textures
     * @return True if detective; False if ghost
     */
    public static boolean getIsDetective() {
        return isDetective;
    }

    /**
     * Texture-Getter
     * @return EmptyTileTexture
     */
    public Texture getEmptyTileTexture() {
        return emptyTileTexture;
    }

    /**
     * Texture-Getter
     * @return GroundTileTexture
     */
    public Texture getGroundTileTexture() {
        return isDetective ? detectiveGroundTileTexture : ghostGroundTileTexture;
    }

    /**
     * Texture-Getter
     * @return TestItemTexture
     */
    public Texture getTestItemTexture() {
        return testItemTexture;
    }

    /**
     * Texture-Getter
     * @return TestItem02Texture
     */
    public Texture getTestItem02Texture() {
        return testItem02Texture;
    }

    /**
     * Texture-Getter
     * @return DoorTriggerTexture
     */
    public Texture getDoorTriggerTexture() {
        return doorTriggerTexture;
    }

    /**
     * Texture-Getter
     * @return TestContentTexture
     */
    public Texture getTestContentTexture() {
        return testContentTexture;
    }

    /**
     * Texture-Getter
     * @return PortalObjectTexture
     */
    public Texture getPortalObjectTexture() {
        return portalObjectTexture;
    }

    /**
     * Texture-Getter
     * @return WallTexture
     */
    public Texture getWallTexture() {
        return isDetective ? detectiveWallTexture : ghostWallTexture;
    }

    /**
     * Texture-Getter
     * @return MovableBlockTexture
     */
    public Texture getMovableBlockTexture() {
        return isDetective ? movableBlockTextureDetective :movableBlockTextureGhost ;
    }

    /**
     * Texture-Getter
     * @return MovableBlockTranscendantTexture
     */
    public Texture getMovableBlockTranscendantTexture() {
        return movableBlockTranscendantTexture;
    }

    /**
     * Texture-Getter
     * @return TranscendentTestBlockTexture
     */
    public Texture getTranscendentTestBlockTexture() {
        return transcendentTestBlockTexture;
    }

    /**
     * Texture-Getter
     * @return GoalObjectTexture
     */
    public Texture getGoalObjectTexture() {
        return goalObjectTexture;
    }

    /**
     * Texture-Getter
     * @return DoorTexture_closed
     */
    public Texture getDoorTexture_closed() {
        return doorTexture_closed;
    }

    /**
     * Texture-Getter
     * @return DoorTexture_open
     */
    public Texture getDoorTexture_open() {
        return doorTexture_open;
    }

    /**
     * Texture-Getter
     * @return GloveTexture
     */
    public Texture getGloveTexture() {
        return gloveTexture;
    }

    /**
     * Texture-Getter
     * @return PlayerTexture
     */
    public Texture getPlayerTexture() {
        return isDetective ? detectiveTexture : ghostTexture;
    }

    /**
     * Texture-Getter
     * @return PlayerTextureNorth
     */
    public Texture getPlayerTextureNorth() {
        return isDetective ? detectiveTextureNorth : ghostTextureNorth;
    }

    /**
     * Texture-Getter
     * @return PlayerTextureSouth
     */
    public Texture getPlayerTextureSouth() {
        return isDetective ? detectiveTextureSouth : ghostTextureSouth;
    }

    /**
     * Texture-Getter
     * @return PlayerTextureEast
     */
    public Texture getPlayerTextureEast() {
        return isDetective ? detectiveTextureEast : ghostTextureEast;
    }

    /**
     * Texture-Getter
     * @return PlayerTextureWest
     */
    public Texture getPlayerTextureWest() {
        return isDetective ? detectiveTextureWest : ghostTextureWest;
    }

    /**
     * Texture-Getter
     * @return GhostTexture
     */
    public Texture getGhostTexture() {
        return ghostTexture;
    }

    /**
     * Texture-Getter
     * @return DetectiveTexture
     */
    public Texture getDetectiveTexture() {
        return detectiveTexture;
    }

    /**
     * Texture-Getter
     * @return Trigger_closed
     */
    public Texture getTrigger_closed() {
        return trigger_closed;
    }

    /**
     * Texture-Getter
     * @return Trigger_opened
     */
    public Texture getTrigger_opened() {
        return trigger_opened;
    }

    /**
     * Texture-Getter
     * @return holeTexture
     */
    public Texture holeTexture() {
        return holeTexture;
    }
}
