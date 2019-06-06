package multiex.jaxrs;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import multiex.core.LatLong;
import multiex.core.LatLongs;

@Path(LatLongService.LAT_LONG_SERVICE_PATH)
public class LatLongService {

	public static final String LAT_LONG_SERVICE_PATH = "latLong";

	@Inject
	private LatLongs latLongs;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public LatLongs getLatLongs() {
		return latLongs;
	}

	@GET
	@Path("/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public LatLong getLatLong(@PathParam("num") int num) {
		if (num < 0) {
			num = latLongs.getLatLongCount() + num;
		}
		return latLongs.getLatLong(num);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int addLatLongs(final List<LatLong> latLongs) {
		int result = -1;
		for (final LatLong latLong : latLongs) {
			final int pos = this.latLongs.addLatLong(latLong);
			if (result < 0) {
				result = pos;
			}
		}
		return result;
	}

	@PUT
	@Path("/{num}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public int setLatLong(final LatLong latLong, @PathParam("num") int num) {
		if (num < 0) {
			num = latLongs.getLatLongCount() + num;
		}
		latLongs.setLatLong(num, latLong);
		return num;
	}

	@DELETE
	@Path("/{num}")
	@Produces(MediaType.APPLICATION_JSON)
	public LatLong deleteLatLong(@PathParam("num") int num) {
		if (num < 0) {
			num = latLongs.getLatLongCount() + num;
		}
		return latLongs.removeLatLong(num);
	}
}
