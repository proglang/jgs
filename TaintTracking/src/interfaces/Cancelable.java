package interfaces;

public interface Cancelable {

	public void addCancelListener(Cancelable cancelable);

	public void cancel();

	public void removeCancelListener(Cancelable cancelable);

}
