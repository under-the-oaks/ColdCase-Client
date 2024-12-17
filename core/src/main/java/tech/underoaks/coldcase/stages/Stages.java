package tech.underoaks.coldcase.stages;

public enum Stages {
    MAIN_MENU{
        public AbstractStage getScreen(Object... params) {
            return new MainMenu();
        }
    },
    GAME{
        public AbstractStage getScreen(Object... params) {
            return new GameStage();
        }
    };

    abstract public AbstractStage getScreen(Object... params);
}
