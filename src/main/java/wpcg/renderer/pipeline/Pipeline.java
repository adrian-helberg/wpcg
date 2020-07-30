package wpcg.renderer.pipeline;

import wpcg.renderer.pipeline.step.Step;

/**
 * Pipeline class to implement Pipeline Design Pattern
 * @param <I> Input
 * @param <O> Output
 */
public class Pipeline<I, O> {
    // Current step of the pipeline
    private final Step<I, O> current;

    /**
     * Create a pipeline with initial step
     * @param current Initial step
     */
    public Pipeline(Step<I, O> current) {
        this.current = current;
    }

    /**
     * Attach another step to the pipeline
     * @param next Next step in the pipeline
     * @param <NewO> Next step output
     * @return New pipeline with initial step
     */
    public <NewO> Pipeline<I, NewO> pipe(Step<O, NewO> next) {
        return new Pipeline<>(input -> next.process(current.process(input)));
    }

    /**
     * Execute pipeline
     * @param input Pipeline input
     * @return Pipeline output
     * @throws Step.StepException Execution exception
     */
    public O execute(I input) throws Step.StepException {
        return current.process(input);
    }
}
