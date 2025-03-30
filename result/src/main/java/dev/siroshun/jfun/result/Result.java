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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * {@link Result} is a type that represents either success ({@link Success}) or failure ({@link Failure}).
 *
 * @param <T> the type of the success value
 * @param <E> the type of the error value
 */
public sealed interface Result<T, E> permits Result.Success, Result.Failure {

    /**
     * Returns a {@link Success} without value.
     * <p>
     * Returning {@link Result} has {@code null} as the success value.
     * <p>
     * This method always returns the same instance.
     *
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return a {@link Success} without value
     */
    static <T, E> Result.@NotNull Success<T, E> success() {
        return SuccessImpl.nullSuccess();
    }

    /**
     * Returns a {@link Success} with the specified value.
     * <p>
     * If {@code value} is {@code null}, this method returns {@link #success()}.
     *
     * @param value the result value
     * @param <T>   the type of the success value
     * @param <E>   the type of the error value
     * @return a {@link Success} the specified value
     */
    static <T, E> Result.@NotNull Success<T, E> success(@Nullable T value) {
        return value != null ? new SuccessImpl<>(value) : SuccessImpl.nullSuccess();
    }

    /**
     * Returns a {@link Failure} without value.
     * <p>
     * Returning {@link Result} has {@code null} as the error.
     * <p>
     * This method always returns the same instance.
     *
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return a {@link Failure} without value
     */
    static <T, E> Result.@NotNull Failure<T, E> failure() {
        return FailureImpl.nullFailure();
    }

    /**
     * Returns a {@link Failure} with the specified error.
     * <p>
     * If {@code value} is {@code null}, this method returns {@link #failure()}.
     *
     * @param e   the error
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     * @return a {@link Failure} the specified value
     */
    static <T, E> Result.@NotNull Failure<T, E> failure(@Nullable E e) {
        return e != null ? new FailureImpl<>(e) : FailureImpl.nullFailure();
    }

    /**
     * Returns {@code true} if this {@link Result} is {@link Success}.
     *
     * @return {@code true} if this {@link Result} is {@link Success}
     */
    boolean isSuccess();

    /**
     * Returns {@code true} if this {@link Result} is {@link Failure}.
     *
     * @return {@code true} if this {@link Result} is {@link Failure}
     */
    boolean isFailure();

    /**
     * Returns the value wrapped by the {@link Optional}.
     * <p>
     * If this {@link Result} is {@link Failure} or the value of {@link Success} is {@code null},
     * this method returns {@link Optional#empty()}.
     *
     * @return the value wrapped by the {@link Optional}
     */
    @NotNull Optional<T> toOptional();

    /**
     * Returns the error wrapped by the {@link Optional}.
     * <p>
     * If this {@link Result} is {@link Success} or the error of {@link Failure} is {@code null},
     * this method returns {@link Optional#empty()}.
     *
     * @return the error wrapped by the {@link Optional}
     */
    @NotNull Optional<E> toOptionalError();

