package wpcg.renderer.pipeline.step;

/**
 * Step interface
 * @param <I> Input
 * @param <O> Output
 */
public interface Step<I, O> {
    /**
     * Exception class to manage exceptions
     */
    class StepException extends RuntimeException {
        public StepException(Throwable t) {
            super(t);
        }
    }

    /**
     * Process step
     * @param input Step input
     * @return Step output
     * @throws StepException Step execution Exception
     */
    O process(I input) throws StepException;
}
