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

class MapTest {

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
                ResultAssertions.assertSuccess(result.map(calledFunction(successTestCase.want(), "map")), "map");
                ResultAssertions.assertSuccess(result.mapOr(calledFunction(successTestCase.want(), "mapOr"), "mapOrDefault"), "mapOr");
                ResultAssertions.assertSuccess(result.mapOrGet(calledFunction(successTestCase.want(), "mapOrGet"), () -> {
                    Assertions.fail("Should not be called");
                    throw new AssertionError();
                }), "mapOrGet");
                ResultAssertions.assertSuccess(result.mapOrElse(calledFunction(successTestCase.want(), "mapOrElse"), notCalledFunction()), "mapOrElse");
                ResultAssertions.assertSuccess(result.mapError(notCalledFunction()), successTestCase.want());
                return;
            case FailureTestCase failureTestCase:
                ResultAssertions.assertFailure(result.map(notCalledFunction()), failureTestCase.want());
                ResultAssertions.assertSuccess(result.mapOr(notCalledFunction(), "mapOrDefault"), "mapOrDefault");
                ResultAssertions.assertSuccess(result.mapOrGet(notCalledFunction(), () -> "mapOrGet"), "mapOrGet");
                ResultAssertions.assertSuccess(result.mapOrElse(notCalledFunction(), calledFunction(failureTestCase.want(), "mapOrElse")), "mapOrElse");
                ResultAssertions.assertFailure(result.mapError(calledFunction(failureTestCase.want(), "mapError")), "mapError");
                return;
            default:
                Assertions.fail(testCase + " failed");
        }
    }

    private static <T, U> Function<T, U> calledFunction(T expectedValue, U returnValue) {
        return t -> {
            Assertions.assertSame(expectedValue, t);
            return returnValue;
        };
    }

    private static <T, U> Function<T, U> notCalledFunction() {
        return ignored -> {
            Assertions.fail("Should not be called");
            throw new AssertionError();
        };
    }
}