    /**
     * Maps the value from {@link T} to {@link U} using the specified {@link Function}.
     * <p>
     * If this {@link Result} is {@link Failure}, this method does nothing.
     *
     * @param mapper the {@link Function} converts the value
     * @param <U>    the new type of the success value
     * @return the new {@link Success}, or same instance if this {@link Result} is {@link Failure}
     */
    <U> @NotNull Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper);

    /**
     * Maps the value from {@link T} to {@link U} using the specified {@link Function}, or returns new {@link Success} with the default value  if this {@link Result} is {@link Failure}.
     *
     * @param mapper       the {@link Function} converts the value
     * @param defaultValue the alternative value of {@link T}
     * @param <U>          the new type of the success value
     * @return the new {@link Success} with mapped value, or {@code defaultValue} if this {@link Result} is {@link Failure}
     */
    <U> @NotNull Result<U, E> mapOr(@NotNull Function<? super T, ? extends U> mapper, U defaultValue);

    /**
     * Maps the value from {@link T} to {@link U} using the specified {@link Function},
     * or returns new {@link Success} with the value provided by {@code supplier} if this {@link Result} is {@link Failure}.
     *
     * @param mapper   the {@link Function} converts the value
     * @param supplier the {@link Supplier} to get the alternative value of {@link T}
     * @param <U>      the new type of the success value
     * @return the new {@link Success} with mapped value, or the value provided by {@code supplier} if this {@link Result} is {@link Failure}
     */
    <U> @NotNull Result<U, E> mapOrGet(@NotNull Function<? super T, ? extends U> mapper, @NotNull Supplier<? extends U> supplier);

    /**
     * Maps the value from {@link T} to {@link U} using the specified {@link Function},
     * or returns new {@link Success} with the value converted by {@code onFailure} from {@link E} to {@link T} if this {@link Result} is {@link Failure}.
     *
     * @param mapper    the {@link Function} converts the value
     * @param onFailure the {@link Function} to create the new value from the error
     * @param <U>       the new type of the success value
     * @return the new {@link Success} with mapped value, or the value created by {@code onFailure} if this {@link Result} is {@link Failure}
     */
    <U> @NotNull Result<U, E> mapOrElse(@NotNull Function<? super T, ? extends U> mapper, @NotNull Function<? super E, ? extends U> onFailure);

    /**
     * Maps the error from {@link E} to {@link O} using the specified {@link Function}.
     * <p>
     * If this {@link Result} is {@link Success}, this method does nothing.
     *
     * @param mapper the {@link Function} converts the value
     * @param <O>    the new type of the error value
     * @return the new {@link Failure}, or same instance if this {@link Result} is {@link Success}
     */
    <O> @NotNull Result<T, O> mapError(@NotNull Function<? super E, ? extends O> mapper);

    /**
     * Maps and flatten the {@link Result} using the specified {@link Function}.
     *
     * @param onSuccess the {@link Function} processes the success value, then returns new {@link Result}
     * @param onFailure the {@link Function} processes the error value, then returns new {@link Result}
     * @param <U>       the new type of the success value
     * @param <E2>      the new type of the error value
     * @return the {@link Result} returned from {@link Function}
     * @throws NullPointerException if the {@link Function} returns {@code null} as {@link Result}
     */
    <U, E2> @NotNull Result<U, E2> flatMap(@NotNull Function<? super T, Result<U, E2>> onSuccess, @NotNull Function<? super E, Result<U, E2>> onFailure);

    /**
     * Calls the specified {@link Consumer} with the success value if this {@link Result} is {@link Success}.
     *
     * @param onSuccess the {@link Consumer} that accepts the success value
     * @return the same instance
     */
    @NotNull Result<T, E> inspect(@NotNull Consumer<? super T> onSuccess);

    /**
     * Calls the specified {@link Consumer} with the error value if this {@link Result} is {@link Error}.
     *
     * @param onFailure the {@link Consumer} that accepts the error value
     * @return the same instance
     */
    @NotNull Result<T, E> inspectError(@NotNull Consumer<? super E> onFailure);

    /**
     * Recovers the error and creates a new {@link Success} result.
     * <p>
     * If this {@link Result} is {@link Success}, this method does nothing.
     *
     * @param onFailure the {@link Function} recovers the error, and provides the success value
     * @return the new {@link Success}, or same instance if this {@link Result} is already {@link Success}
     */
    @NotNull Result<T, E> recover(@NotNull Function<? super E, ? extends T> onFailure);

    /**
     * Tries recovering the error using the specified {@link Function}.
     * <p>
     * If this {@link Result} is {@link Success}, this method does nothing.
     *
     * @param onFailure the {@link Function} tries recovering the error, and provides the new {@link Result}
     * @param <E2>      the new type of the error value
     * @return the new {@link Result} returned from {@link Function}, or same instance if this {@link Result} is {@link Success}
     * @throws NullPointerException if the {@link Function} returns {@code null} as {@link Result}
     */
    <E2> @NotNull Result<T, E2> tryRecover(@NotNull Function<? super E, Result<T, E2>> onFailure);

    /**
     * Unwraps the success value.
     * <p>
     * If this {@link Result} is {@link Failure}, this method throws a {@link NoSuchElementException}.
     *
     * @return the success value
     * @throws NoSuchElementException if this {@link Result} is {@link Failure}
     */
    T unwrap() throws NoSuchElementException;

    /**
     * Unwraps the success value, or returns the specified {@code defaultValue} if this {@link Result} is {@link Failure}.
     *
     * @param defaultValue the alternative value of {@link T}
     * @return the success value, or the specified {@code defaultValue} if this {@link Result} is {@link Failure}
     */
    T unwrapOr(T defaultValue);

    /**
     * Unwraps the success value, or get the value from the specified {@link Supplier} if this {@link Result} is {@link Failure}.
     *
     * @param supplier the {@link Supplier} to get the alternative value of {@link T}
     * @return the success value, or the value supplied by the specified {@link Supplier} if this {@link Result} is {@link Failure}
     */
    T unwrapOrGet(@NotNull Supplier<? extends T> supplier);

    /**
     * Unwraps the error value.
     * <p>
     * If this {@link Result} is {@link Success}, this method throws a {@link NoSuchElementException}.
     *
     * @return the error value
     * @throws NoSuchElementException if this {@link Result} is {@link Failure}
     */
    E unwrapError() throws NoSuchElementException;

    /**
     * Casts this result to {@link Result.Success}.
     * <p>
     * If this {@link Result} is {@link Failure}, this method throws a {@link ClassCastException}.
     *
     * @param <E2> the arbitrary error type
     * @return the same instance of this {@link Result}
     * @throws ClassCastException if this {@link Result} is {@link Failure}
     */
    <E2> Result.@NotNull Success<T, E2> asSuccess();

    /**
     * Casts this result to {@link Result.Failure}.
     * <p>
     * If this {@link Result} is {@link Success}, this method throws a {@link ClassCastException}.
     *
     * @param <U> the arbitrary value type
     * @return the same instance of this {@link Result}
     * @throws ClassCastException if this {@link Result} is {@link Success}
     */
    <U> Result.@NotNull Failure<U, E> asFailure();

    /**
     * An interface that represents the success {@link Result}.
     *
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     */
    sealed interface Success<T, E> extends Result<T, E> permits SuccessImpl {
    }

    /**
     * An interface that represents the failure {@link Result}.
     *
     * @param <T> the type of the success value
     * @param <E> the type of the error value
     */
    sealed interface Failure<T, E> extends Result<T, E> permits FailureImpl {
    }
}
