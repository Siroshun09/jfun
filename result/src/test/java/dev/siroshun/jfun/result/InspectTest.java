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

package dev.siroshun.jfun.result;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Consumer;
import java.util.stream.Stream;

class InspectTest {

    private record TestCase<T, E>(Result<T, E> result, Consumer<? super T> inspect, Consumer<? super E> inspectError) {

        void test() {
            Assertions.assertSame(this.result, this.result.inspect(this.inspect));
            Assertions.assertSame(this.result, this.result.inspectError(this.inspectError));
        }
    }

    private static Stream<TestCase<?, ?>> testCases() {
        return Stream.of(
            new TestCase<>(Result.success(), calledConsumer(null), notCalledConsumer()),
            new TestCase<>(Result.success("test"), calledConsumer("test"), notCalledConsumer()),
            new TestCase<>(Result.success(null), calledConsumer(null), notCalledConsumer()),
            new TestCase<>(Result.failure(), notCalledConsumer(), calledConsumer(null)),
            new TestCase<>(Result.failure("test"), notCalledConsumer(), calledConsumer("test")),
            new TestCase<>(Result.failure(null), notCalledConsumer(), calledConsumer(null))
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void test(TestCase<?, ?> testCase) {
        testCase.test();
    }

    private static <T> Consumer<T> calledConsumer(T expectedValue) {
        return t -> Assertions.assertEquals(expectedValue, t);
    }

    private static <T> Consumer<T> notCalledConsumer() {
        return ignored -> {
            Assertions.fail("Should not be called");
            throw new AssertionError();
        };
    }
}

