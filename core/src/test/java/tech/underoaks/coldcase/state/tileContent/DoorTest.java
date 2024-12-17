package tech.underoaks.coldcase.state.tileContent;

/**
 * to test:
 * - action method case 1:
 * -> interaction.getCaller() != Player.class.getName()
 * -> should call chain.addGameStateUpdate() with ChangeTextureUpdate and PlayerPassebilityUpdate
 * - action method case 2:
 * -> interaction.getCaller() == Player.class.getName()
 * -> should return false
 *
 * @implNote not testable with current implementation
 */
public class DoorTest {
}
