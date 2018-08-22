/*
 * Copyright (c) 2017 Bosch Software Innovations GmbH.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/org/documents/epl-2.0/index.php
 * Contributors:
 *    Bosch Software Innovations GmbH - initial contribution
 *
 */
package org.eclipse.ditto.services.things.persistence.actors.strategies.commands;

import java.util.Optional;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import org.eclipse.ditto.model.base.headers.DittoHeaders;
import org.eclipse.ditto.model.things.Features;
import org.eclipse.ditto.model.things.Thing;
import org.eclipse.ditto.signals.commands.things.modify.ModifyFeatures;
import org.eclipse.ditto.signals.commands.things.modify.ModifyFeaturesResponse;
import org.eclipse.ditto.signals.events.things.FeaturesCreated;
import org.eclipse.ditto.signals.events.things.FeaturesModified;

/**
 * This strategy handles the {@link org.eclipse.ditto.signals.commands.things.modify.ModifyFeatures} command.
 */
@Immutable
final class ModifyFeaturesStrategy
        extends AbstractConditionalHeadersCheckingCommandStrategy<ModifyFeatures, Features> {

    /**
     * Constructs a new {@code ModifyFeaturesStrategy} object.
     */
    ModifyFeaturesStrategy() {
        super(ModifyFeatures.class);
    }

    @Override
    protected Result doApply(final Context context, @Nullable final Thing thing,
            final long nextRevision, final ModifyFeatures command) {

        return extractFeatures(thing)
                .map(features -> getModifyResult(context, nextRevision, command))
                .orElseGet(() -> getCreateResult(context, nextRevision, command));
    }

    private Optional<Features> extractFeatures(final @Nullable Thing thing) {
        return getThingOrThrow(thing).getFeatures();
    }

    private Result getModifyResult(final Context context, final long nextRevision,
            final ModifyFeatures command) {
        final DittoHeaders dittoHeaders = command.getDittoHeaders();

        return ResultFactory.newMutationResult(command,
                FeaturesModified.of(command.getId(), command.getFeatures(), nextRevision,
                        getEventTimestamp(), dittoHeaders),
                ModifyFeaturesResponse.modified(context.getThingId(), dittoHeaders),
                this);
    }

    private Result getCreateResult(final Context context, final long nextRevision,
            final ModifyFeatures command) {
        final Features features = command.getFeatures();
        final DittoHeaders dittoHeaders = command.getDittoHeaders();

        return ResultFactory.newMutationResult(command,
                FeaturesCreated.of(command.getId(), features, nextRevision, getEventTimestamp(),
                        dittoHeaders),
                ModifyFeaturesResponse.created(context.getThingId(), features, dittoHeaders),
                this);
    }


    @Override
    public Optional<Features> determineETagEntity(final ModifyFeatures command, @Nullable final Thing thing) {
        return extractFeatures(thing);
    }
}
