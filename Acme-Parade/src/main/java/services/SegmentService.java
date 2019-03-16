
package services;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SegmentRepository;
import domain.Brotherhood;
import domain.Parade;
import domain.Segment;

@Service
@Transactional
public class SegmentService {

	// Managed repository --------------------------

	@Autowired
	private SegmentRepository	segmentRepository;

	// Other supporting services -------------------

	@Autowired
	private BrotherhoodService	brotherhoodService;

	@Autowired
	private ParadeService		paradeService;


	// Constructors -------------------------------

	public SegmentService() {
		super();
	}

	// Simple CRUD methods ------------------------

	public Segment create() {
		Segment result;

		result = new Segment();

		return result;

	}

	public Segment save(final Segment segment, Parade parade) {
		Assert.notNull(segment);

		Segment result;
		boolean isUpdating;

		isUpdating = this.segmentRepository.exists(segment.getId());

		if (isUpdating) {

			parade = this.paradeService.findBySegment(segment.getId());
			this.paradeService.checkParadeByBrotherhood(parade);
			this.checkSegmentEdit(segment, parade);
		} else {
			this.paradeService.checkParadeByBrotherhood(parade);
			this.checkSegmentCreate(segment, parade);
		}

		Assert.notNull(parade);

		this.checkSegment(segment, parade);
		result = this.segmentRepository.save(segment);

		if (!isUpdating)
			this.addSegmentToParade(parade, result);

		return result;

	}

	private void checkSegment(final Segment segment, final Parade parade) {
		//Compruebo que la fecha origen es antes que la destino
		if (!segment.getReachingDestination().after(segment.getReachingOrigin()))
			throw new DataIntegrityViolationException("Invalid date");

		//Comprueba que las fechas del segmento son posteriores a la del desfile
		if (!segment.getReachingDestination().after(parade.getMoment()) || !segment.getReachingOrigin().after(parade.getMoment()))
			throw new DataIntegrityViolationException("Invalid dates");
	}

	private void checkSegmentEdit(final Segment segment, final Parade parade) {
		//Compruebo que la fecha del segmento actual está entre las fechas del segmento previo y el posterior(depende de su posicion en el camino)
		Collection<Segment> segmentsParadeCollection;
		final SimpleDateFormat formatter;
		final int tamaño;
		String segmentOriginString;
		String segmentDestinationString;

		segmentsParadeCollection = parade.getSegments();

		if (!segmentsParadeCollection.isEmpty()) {
			java.util.List<Segment> segmentsParade;
			segmentsParade = this.findOrderedSegments(parade.getId());
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");

			//Recorremos el camino para ver la posición del segmento
			int i = 0;
			tamaño = segmentsParade.size();
			for (final Segment s : segmentsParade) {

				segmentOriginString = formatter.format(s.getReachingOrigin());
				segmentDestinationString = formatter.format(s.getReachingDestination());

				if (s.equals(segment) && segmentsParade.indexOf(s) == 0) { //si es el primer segmento del camino 
					if (tamaño > 1)
						if (!segmentDestinationString.equals(formatter.format(segmentsParade.get(i + 1).getReachingOrigin())) || s.getDestination().equals(segmentsParade.get(i + 1).getOrigin()))
							throw new DataIntegrityViolationException("Invalid data");
				} else if (s.equals(segment) && i >= 0 && i <= tamaño - 1) { // si no es ni el primero ni el ultimo
					if (!segmentDestinationString.equals(formatter.format(segmentsParade.get(i + 1).getReachingOrigin())) || !segmentOriginString.equals(formatter.format(segmentsParade.get(i - 1).getDestination()))
						|| !(s.getDestination().getLatitude() == (segmentsParade.get(i + 1).getOrigin().getLatitude())) || !(s.getDestination().getLongitude() == (segmentsParade.get(i + 1).getOrigin().getLongitude()))
						|| !(s.getOrigin().getLatitude() == (segmentsParade.get(i - 1).getDestination().getLatitude())) || !(s.getOrigin().getLongitude() == (segmentsParade.get(i - 1).getDestination().getLongitude())))
						throw new DataIntegrityViolationException("Invalid data");
				} else if (s.equals(segment) && i == tamaño - 1)
					//solo tengo que mirar el inicio de s con el final del anterior
					if (!segmentOriginString.equals(formatter.format(segmentsParade.get(i - 1).getReachingDestination())) || !s.getOrigin().equals(segmentsParade.get(i - 1).getDestination()))
						throw new DataIntegrityViolationException("Invalid data");

				i = i + 1;
			}

		}

	}

