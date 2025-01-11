package tech.underoaks.coldcase.stages;

import tech.underoaks.coldcase.game.Levels;

/**
 * Enum representing the different stages/screens in the game.
 * Each stage is responsible for creating the appropriate screen when requested.
 *
 * @author Max Becker, Jean-Luc Wenserski
 */
public enum Stages {
    MAIN_MENU{
        public AbstractStage getScreen(Object... params) {
            return new MainMenuStage();
        }
    },
    /**
     * The game stage where the actual gameplay occurs.
     * It takes a level parameter to load the appropriate game level. if none is provided it will load Level_01
     */
    GAME {
        public AbstractStage getScreen(Object... params) {
            Levels level = (params.length > 0 && params[0] instanceof Levels) ? (Levels) params[0] : null;
            return new GameStage(level);
        }
    },
    JOIN{
        public AbstractStage getScreen(Object... params) {
            return new JoinStage();
        }
    },
    HOST{
        public AbstractStage getScreen(Object... params) {
            return new HostStage();
        }
    },
    SETTINGS{
        public AbstractStage getScreen(Object... params) {
            return new SettingsStage();
        }
    };

    /**
     * Abstract method that returns the corresponding screen for the stage.
     * Each stage must implement this method to return the appropriate screen based on the parameters.
     *
     * @param params The parameters to configure the screen.
     * @return The screen corresponding to the stage.
     */
    abstract public AbstractStage getScreen(Object... params);
}
