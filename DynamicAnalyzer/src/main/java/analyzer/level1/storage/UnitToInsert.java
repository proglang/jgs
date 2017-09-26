package analyzer.level1.storage;

import soot.Unit;

/**
 * A unit that should be inserted "near of" another unit
 * Created by fennell on 26.09.17.
 */
// TODO: this class does not make sense and should be removed. Just use units instead.
public class UnitToInsert {
  Unit unit;
  Unit position;

  public UnitToInsert(Unit newUnit, Unit position) {
    unit = newUnit;
    this.position = position;
  }

  public Unit getUnit() {
    return unit;
  }

    public Unit getPosition() {
    return position;
  }
}
