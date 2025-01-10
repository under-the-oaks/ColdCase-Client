package tech.underoaks.coldcase.stages;

import tech.underoaks.coldcase.game.Levels;

public enum Stages {
    MAIN_MENU{
        public AbstractStage getScreen(Object... params) {
            return new MainMenuStage();
        }
    },
    GAME {
        public AbstractStage getScreen(Object... params) {
            Levels level = (params.length > 0 && params[0] instanceof Levels) ? (Levels) params[0] : Levels.LEVEL_01;
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

    abstract public AbstractStage getScreen(Object... params);
}
