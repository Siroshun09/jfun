/**
 * The package provides utility methods that can be used when using {@link dev.siroshun.jfun.result.Result} in testing.
 */
module dev.siroshun.jfun.result.assertion {
    requires org.jetbrains.annotations;
    requires org.jspecify;
    requires dev.siroshun.jfun.result;
    requires org.junit.jupiter.api;

    exports dev.siroshun.jfun.result.assertion;
}
