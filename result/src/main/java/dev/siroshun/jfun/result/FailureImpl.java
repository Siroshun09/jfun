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
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

record FailureImpl<T, E>(@Nullable E e) implements Result.Failure<T, E> {

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static final FailureImpl INSTANCE = new FailureImpl(null);

    @SuppressWarnings("unchecked")
    static <T, E> FailureImpl<T, E> nullFailure() {
        return (FailureImpl<T, E>) INSTANCE;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public @NonNull Optional<T> toOptional() {
        return Optional.empty();
    }

    @Override
    public @NonNull Optional<E> toOptionalError() {
        return Optional.ofNullable(this.e);
    }

    @Override
    public @NonNull <U> Result<U, E> map(@NonNull Function<? super T, ? extends U> mapper) {
        return this.cast();
    }

    @Override
    public @NonNull <U> Result<U, E> mapOr(@NonNull Function<? super T, ? extends U> mapper, U defaultValue) {
        return Result.success(defaultValue);
    }

    @Override
    public @NonNull <U> Result<U, E> mapOrGet(@NonNull Function<? super T, ? extends U> mapper, @NonNull Supplier<? extends U> supplier) {
        return Result.success(supplier.get());
    }

    @Override
    public @NonNull <U> Result<U, E> mapOrElse(@NonNull Function<? super T, ? extends U> mapper, @NonNull Function<? super E, ? extends U> onFailure) {
        return Result.success(onFailure.apply(this.e));
    }

    @Override
    public @NonNull <O> Result<T, O> mapError(@NonNull Function<? super E, ? extends O> mapper) {
        return Result.failure(mapper.apply(this.e));
    }

    @Override
    public @NonNull <U, E2> Result<U, E2> flatMap(@NonNull Function<? super T, Result<U, E2>> onSuccess, @NonNull Function<? super E, Result<U, E2>> onFailure) {
        return Objects.requireNonNull(onFailure.apply(this.e));
    }

    @Override
    public @NonNull Result<T, E> inspect(@NonNull Consumer<? super T> onSuccess) {
        return this;
    }

    @Override
    public @NonNull Result<T, E> inspectError(@NonNull Consumer<? super E> onFailure) {
        onFailure.accept(this.e);
        return this;
    }

    @Override
    public @NonNull Result<T, E> recover(@NonNull Function<? super E, ? extends T> onFailure) {
        return Result.success(onFailure.apply(this.e));
    }

    @Override
    public @NonNull <E2> Result<T, E2> tryRecover(@NonNull Function<? super E, Result<T, E2>> onFailure) {
        return Objects.requireNonNull(onFailure.apply(this.e));
    }

    @Override
    public T unwrap() throws NoSuchElementException {
        throw new NoSuchElementException("Result.Failure cannot unwrap value.");
    }

    @Override
    public T unwrapOr(T defaultValue) {
        return defaultValue;
    }

    @Override
    public T unwrapOrGet(@NonNull Supplier<? extends T> supplier) {
        return supplier.get();
    }

    @Override
    public E unwrapError() throws NoSuchElementException {
        return this.e;
    }

    @SuppressWarnings("unchecked")
    @Contract("-> this")
    private <U> @NonNull Result<U, E> cast() {
        return (Result<U, E>) this;
    }
}
