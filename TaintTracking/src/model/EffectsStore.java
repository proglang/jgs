package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import security.Annotations.WriteEffect;
import security.SecurityAnnotation;


/**
 * Class which provides the storing of effects with specific effect types as well as invoked methods
 * and used classes.
 * 
 * @author Thomas Vogel
 * @version 0.3
 * 
 */
public class EffectsStore {
	
	/**
	 * Wrapper for storing effects which are of a single type (e.g. read effect, new effect or write
	 * effect). The class provides a map which maps the name of an effected class to a list of
	 * effects. All these effects affect the same class. Separate
	 * 
	 * @author Thomas Vogel
	 * @version 0.2
	 */
	private class SeparateEffectsStore {

		private Map<String, List<Effect>> effects;

		/**
		 * Constructor of a EffectsStorage which stores effects of the same type depending on the
		 * class which the effects affect.
		 */
		private SeparateEffectsStore() {
			super();
			this.effects = new HashMap<String, List<Effect>>();
		}

		/**
		 * Adds an effect depending on the effected class to the storage.
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
		 * Returns a set of all effected class names, thus the key set of the map.
		 * 
		 * @return The set of all effected classes
		 */
		private Set<String> makeEffectsSet() {
			return this.effects.keySet();
		}

		/**
		 * Returns the list of effects for a specific effected class name.
		 * 
		 * @param effected
		 *            Name of the class which is effected.
		 * @return List of effects which affect the given class.
		 */
		private List<Effect> getEffects(String effected) {
			return this.effects.get(effected);
		}

	}
	
	private Map<String, SeparateEffectsStore> effectsStore = new HashMap<String, SeparateEffectsStore>();
	
	/**
	 * Constructor of an EffectsStore object which stores effects of all effect types.
	 */
	public EffectsStore() {
		super();
		effectsStore.put(SecurityAnnotation.getEffectIdentifier(WriteEffect.class), new SeparateEffectsStore());
	}
	
	/**
	 * Adds a write effect to the storage.
	 * 
	 * @param effect
	 *            Write effect which should be stored.
	 */
	public void addWriteEffect(Effect effect) {
		this.effectsStore.get(SecurityAnnotation.getEffectIdentifier(WriteEffect.class)).addEffect(effect);
	}
	
	/**
	 * Returns a set of the class names which are effected by a write effect.
	 * 
	 * @return Set containing the write effect class names.
	 */
	public Set<String> getWriteEffectSet() {
		return this.effectsStore.get(SecurityAnnotation.getEffectIdentifier(WriteEffect.class)).makeEffectsSet();
	}
	
	/**
	 * Returns a list of effects where each effect is a write effect and affects the given class.
	 * 
	 * @param effected
	 *            Class name which should be affected by the resulting effects.
	 * @return List of write effects affecting the given class.
	 */
	public List<Effect> getWriteEffects(String effected) {
		return this.effectsStore.get(SecurityAnnotation.getEffectIdentifier(WriteEffect.class)).getEffects(effected);
	}
	
}
