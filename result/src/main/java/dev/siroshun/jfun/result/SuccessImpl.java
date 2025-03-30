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

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

record SuccessImpl<T, E>(@Nullable T value) implements Result.Success<T, E> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final SuccessImpl INSTANCE = new SuccessImpl(null);

    @SuppressWarnings("unchecked")
    static <T, E> @NotNull SuccessImpl<T, E> nullSuccess() {
        return (SuccessImpl<T, E>) INSTANCE;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public @NotNull Optional<T> toOptional() {
        return Optional.ofNullable(this.value);
    }

    @Override
    public @NotNull Optional<E> toOptionalError() {
        return Optional.empty();
    }

    @Override
    public @NotNull <U> Result<U, E> map(@NotNull Function<? super T, ? extends U> mapper) {
        U result = mapper.apply(this.value);
        return Result.success(result);
    }

    @Override
    public @NotNull <U> Result<U, E> mapOr(@NotNull Function<? super T, ? extends U> mapper, U defaultValue) {
        return this.map(mapper);
    }

    @Override
    public @NotNull <U> Result<U, E> mapOrGet(@NotNull Function<? super T, ? extends U> mapper, @NotNull Supplier<? extends U> supplier) {
        return this.map(mapper);
    }

    @Override
    public @NotNull <U> Result<U, E> mapOrElse(@NotNull Function<? super T, ? extends U> mapper, @NotNull Function<? super E, ? extends U> onFailure) {
        return this.map(mapper);
    }

    @Override
    public @NotNull <O> Result<T, O> mapError(@NotNull Function<? super E, ? extends O> mapper) {
        return this.castError();
    }

    @Override
    public @NotNull <U, E2> Result<U, E2> flatMap(@NotNull Function<? super T, Result<U, E2>> onSuccess, @NotNull Function<? super E, Result<U, E2>> onFailure) {
        return Objects.requireNonNull(onSuccess.apply(this.value));
    }

    @Override
    public @NotNull Result<T, E> inspect(@NotNull Consumer<? super T> onSuccess) {
        onSuccess.accept(this.value);
        return this;
    }

    @Override
    public @NotNull Result<T, E> inspectError(@NotNull Consumer<? super E> onFailure) {
        return this;
    }

    @Override
    public @NotNull Result<T, E> recover(@NotNull Function<? super E, ? extends T> onFailure) {
        return this;
    }

    @Override
    public @NotNull <E2> Result<T, E2> tryRecover(@NotNull Function<? super E, Result<T, E2>> onFailure) {
        return this.castError();
    }

    @Override
    public T unwrap() throws NoSuchElementException {
        return this.value;
    }

    @Override
    public T unwrapOr(T defaultValue) {
        return this.unwrap();
    }

    @Override
    public T unwrapOrGet(@NotNull Supplier<? extends T> supplier) {
        return this.unwrap();
    }

    @Override
    public E unwrapError() throws NoSuchElementException {
        throw new NoSuchElementException("Result.Success cannot unwrap error.");
    }

    @Override
    public <E2> Result.@NotNull Success<T, E2> asSuccess() {
        return this.castError();
    }

    @Override
    public <U> Result.@NotNull Failure<U, E> asFailure() {
        throw new ClassCastException("Result.Success cannot cast to Result.Failure.");
    }

    @SuppressWarnings("unchecked")
    @Contract("-> this")
    private <E2> Result.@NotNull Success<T, E2> castError() {
        return (Result.Success<T, E2>) this;
    }
}
