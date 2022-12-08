package org.dimdev.dimdoors.criteria;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import org.dimdev.dimdoors.DimensionalDoors;

public class RiftTrackedCriterion extends AbstractCriterion<RiftTrackedCriterion.Conditions> {
	public static final Identifier ID = DimensionalDoors.id("rift_tracked");

	@Override
	protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		return new Conditions(playerPredicate);
	}

	public void trigger(ServerPlayerEntity player) {
		this.trigger(player, t -> true);
	}

	@Override
	public Identifier getId() {
		return ID;
	}

	public static class Conditions extends AbstractCriterionConditions {
		public Conditions(EntityPredicate.Extended playerPredicate) {
			super(ID, playerPredicate);
		}
	}
}
