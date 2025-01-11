package tech.underoaks.coldcase.game;

import tech.underoaks.coldcase.stages.StageManager;
import tech.underoaks.coldcase.stages.Stages;

/**
 * Manages the levels in the game.
 * This class is responsible for loading levels, tracking the current level,
 * and transitioning between levels.
 *
 * @author Jean-Luc Wenserski
 */
public class LevelManager {
    private static LevelManager instance;

    public int currentLevelIndex = 0;

    private LevelManager() {}

    /**
     * Returns the singleton instance of LevelManager.
     * If the instance doesn't exist, it creates a new one.
     *
     * @return The singleton instance of LevelManager.
     */
    public static LevelManager getInstance(){
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    /**
     * Loads the next level based on the current level index.
     * If the current level is the last one, it resets to the first level
     * and sets the next stage to the main menu.
     */
    public void loadNextLevel() {
        if(currentLevelIndex < Levels.values().length){
            loadLevel(Levels.values()[currentLevelIndex]);
        }else {
            currentLevelIndex = 0;
            StageManager.getInstance().setNextStage(Stages.MAIN_MENU);
        }
        currentLevelIndex++;
    }

    /**
     * Loads the specified level and sets the next stage to the game stage
     * for the given level.
     *
     * @param level The level to be loaded.
     */
    public void loadLevel(Levels level){
        PlayerController.getInstance().setInventory(null);
        StageManager.getInstance().setNextStage(Stages.GAME, level);
    }
}
