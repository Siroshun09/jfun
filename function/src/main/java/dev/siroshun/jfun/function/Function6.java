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

package dev.siroshun.jfun.function;

/**
 * An interface to create a specified type from 6 arguments.
 *
 * @param <A1> the type of the 1st argument
 * @param <A2> the type of the 2nd argument
 * @param <A3> the type of the 3rd argument
 * @param <A4> the type of the 4th argument
 * @param <A5> the type of the 5th argument
 * @param <A6> the type of the 6th argument
 * @param <R> the type of the function result
 */
@FunctionalInterface
public interface Function6<A1, A2, A3, A4, A5, A6, R> {

    /**
     * Applies this function to given 6 arguments.
     *
     * @param a1 the 1st argument
     * @param a2 the 2nd argument
     * @param a3 the 3rd argument
     * @param a4 the 4th argument
     * @param a5 the 5th argument
     * @param a6 the 6th argument
     * @return the function result
     */
    R apply(A1 a1, A2 a2, A3 a3, A4 a4, A5 a5, A6 a6);

}
