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

import java.util.Optional;
import java.util.stream.Stream;

class ToOptionalErrorTest {

    private record TestCase(Result<?, ?> result, boolean isPresent, Object want) {
    }

    private static Stream<TestCase> testCases() {
        return Stream.of(
            new TestCase(Result.success(), false, null),
            new TestCase(Result.success("test"), false, null),
            new TestCase(Result.success(null), false, null),
            new TestCase(Result.failure(), false, null),
            new TestCase(Result.failure("test"), true, "test"),
            new TestCase(Result.failure(null), false, null)
        );
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void test(TestCase testCase) {
        Optional<?> ret = testCase.result.toOptionalError();
        Assertions.assertEquals(ret.isPresent(), testCase.isPresent);
        Assertions.assertEquals(ret.orElse(null), testCase.want);
    }
}
