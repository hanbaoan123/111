package jasima.core.util.observer;

import java.util.ArrayDeque;
import java.util.ArrayList;

import joptsimple.internal.Objects;

/**
 * Implementation of a {@link Notifier} functionality. A NotifierAdapter handles
 * notifier functionality for some real Notifier.
 * 
 * @author Torsten Hildebrandt
 */
public class NotifierAdapter<SUBJECT extends Notifier<SUBJECT, MESSAGE>, MESSAGE>
		implements Notifier<SUBJECT, MESSAGE> {

	private final SUBJECT subject;

	private ArrayList<Subscriber<SUBJECT, MESSAGE>> listener;
	private ArrayDeque<MESSAGE> msgs;
	private int it;

	public NotifierAdapter(SUBJECT subject) {
		super();
		this.subject = subject;
		this.listener = new ArrayList<>();
		this.msgs = null;
		this.it = -1;
	}

	@Override
	public int numListener() {
		return listener.size();
	}

	@Override
	public void addListener(Subscriber<SUBJECT, MESSAGE> l) {
		Objects.ensureNotNull(l);

		listener.add(l);
	}

	@Override
	public boolean removeListener(Subscriber<SUBJECT, MESSAGE> l) {
		Objects.ensureNotNull(l);

		int idx = listener.indexOf(l);
		if (idx < 0)
			return false;

		listener.remove(idx);

		// do we have to adjust iteration position?
		if (it >= 0 && it < idx) {
			it--;
			assert it >= 0;
		}

		return true;
	}

	@Override
	public Subscriber<SUBJECT, MESSAGE> getListener(int idx) {
		return listener.get(idx);
	}

	@Override
	public void fire(MESSAGE msg) {
		if (it >= 0) {
			// already firing, i.e., listener triggered another event
			if (msgs == null)
				msgs = new ArrayDeque<>();
			// enqueue msg for later processing
			msgs.addLast(msg);
		} else {
			// normal firing
			do {
				it = 0;
				while (it < listener.size()) {
					Subscriber<SUBJECT, MESSAGE> l = listener.get(it);
					it++;

					l.inform(subject, msg);
				}
				it = -1;
				msg = null;

				if (msgs != null && msgs.size() > 0)
					msg = msgs.removeFirst();
			} while (msg != null);
		}
	}

}
