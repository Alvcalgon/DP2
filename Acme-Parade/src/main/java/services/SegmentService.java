
package services;

import java.util.Collection;
import java.util.Date;
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
		//TODO: falta coordenada

		//Compruebo que la fecha del segmento actual está entre las fechas del segmento previo y el posterior(depende de su posicion en el camino)
		final Date fechaSegmentoPrevio;
		final Date fechaSegmentoPosterior;
		Collection<Segment> segmentsParadeCollection;
		segmentsParadeCollection = parade.getSegments();
		if (!segmentsParadeCollection.isEmpty()) {
			java.util.List<Segment> segmentsParade;

			segmentsParade = this.findOrderedSegments(parade.getId());

			//Recorremos el camino para ver la posición del segmento
			int i = 0;
			final int tamaño = segmentsParade.size();
			for (final Segment s : segmentsParade) {
				if (s.equals(segment) && i == 0) { //si es el primer segmento del camino 
					if (tamaño != 1)
						if (!s.getReachingDestination().equals(segmentsParade.get(i + 1).getReachingOrigin()))
							throw new DataIntegrityViolationException("Invalid data");
				} else if (s.equals(segment) && i >= 0 && i <= tamaño - 1) { // si no es ni el primero ni el ultimo
					if (!s.getReachingDestination().equals(segmentsParade.get(i + 1).getReachingOrigin()) || !s.getReachingOrigin().equals(segmentsParade.get(i - 1).getReachingDestination()))
						throw new DataIntegrityViolationException("Invalid data");
				} else if (s.equals(segment) && i == tamaño - 1)
					//solo tengo que mirar el inicio de s con el final del anterior
					if (!s.getReachingOrigin().equals(segmentsParade.get(i - 1).getReachingDestination()))
						throw new DataIntegrityViolationException("Invalid data");

				i = i + 1;
			}

		}

	}

	private void checkSegmentCreate(final Segment segment, final Parade parade) {
		//TODO: falta coordenada

		//Compruebo que la fecha del segmento actual está entre las fechas del segmento previo y el posterior(depende de su posicion en el camino)
		final Date fechaSegmentoPrevio;
		final Date fechaSegmentoPosterior;
		Collection<Segment> segmentsParadeCollection;
		Segment segmentLast;

		segmentsParadeCollection = parade.getSegments();
		if (!segmentsParadeCollection.isEmpty()) {
			java.util.List<Segment> segmentsParade;

			segmentsParade = this.findOrderedSegments(parade.getId());

			//Recorremos el camino para ver la posición del segmento
			final int tamaño = segmentsParade.size();

			segmentLast = segmentsParade.get(tamaño - 1); // cojo el ultimo segmento

			final int i = segment.getReachingOrigin().compareTo(segmentLast.getReachingDestination());

			if (segment.getReachingOrigin().compareTo(segmentLast.getReachingDestination()) != 1)
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
