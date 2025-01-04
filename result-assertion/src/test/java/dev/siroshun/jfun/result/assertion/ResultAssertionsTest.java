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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

class ResultAssertionsTest {

    @Test
    void assertSuccess() {
        Assertions.assertDoesNotThrow(() -> ResultAssertions.assertSuccess(Result.success()));
        Assertions.assertDoesNotThrow(() -> ResultAssertions.assertSuccess(Result.success("test"), "test"));
        Assertions.assertDoesNotThrow(() -> ResultAssertions.assertSuccess(Result.success(null), null));

        Assertions.assertEquals("test", ResultAssertions.assertSuccess(Result.success("test")));
        Assertions.assertEquals("test", ResultAssertions.assertSuccess(Result.success("test"), "test"));
        Assertions.assertNull(ResultAssertions.assertSuccess(Result.success(null)));
        Assertions.assertNull(ResultAssertions.assertSuccess(Result.success(null), null));

        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.success("test"), "diff"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.success(null), "diff"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.success("test"), null));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.failure()));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.failure("test"), "test"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.failure("test"), "diff"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.failure(null), "test"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertSuccess(Result.failure("test"), null));
    }

    @Test
    void assertFailure() {
        Assertions.assertDoesNotThrow(() -> ResultAssertions.assertFailure(Result.failure()));
        Assertions.assertDoesNotThrow(() -> ResultAssertions.assertFailure(Result.failure("test"), "test"));
        Assertions.assertDoesNotThrow(() -> ResultAssertions.assertFailure(Result.failure(null), null));

        Assertions.assertEquals("test", ResultAssertions.assertFailure(Result.failure("test")));
        Assertions.assertEquals("test", ResultAssertions.assertFailure(Result.failure("test"), "test"));
        Assertions.assertNull(ResultAssertions.assertFailure(Result.failure(null)));
        Assertions.assertNull(ResultAssertions.assertFailure(Result.failure(null), null));

        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.failure("test"), "diff"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.failure(null), "diff"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.failure("test"), null));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.success()));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.success("test"), "test"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.success("test"), "diff"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.success(null), "test"));
        Assertions.assertThrows(AssertionFailedError.class, () -> ResultAssertions.assertFailure(Result.success("test"), null));
    }

}
