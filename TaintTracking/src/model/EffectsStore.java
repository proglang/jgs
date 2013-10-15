package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.Annotations.WriteEffect;
import security.SecurityAnnotation;

/**
 * <h1>Store of calculate effects</h1>
 * 
 * The {@link EffectsStore} allows to store calculated effects to a specific
 * <em>security levels</em> . In this version the store only provides the storing of
 * <em>write effects</em>. Therefore the {@link EffectsStore} provides methods to add and to lookup
 * a <em>write effect</em>.
 * 
 * <h2>Internal behaviour</h2>
 * 
 * For efficiency reasons, the effects will be stored in a map which maps a specific effect type,
 * such as a <em>write effect</em>, to a {@link EffectTypeStore} that contains only effects of this
 * specific effect type. Each {@link EffectTypeStore} contains a map which maps an effected
 * <em>security level</em> to a list of effects which all effect this <em>security level</em>.
 * 
 * <hr />
 * 
 * @author Thomas Vogel
 * @version 0.3
 * 
 */
public class EffectsStore {

	/**
	 * <h1>Internal store for a single effect type</h1>
	 * 
	 * The {@link EffectTypeStore} stores effects which are of a single type (e.g.
	 * <em>read effect</em>, <em>new effect</em> or <em>write effect</em>). The class provides a map
	 * which maps the effected <em>security level</em> to a list of effects. All these effects
	 * affect the same <em>security level</em>.
	 * 
	 * <hr />
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 */
	private class EffectTypeStore {

		/**
		 * Map that maps for every specific effected <em>security level</em> to a list of effects
		 * that all affect this specific <em>security level</em>.
		 */
		private Map<String, List<Effect>> effects;

		/**
		 * Constructor of a {@link EffectTypeStore} which stores effects of the same effect type
		 * depending on the <em>security level</em> which the effects affect.
		 */
		private EffectTypeStore() {
			super();
			this.effects = new HashMap<String, List<Effect>>();
		}

		/**
		 * Adds an effect depending on the effected <em>security level</em> to the storage.
		 * 
		 * @param effect
		 *            The effect which should be stored.
		 */
		private void addEffect(Effect effect) {
			if (!this.effects.containsKey(effect.getEffected())) {
				this.effects.put(effect.getEffected(), new ArrayList<Effect>());
			}
			this.effects.get(effect.getEffected()).add(effect);
		}

		/**
		 * Returns the list of effects for a specific effected <em>security level</em>.
		 * 
		 * @param effected
		 *            The <em>security level</em> which is effected.
		 * @return List of effects which affect the given <em>security level</em>.
		 */
		private List<Effect> getEffects(String effected) {
			return this.effects.get(effected);
		}

		/**
		 * Returns a set of all effected <em>security levels</em>, thus the key set of the map.
		 * 
		 * @return The set of all effected <em>security levels</em>.
		 */
		private Set<String> makeEffectsSet() {
			return this.effects.keySet();
		}

	}

	/**
	 * Map that maps for every specific effect type to a {@link EffectTypeStore} which contains only
	 * effects of this specific type.
	 */
	private Map<String, EffectTypeStore> effectsStore = new HashMap<String, EffectTypeStore>();

	/**
	 * Constructor of an {@link EffectsStore} object which stores effects (in the current version
	 * only <em>write effects</em>.
	 */
	public EffectsStore() {
		super();
		effectsStore.put(SecurityAnnotation.getEffectIdentifier(WriteEffect.class),
				new EffectTypeStore());
	}

	/**
	 * Adds a write effect to the storage.
	 * 
	 * @param effect
	 *            Write effect which should be stored.
	 */
	public void addWriteEffect(Effect effect) {
		this.effectsStore.get(SecurityAnnotation.getEffectIdentifier(WriteEffect.class)).addEffect(
				effect);
	}

	/**
	 * Returns a list of effects where each effect is a write effect and affects the given
	 * <em>security level</em>.
	 * 
	 * @param effected
	 *            <em>Security level</em> which should be affected by the resulting effects.
	 * @return List of write effects affecting the given <em>security level</em>.
	 */
	public List<Effect> getWriteEffects(String effected) {
		return this.effectsStore.get(SecurityAnnotation.getEffectIdentifier(WriteEffect.class))
				.getEffects(effected);
	}

	/**
	 * Returns a set of the <em>security levels</em> which are effected by a write effect.
	 * 
	 * @return Set containing the write effected <em>security levels</em>.
	 */
	public Set<String> getWriteEffectSet() {
		return this.effectsStore.get(SecurityAnnotation.getEffectIdentifier(WriteEffect.class))
				.makeEffectsSet();
	}

}