	private void checkSegmentCreate(final Segment segment, final Parade parade) {

		//Compruebo que la fecha del segmento actual está entre las fechas del segmento previo y el posterior(depende de su posicion en el camino)
		Collection<Segment> segmentsParadeCollection;
		Segment segmentLast;
		final SimpleDateFormat formatter;
		final String segementOriginString;
		final String segmentLastDestinationString;
		final int tamaño;

		segmentsParadeCollection = parade.getSegments();
		if (!segmentsParadeCollection.isEmpty()) {
			java.util.List<Segment> segmentsParade;

			segmentsParade = this.findOrderedSegments(parade.getId());
			tamaño = segmentsParade.size();
			segmentLast = segmentsParade.get(tamaño - 1); // cojo el ultimo segmento
			formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			segementOriginString = formatter.format(segment.getReachingOrigin());
			segmentLastDestinationString = formatter.format(segmentLast.getReachingDestination());

			if (!segementOriginString.equals(segmentLastDestinationString) || !(segment.getOrigin().getLatitude() == (segmentLast.getDestination().getLatitude())) || !(segment.getOrigin().getLongitude() == (segmentLast.getDestination().getLongitude())))
				throw new DataIntegrityViolationException("Invalid data");

		}

	}
	public void delete(final Segment segment) {
		Assert.notNull(segment);
		Assert.isTrue(this.segmentRepository.exists(segment.getId()));

		Parade parade;
		Brotherhood principal;

		parade = this.paradeService.findBySegment(segment.getId());
		principal = this.brotherhoodService.findByPrincipal();

		Assert.isTrue(this.paradeService.getBrotherhoodToParade(parade).equals(principal));
		Assert.isTrue(this.isDeletable(segment));

		this.removeSegmentToParade(parade, segment);

		this.segmentRepository.delete(segment);

	}

	public Segment findOne(final int segmentId) {
		Segment result;

		result = this.segmentRepository.findOne(segmentId);

		return result;
	}

	public Collection<Segment> findAll() {
		Collection<Segment> results;

		results = this.segmentRepository.findAll();

		return results;
	}

	public Segment findOneToEdit(final int segmentId) {
		Segment result;
		Brotherhood principal;

		result = this.segmentRepository.findOne(segmentId);
		principal = this.brotherhoodService.findByPrincipal();

		Assert.notNull(result);
		Assert.isTrue(this.brotherhoodService.findBrotherhoodBySegment(segmentId).equals(principal));

		return result;
	}

	// Other business methods ---------------------

	private void removeSegmentToParade(final Parade parade, final Segment segment) {
		Collection<Segment> segments;

		segments = parade.getSegments();
		segments.remove(segment);

	}

	private void addSegmentToParade(final Parade parade, final Segment segment) {
		Collection<Segment> segments;

		segments = parade.getSegments();
		segments.add(segment);

	}

	public List<Segment> findOrderedSegments(final int paradeId) {
		List<Segment> segmentsOrdered;

		segmentsOrdered = this.segmentRepository.findOrderedSegments(paradeId);

		return segmentsOrdered;
	}
	public Boolean isDeletable(final Segment segment) {
		Boolean result;
		Parade parade;
		List<Segment> segments;

		result = false;
		parade = this.paradeService.findBySegment(segment.getId());
		segments = this.segmentRepository.findOrderedSegments(parade.getId());

		if (segments.indexOf(segment) == 0 || segments.indexOf(segment) == segments.size() - 1)
			result = true;

		return result;
	}

}
