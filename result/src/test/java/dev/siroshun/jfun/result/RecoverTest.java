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

class RecoverTest {

    private sealed interface TestCase permits SuccessTestCase, FailureTestCase {
        Result<String, String> result();
    }

    private record SuccessTestCase(Result<String, String> result, Object want) implements TestCase {
    }

    private record FailureTestCase(Result<String, String> result, Object want) implements TestCase {
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
        Result<String, String> result = testCase.result();
        switch (testCase) {
            case SuccessTestCase ignored:
                Assertions.assertSame(result, result.recover(notCalledFunction()));
                Assertions.assertSame(result, result.tryRecover(notCalledFunction()));
                return;
            case FailureTestCase failureTestCase:
                ResultAssertions.assertSuccess(result.recover(calledFunction(failureTestCase.want(), "recover")), "recover");
                ResultAssertions.assertSuccess(result.tryRecover(calledFunction(failureTestCase.want(), Result.success("tryRecover"))), "tryRecover");
                ResultAssertions.assertFailure(result.tryRecover(calledFunction(failureTestCase.want(), Result.failure("tryRecover"))), "tryRecover");
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
