/*
 *    Copyright 2025 Siroshun09
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package dev.siroshun.jfun.result.assertion;

import dev.siroshun.jfun.result.Result;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AssertionFailureBuilder;
import org.junit.jupiter.api.Assertions;

/**
 * The class that provides methods for asserting {@link Result}s.
 */
public final class ResultAssertions {

    /**
     * Asserts the {@link Result} to be {@link Result.Success}.
     *
     * @param result the {@link Result} to assert
     * @param <T>    the type of the success value
     * @return the unwrapped value using {@link Result#unwrap()}
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <T> T assertSuccess(@NotNull Result<? extends T, ?> result) {
        if (result.isSuccess()) {
            return result.unwrap();
        }
        return fail(result, true);
    }

    /**
     * Asserts the {@link Result} to be {@link Result.Success}, then compares the unwrapped value and the specified {@code expectedValue}.
     *
     * @param result        the {@link Result} to assert
     * @param expectedValue the expected success value
     * @param <T>           the type of the success value
     * @return the unwrapped value using {@link Result#unwrap()}
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <T> T assertSuccess(@NotNull Result<? extends T, ?> result, T expectedValue) {
        if (result.isSuccess()) {
            T actual = result.unwrap();
            Assertions.assertEquals(expectedValue, actual);
            return actual;
        }
        return fail(result, true);
    }

    /**
     * Asserts the {@link Result} to be {@link Result.Failure}.
     *
     * @param result the {@link Result} to assert
     * @param <E>    the type of the error value
     * @return the unwrapped error value using {@link Result#unwrapError()}
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <E> E assertFailure(@NotNull Result<?, ? extends E> result) {
        if (result.isFailure()) {
            return result.unwrapError();
        }
        return fail(result, false);
    }

    /**
     * Asserts the {@link Result} to be {@link Result.Failure}, then compares the unwrapped error and the specified {@code expectedError}.
     *
     * @param result        the {@link Result} to assert
     * @param expectedError the expected error value
     * @param <E>           the type of the error value
     * @return the unwrapped error value using {@link Result#unwrapError()}
     */
    @SuppressWarnings("UnusedReturnValue")
    public static <E> E assertFailure(@NotNull Result<?, ? extends E> result, E expectedError) {
        if (result.isFailure()) {
            E actual = result.unwrapError();
            Assertions.assertEquals(expectedError, actual);
            return actual;
        }
        return fail(result, false);
    }

    private static <T> T fail(@NotNull Result<?, ?> result, boolean expectedSuccess) {
        AssertionFailureBuilder builder = AssertionFailureBuilder.assertionFailure();

        if (expectedSuccess) {
            builder.expected("SUCCESS").actual(result);
            builder.reason("Expected success, but actual failed.");
        } else {
            builder.expected("FAILURE").actual(result);
            builder.reason("Expected failure, but actual succeeded.");
        }

        throw builder.build();
    }

    private ResultAssertions() {
        throw new UnsupportedOperationException();
    }
}
