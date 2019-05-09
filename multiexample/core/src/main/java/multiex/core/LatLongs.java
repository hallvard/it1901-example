package multiex.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LatLongs implements Iterable<LatLong> {

	final List<LatLong> latLongs = new ArrayList<>();

	public LatLongs() {
	}

	public LatLongs(final double... latLongsArray) {
		addLatLongs(latLongsArray);
	}

	public LatLongs(final LatLong... latLongs) {
		addLatLongs(latLongs);
	}

	public LatLongs(final Collection<LatLong> latLongs) {
		addLatLongs(latLongs);
	}

	@Override
	public Iterator<LatLong> iterator() {
		return latLongs.iterator();
	}

	public int getLatLongCount() {
		return latLongs.size();
	}

	public LatLong getLatLong(final int num) {
		return latLongs.get(num);
	}

	public void setLatLong(final int num, final LatLong latLong) {
		latLongs.set(num, latLong);
	}

	public int addLatLong(final LatLong latLong) {
		final int pos = latLongs.size();
		latLongs.add(latLong);
		return pos;
	}

	public int addLatLongs(final Collection<LatLong> latLongs) {
		final int pos = this.latLongs.size();
		this.latLongs.addAll(latLongs);
		return pos;
	}

	public int addLatLongs(final LatLong... latLongs) {
		return addLatLongs(List.of(latLongs));
	}

	public int addLatLongs(final double... latLongsArray) {
		final Collection<LatLong> latLongs = new ArrayList<>(latLongsArray.length / 2);
		for (int i = 0; i < latLongsArray.length; i += 2) {
			latLongs.add(new LatLong(latLongsArray[i], latLongsArray[i + 1]));
		}
		return addLatLongs(latLongs);
	}

	public LatLong removeLatLong(final int num) {
		return latLongs.remove(num);
	}
}
