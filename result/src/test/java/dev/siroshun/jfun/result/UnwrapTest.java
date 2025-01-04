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

import java.util.NoSuchElementException;
import java.util.stream.Stream;

class UnwrapTest {

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
            case SuccessTestCase successTestCase:
                Assertions.assertEquals(successTestCase.want(), result.unwrap());
                Assertions.assertEquals(successTestCase.want(), result.unwrapOr("unwrapOr"));
                Assertions.assertEquals(successTestCase.want(), result.unwrapOrGet(() -> {
                    Assertions.fail("Should not be called");
                    throw new AssertionError();
                }));
                Assertions.assertThrows(NoSuchElementException.class, result::unwrapError);
                return;
            case FailureTestCase failureTestCase:
                Assertions.assertThrows(NoSuchElementException.class, result::unwrap);
                Assertions.assertEquals("unwrapOr", result.unwrapOr("unwrapOr"));
                Assertions.assertEquals("unwrapOrGet", result.unwrapOrGet(() -> "unwrapOrGet"));
                Assertions.assertEquals(failureTestCase.want(), result.unwrapError());
                return;
            default:
                Assertions.fail(testCase + " failed");
        }
    }
}
