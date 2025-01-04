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

import dev.siroshun.jfun.result.assertion.ResultAssertions;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Function;
import java.util.stream.Stream;

class FlatMapTest {

    private sealed interface TestCase permits SuccessTestCase, FailureTestCase {
        Result<?, ?> result();
    }

    private record SuccessTestCase(Result<?, ?> result, Object want) implements TestCase {
    }

    private record FailureTestCase(Result<?, ?> result, Object want) implements TestCase {
    }

    private static Stream<TestCase> testCases() {
        return Stream.of(
            new SuccessTestCase(Result.success(), null),
            new SuccessTestCase(Result.success("test"), "test"),
            new SuccessTestCase(Result.success(null), null),
            new FailureTestCase(Result.failure(), null),
            new FailureTestCase(Result.failure("test"), "test"),
            new FailureTestCase(Result.failure(null), null)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void test(TestCase testCase) {
        Result<?, ?> result = testCase.result();
        switch (testCase) {
            case SuccessTestCase successTestCase:
                ResultAssertions.assertSuccess(result.flatMap(calledFunction(successTestCase.want(), Result.success("flatMap")), notCalledFunction()), "flatMap");
                ResultAssertions.assertFailure(result.flatMap(calledFunction(successTestCase.want(), Result.failure("flatMap")), notCalledFunction()), "flatMap");
                return;
            case FailureTestCase failureTestCase:
                ResultAssertions.assertSuccess(result.flatMap(notCalledFunction(), calledFunction(failureTestCase.want(), Result.success("flatMap"))), "flatMap");
                ResultAssertions.assertFailure(result.flatMap(notCalledFunction(), calledFunction(failureTestCase.want(), Result.failure("flatMap"))), "flatMap");
                return;
            default:
                Assertions.fail(testCase + " failed");
        }
    }

    private static <T, R> Function<T, R> calledFunction(T expectedValue, R returnResult) {
        return t -> {
            Assertions.assertSame(expectedValue, t);
            return returnResult;
        };
    }

    private static <T, U> Function<T, U> notCalledFunction() {
        return ignored -> {
            Assertions.fail("Should not be called");
            throw new AssertionError();
        };
    }
}
