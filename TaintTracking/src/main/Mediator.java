package main;

import java.util.List;

import security.ALevelMediator;
import security.ILevel;
import security.ILevelDefinition;

/**
 * DOC
 * 
 * @author Thomas Vogel
 * @version 0.2
 */
public class Mediator extends ALevelMediator {

    /**
     * DOC
     * 
     * @param definition
     */
    public Mediator(ILevelDefinition definition) {
        super(definition);
    }

    /**
     * DOC
     * 
     * @param levels
     * @return
     * 
     * @see ALevelMediator#checkParameterLevelsValidity(List)
     */
    @Override
    public boolean checkParameterLevelsValidity(List<ILevel> levels) {
        return checkLevelsValidity(levels);
    }

    /**
     * DOC
     * 
     * @param levels
     * @return
     * 
     * @see ALevelMediator#getInvalidParameterLevels(List)
     */
    @Override
    public List<ILevel> getInvalidParameterLevels(List<ILevel> levels) {
        return getInvalidLevels(levels);
    }

}
