/*
 * Copyright (c) 2020 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.ditto.model.base.entity.id;

import javax.annotation.concurrent.Immutable;

/**
 * Validator validating {@code EntityIdWithType} instances.
 *
 * @since 1.1.0
 */
@Immutable
final class EntityIdWithTypeEqualityValidator extends EntityIdEqualityValidator<EntityIdWithType> {

    private EntityIdWithTypeEqualityValidator(final EntityIdWithType expected) {
        super(expected);
    }

    static EntityIdWithTypeEqualityValidator getInstance(final EntityIdWithType expected) {
        return new EntityIdWithTypeEqualityValidator(expected);
    }

}
