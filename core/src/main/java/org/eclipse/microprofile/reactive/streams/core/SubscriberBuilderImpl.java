/*******************************************************************************
 * Copyright (c) 2018 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package org.eclipse.microprofile.reactive.streams.core;

import org.eclipse.microprofile.reactive.streams.CompletionSubscriber;
import org.eclipse.microprofile.reactive.streams.SubscriberBuilder;
import org.eclipse.microprofile.reactive.streams.spi.Graph;
import org.eclipse.microprofile.reactive.streams.spi.ReactiveStreamsEngine;
import org.eclipse.microprofile.reactive.streams.spi.Stage;

import java.util.Objects;

public final class SubscriberBuilderImpl<T, R> extends ReactiveStreamsGraphBuilder implements SubscriberBuilder<T, R> {

    SubscriberBuilderImpl(Stage stage, ReactiveStreamsGraphBuilder previous) {
        super(stage, previous);
    }

    @Override
    public CompletionSubscriber<T, R> build() {
        return build(defaultEngine());
    }

    @Override
    public CompletionSubscriber<T, R> build(ReactiveStreamsEngine engine) {
        Objects.requireNonNull(engine, "Engine must not be null");
        org.eclipse.microprofile.reactive.streams.spi.CompletionSubscriber<T, R> spiCs = engine.buildSubscriber(toGraph());
        if (spiCs instanceof CompletionSubscriber) {
            return (CompletionSubscriber) spiCs;
        }
        else {
            return CompletionSubscriber.of(spiCs, spiCs.getCompletion());
        }
    }

    public Graph toGraph() {
        return build(true, false);
    }
}
