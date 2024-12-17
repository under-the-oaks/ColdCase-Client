package tech.underoaks.coldcase.stages;

public enum Stages {
    MAIN_MENU{
        public AbstractStage getScreen(Object... params) {
            return new MainMenuStage();
        }
    },
    GAME{
        public AbstractStage getScreen(Object... params) {
            return new GameStage();
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
