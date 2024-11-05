package ic2.advancedmachines.blocks.tiles.machines;

/**
 * Interface representing an energy provider block.
 * <p>
 * Implementations of this interface are responsible for providing energy
 * to adjacent blocks, such as electrolyzers.
 * </p>
 *
 * <p>
 * The primary purpose of this interface is to define a contract for blocks
 * that can supply energy. The {@code getTransferRate} method is used to
 * determine the maximum amount of energy that can be provided per tick.
 * </p>
 *
 * <p>
 * Example usage:
 * <pre>
 * public class EnergyProviderBlock implements IEnergyProvider {
 *     private int transferRate;
 *
 *     public EnergyProviderBlock(int transferRate) {
 *         this.transferRate = transferRate;
 *     }
 *
 *     {@literal @}Override
 *     public int getTransferRate() {
 *         return transferRate;
 *     }
 * }
 * </pre>
 * </p>
 */
public interface IEnergyProvider {
    /**
     * Gets the maximum transfer rate of energy this provider can supply per tick.
     *
     * @return the maximum energy transfer rate in energy units per tick
     */
    int getTransferRate();
}
