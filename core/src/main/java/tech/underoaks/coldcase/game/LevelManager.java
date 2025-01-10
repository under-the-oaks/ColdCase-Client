package tech.underoaks.coldcase.game;

import tech.underoaks.coldcase.stages.StageManager;
import tech.underoaks.coldcase.stages.Stages;

import java.awt.*;

public class LevelManager {
    private static LevelManager instance;

    public int currentLevelIndex = 0;

    private LevelManager() {}

    public static LevelManager getInstance(){
        if (instance == null) {
            instance = new LevelManager();
        }
        return instance;
    }

    // Load the next level dynamically based on the current level
    public void loadNextLevel() {
        currentLevelIndex++;
        System.out.println(Levels.values().length);
        if(currentLevelIndex <= Levels.values().length){
            LoadLevel(Levels.values()[currentLevelIndex]);
            //StageManager.getInstance().setNextStage(Stages.MAIN_MENU);
        }else {
            currentLevelIndex = 0;
            StageManager.getInstance().setNextStage(Stages.MAIN_MENU);
        }
    }

    public void LoadLevel(Levels level){
        StageManager.getInstance().setNextStage(Stages.GAME, level);
    }
}
